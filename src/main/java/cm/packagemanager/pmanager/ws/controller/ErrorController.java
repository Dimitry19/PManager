package cm.packagemanager.pmanager.ws.controller;


import cm.packagemanager.pmanager.ws.controller.rest.CommonController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ErrorController extends CommonController {

    @RequestMapping (value = "/errors", method = RequestMethod.GET)
    public void handle( HttpServletResponse response) throws IOException {

        response.sendRedirect(contextRoot+redirectPage);
    }

}