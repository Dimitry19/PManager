package cm.travelpost.tp.ws.client;

import cm.framework.ds.common.security.CommonSecurityResource;
import cm.travelpost.tp.common.utils.HTMLEntities;
import cm.travelpost.tp.common.utils.StringUtils;
import cm.travelpost.tp.common.utils.Utility;
import cm.travelpost.tp.ws.responses.Response;
import org.apache.commons.lang3.BooleanUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Scope("singleton")
public class RestClient  extends CommonSecurityResource {

    RestTemplate restTemplate;

    private HttpHeaders headers;

    private static final String KEY="key=";
    private static final String DATA_KEY="data";
    private static final String COUNTRY_KEY="country";
    private static final String ISO3_KEY="iso3";
    private static final String MESSAGE_KEY="msg";
    private static final String ERROR_KEY="error";
    private static final String COUNTRY_CODE="countryCode=";
    private static Logger logger = LoggerFactory.getLogger(RestClient.class);

    @Value("${bigdatacloud.api.key}")
    private String bigDataCloudApiKey;

    @Value("${bigdatacloud.api.geolocation.url}")
    private String baseUrl;

    @Value("${bigdatacloud.api.geolocation.countries.list}")
    private String countriesUrl;

    @Value("${bigdatacloud.api.geolocation.email.validation}")
    private String emailValidationUrl;

    @Value("${bigdatacloud.api.geolocation.phone.validation}")
    private String phoneValidationUrl;

    @Value("${countriesnow.api.countries.cities.get}")
    private String countriesCitiesUrl;

    @Value("${countriesnow.api.cities.country.get}")
    private String citiesCountryUrl;

    @Value("${countriesnow.api.countries.info.get}")
    private String countriesInfoUrl;

    private String decryptedApiKey;


    @PostConstruct
    public void init() {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        this.headers.setContentType(MediaType.APPLICATION_JSON);
        this.headers.add("Accept", "application/json");
        this.decryptedApiKey= encryptorBean.decrypt(bigDataCloudApiKey);
    }

    public String bigDataCountries() {
        Response response=new Response();
        try {
            logger.info("Start retrieve all countries (...)");
            String url =buildCompleteUrl(baseUrl,countriesUrl,decryptedApiKey);

            String res= restTemplate.getForObject(url, String.class);
            JSONArray array = new JSONArray(res);
            return res;
        }
        catch(Exception e) {
            String errorMessage = String.format("countries Error: %s", Utility.getStackTrace(e));
            logger.error(errorMessage);
            response.setRetCode(-1);
            throw new RestClientException(errorMessage);
        }
    }

    public String phoneEmailValidation(String toValidate,String countryCode) {
        Response response=new Response();
        try {
            logger.info("Start phoneEmailValidation(...)");

            String url =buildCompleteUrl(baseUrl,emailValidationUrl,toValidate, HTMLEntities.AND,KEY,decryptedApiKey);

            if(StringUtils.isNotEmpty(countryCode)){
                url =buildCompleteUrl(baseUrl,phoneValidationUrl,toValidate, HTMLEntities.AND,COUNTRY_CODE,countryCode,HTMLEntities.AND,KEY,decryptedApiKey);
            }
            String res= restTemplate.getForObject(url, String.class);
            JSONObject array = new JSONObject(res);
            return res;
        }
        catch(Exception e) {
            String errorMessage = String.format("phoneEmailValidation Error: %s", Utility.getStackTrace(e));
            logger.error(errorMessage);
            response.setRetCode(-1);
            throw new RestClientException(errorMessage);
        }
    }


    public Set countriesInfos(){
        Set countries=new HashSet();
        try {
            logger.info("Start countriesInfos (...)");
            String obj = restTemplate.getForObject(countriesInfoUrl,  String.class);
            countries=setCountries(obj,true);
        } catch(Exception e) {
            String errorMessage = String.format("countriesAndCities Error: %s", Utility.getStackTrace(e));
            logger.error(errorMessage);
            throw new RestClientException(errorMessage);
        }
        return countries;
    }
    public Set countriesAndCities(){
        Set countries=new HashSet();
        try {
            logger.info("Start countriesAndCities (...)");
            String obj = restTemplate.getForObject(countriesCitiesUrl,  String.class);
            readDataCountry(obj);
            countries=setCountries(obj,false);
        } catch(Exception e) {
            String errorMessage = String.format("countriesAndCities Error: %s", Utility.getStackTrace(e));
            logger.error(errorMessage);
            throw new RestClientException(errorMessage);
        }
        return countries;
    }

    public Set citiesFromCountry(String country){
        Set  cities=new HashSet();
        try {
            logger.info("Start citiesFromCountry (...)");
            String url =buildCompleteUrl(citiesCountryUrl,country);
            String obj = restTemplate.getForObject(url,String.class);
            readDataCities(obj);
            cities =setCities(obj);
        } catch(Exception e) {
            String errorMessage = String.format("citiesFromCountry Error: %s", Utility.getStackTrace(e));
            logger.error(errorMessage);
            throw new RestClientException(e.getMessage());
        }
        return cities;
    }

