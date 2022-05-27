package cm.travelpost.tp.ws.controller;


import cm.travelpost.tp.ws.controller.rest.CommonController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//@Controller
public class CustomErrorController extends CommonController  { //implements ErrorController {

   // @Override
    @RequestMapping(PATH)
    @ResponseBody
    public String getErrorPath() {
        // TODO Auto-generated method stub
        return "404";
    }

}