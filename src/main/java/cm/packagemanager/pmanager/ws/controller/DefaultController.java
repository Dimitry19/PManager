package cm.packagemanager.pmanager.ws.controller;

import cm.packagemanager.pmanager.ws.controller.rest.CommonController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


@Controller
public class DefaultController extends CommonController {
	private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);

	/**
	 *  Ceci permet de rediriger la context route sur la page index.html
	 * @param response
	 * @throws IOException
	 */

    @RequestMapping (value = "/", method = RequestMethod.GET)
	public void  ping(HttpServletResponse response) throws Exception {
		System.out.println("/ -> index.html.....");
		int totalUsers=userService.count(null, null,null);

		response.setIntHeader("totalUsers", totalUsers);
		response.sendRedirect(redirectPage);
	}

	@RequestMapping (value = "/index", method = RequestMethod.GET)
	public void error(HttpServletResponse response) throws Exception {
		System.out.println("index -> index.html.....");
		int totalUsers=userService.count(null, null,null);

		response.setIntHeader("totalUsers", totalUsers);
		response.sendRedirect(redirectPage);
	}


	@RequestMapping(value = "/external-redirect", method = RequestMethod.GET)
	public ResponseEntity<Object> method() throws URISyntaxException {

		URI externalUri = new URI("https://some-domain.com/path/to/somewhere");
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(externalUri);

		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}

}