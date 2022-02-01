package cm.packagemanager.pmanager.ws.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static cm.packagemanager.pmanager.constant.WSConstants.USER_WS;

@RestController
@RequestMapping("/")
public class ErrorController {

    @Value("${redirect.page}")
    private String redirectPage;

    @RequestMapping(value = "/error", produces = "application/json")
    @ResponseBody
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.sendRedirect(redirectPage);
    }

    //@Override

//    public String getErrorPath() {
//        return "/";
//    }
}