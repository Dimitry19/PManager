package cm.travelpost.tp.ws.controller;


import cm.travelpost.tp.ws.controller.rest.CommonController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;


@Controller
public class DefaultController extends CommonController {
	private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);

	/**
	 *  Ceci permet de rediriger la context route sur la page index.html
	 * @param response
	 * @throws IOException
	 */



    @GetMapping(value = "/")
	public void  ping(HttpServletResponse response) throws Exception {
		logger.info("/ -> index.html.....");
		int totalUsers=userService.count(null, null,null);

		//String countries = client.countries();
		//String countries = client.phoneEmailValidation("3887576185","it");
		response.setIntHeader("totalUsers", totalUsers);
		response.sendRedirect(getRedirectPage(RedirectType.INDEX));

	}

	@GetMapping(value = "/index")
	public void  index(HttpServletResponse response) throws Exception {
	 	int totalUsers=userService.count(null, null,null);
		//String countries = client.phoneEmailValidation("3887576185","it");
		//String email = client.phoneEmailValidation("dimipasc@yahoo.fr",null);

		String[] countryCodes = Locale.getISOCountries();

		for (String countryCode : countryCodes) {

			Locale obj = new Locale("fr-FR", countryCode);

			//System.out.println("Country Code = " + obj.getCountry() + ", Country Name = " + obj.getDisplayCountry());

		}
		//client.countriesAndCities();
		client.citiesFromCountry("Cameroon");
		response.setIntHeader("totalUsers", totalUsers);
		response.sendRedirect(getRedirectPage(RedirectType.INDEX));
	}
}