    private Set setCities(String response){
        if (StringUtils.isEmpty(response)){
            return null;
        }
        JSONObject json = new JSONObject(response);
        boolean error = json.getBoolean(ERROR_KEY);
        if (BooleanUtils.isTrue(error)){
            return null;
        }
        String msg = json.getString(MESSAGE_KEY);
        JSONArray data=json.getJSONArray(DATA_KEY);
        List<String> cities =new ArrayList<>();
        for(int i=0; i < data.length(); i++) {
            System.out.println(data.get(i));
            cities.add((String) data.get(i));
        }
        Collections.sort(cities);
        cities.stream().forEach(System.out::println);
        return cities.stream().collect(Collectors.toSet());
    }

    private Set setCountries(String response, boolean isInfo){
        List<Country> countries = new ArrayList();
        if (StringUtils.isEmpty(response)){
            return null;
        }
        JSONObject json = new JSONObject(response);
        boolean error = json.getBoolean(ERROR_KEY);
        if (BooleanUtils.isTrue(error)){
            return null;
        }
        String msg = json.getString(MESSAGE_KEY);
        JSONArray data=json.getJSONArray(DATA_KEY);
        for(int i=0; i < data.length(); i++) {
            JSONObject object = data.getJSONObject(i);
            Country country = new Country();

            if(BooleanUtils.isTrue(isInfo)){
                country.setName(object.getString("name"));
                country.setFlag(object.getString("flag"));
                country.setCurrency(object.getString("currency"));
                country.setDialCode(object.getString("dialCode"));
                JSONArray cities = object.getJSONArray("cities");

                for(int t=0; t < cities.length(); t++) {
                    country.getCities().add(cities.get(t));
                }
            }else{
                country.setCode(object.getString(ISO3_KEY));
                country.setName(object.getString(COUNTRY_KEY));
            }
            countries.add(country);
        }
        countries.sort(countryByName);
        return countries.stream().collect(Collectors.toSet());
    }


    private void readDataCountry(String response){
        System.out.println("readDataCountry");
        if (StringUtils.isEmpty(response)){
            return;
        }
        JSONObject json = new JSONObject(response);
        boolean error = json.getBoolean(ERROR_KEY);
        String msg = json.getString(MESSAGE_KEY);
        System.out.println(error);
        System.out.println(msg);
        JSONArray data=json.getJSONArray(DATA_KEY);
        for(int i=0; i < data.length(); i++) {
            JSONObject object = data.getJSONObject(i);
            System.out.println(object.getString(COUNTRY_KEY));
            System.out.println(object.getString(ISO3_KEY));
        }
    }

    private void readDataCities(String response){
        System.out.println("readDataCities");
        if (StringUtils.isEmpty(response)){
            return;
        }
        JSONObject json = new JSONObject(response);
        boolean error = json.getBoolean(ERROR_KEY);
        String msg = json.getString(MESSAGE_KEY);
        System.out.println(error);
        System.out.println(msg);
        JSONArray data=json.getJSONArray(DATA_KEY);
        List<String> cities =new ArrayList<>();
        for(int i=0; i < data.length(); i++) {
            System.out.println(data.get(i));
            cities.add((String) data.get(i));
        }
        Collections.sort(cities);
        System.out.println("Sorting");
        cities.stream().forEach(System.out::println);
    }


    private String buildCompleteUrl(String ...parts){
        StringBuilder sbUrl = new StringBuilder();
        Arrays.stream(parts).forEach(sbUrl::append);
        return sbUrl.toString();
    }
    private List<HttpMessageConverter<?>> getJsonMessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new MappingJackson2HttpMessageConverter());
        converters.add(new StringHttpMessageConverter());
        return converters;
    }
    class Country{
        private String code;
        private String name;
        private String currency,flag,dialCode;
        private List cities = new ArrayList();

        public String getCode() { return code;  }

        public void setCode(String code) { this.code = code;   }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCurrency() { return currency; }

        public void setCurrency(String currency) { this.currency = currency; }

        public String getFlag() { return flag; }

        public void setFlag(String flag) { this.flag = flag; }

        public String getDialCode() { return dialCode;}

        public void setDialCode(String dialCode) {  this.dialCode = dialCode;}

        public List getCities() { return cities; }

        public void setCities(List cities) { this.cities = cities;}
    }

    Comparator<Country> countryByCode = new Comparator<Country>() {
        @Override
        public int  compare(Country c1, Country c2) {
            return c1.getCode().compareTo(c2.getCode());
        }
    };

    Comparator<Country> countryByName =
            (Country o1, Country o2)->o1.getName().compareTo(o2.getName());

}
