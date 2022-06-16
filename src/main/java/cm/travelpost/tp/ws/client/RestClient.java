package cm.travelpost.tp.ws.client;

import cm.framework.ds.common.security.CommonSecurityResource;
import cm.travelpost.tp.common.utils.HTMLEntities;
import cm.travelpost.tp.common.utils.StringUtils;
import cm.travelpost.tp.common.utils.Utility;
import cm.travelpost.tp.ws.responses.Response;
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
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
@Scope("singleton")
public class RestClient  extends CommonSecurityResource {

    RestTemplate restTemplate;

    private HttpHeaders headers;

    private static final String KEY="key=";
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

    private String decryptedApiKey;


    @PostConstruct
    public void init() {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        this.headers.setContentType(MediaType.APPLICATION_JSON);
        this.headers.add("Accept", "application/json");
        this.decryptedApiKey= encryptorBean.decrypt(bigDataCloudApiKey);
    }

    public String countries() {
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
        }
        return null;
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
            String errorMessage = String.format("countries Error: %s", Utility.getStackTrace(e));
            logger.error(errorMessage);
            response.setRetCode(-1);
        }
        return null;
    }


    public String countriesAndCities(){
        String response=new String();
        try {
            logger.info("Start countriesAndCities (...)");
            response = restTemplate.getForObject(countriesCitiesUrl,  String.class);

           readDataCountry(response);

        }
        catch(Exception e) {
            String errorMessage = String.format("countriesAndCities Error: %s", Utility.getStackTrace(e));
            logger.error(errorMessage);
        }
        return response;
    }

    public String citiesFromCountry(String country){
        String  response=new String();
        try {
            logger.info("Start citiesFromCountry (...)");
            String url =buildCompleteUrl(citiesCountryUrl,country);
            response = restTemplate.getForObject(url,String.class);
            readDataCities(response);

        }
        catch(Exception e) {
            String errorMessage = String.format("citiesFromCountry Error: %s", Utility.getStackTrace(e));
            logger.error(errorMessage);

        }
        return response;
    }


    private void readDataCountry(String response){
        System.out.println("readDataCountry");
        if (StringUtils.isEmpty(response)){
            return;
        }
        JSONObject json = new JSONObject(response);
        boolean error = json.getBoolean("error");
        String msg = json.getString("msg");
        System.out.println(error);
        System.out.println(msg);
        JSONArray data=json.getJSONArray("data");
        for(int i=0; i < data.length(); i++) {
            JSONObject object = data.getJSONObject(i);
            System.out.println(object.getString("country"));
            System.out.println(object.getString("iso3"));
        }
    }

    private void readDataCities(String response){
        System.out.println("readDataCities");
        if (StringUtils.isEmpty(response)){
            return;
        }
        JSONObject json = new JSONObject(response);
        boolean error = json.getBoolean("error");
        String msg = json.getString("msg");
        System.out.println(error);
        System.out.println(msg);
        JSONArray data=json.getJSONArray("data");
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
        private String country;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

}
