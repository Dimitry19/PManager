package cm.packagemanager.pmanager.ws.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;


@Controller
public class DefaultController {
	private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);

   @RequestMapping (value = "/", method = RequestMethod.GET)
	public ResponseEntity<String> ping(HttpServletResponse response) {
		logger.info("Démarrage des services OK .....");
		response.setContentType("text/html");
		return new ResponseEntity<String>("Réponse du serveur: "+HttpStatus.OK.name(), HttpStatus.OK);
	   //return "public/index";
	}


}