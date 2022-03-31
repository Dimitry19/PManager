package cm.packagemanager.pmanager.ws.controller;

import cm.packagemanager.pmanager.ws.controller.rest.CommonController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
public class DefaultController extends CommonController {
	private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);



   @RequestMapping (value = "/", method = RequestMethod.GET)
	public void ping(HttpServletResponse response) throws IOException {
		System.out.println("Démarrage des services OK .....");

	    response.sendRedirect(redirectPage);
	}


}