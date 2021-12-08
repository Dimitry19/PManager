package cm.packagemanager.pmanager.ws.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class DefaultController {
    private static final Logger logger = LoggerFactory.getLogger(DefaultController.class);

    // @GetMapping(value = "/")
    public ResponseEntity<String> ping() {
        logger.info("Démarrage des services OK .....");
        return new ResponseEntity<String>("Réponse du serveur: " + HttpStatus.OK.name(), HttpStatus.OK);
    }

    @RequestMapping("/index")
    public String index() {

        return "/dist/index";
    }

}