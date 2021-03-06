package cm.travelpost.tp.ws.controller.rest.mail;

import cm.travelpost.tp.ws.controller.rest.CommonController;
import cm.travelpost.tp.ws.requests.mail.ContactUSDTO;
import cm.travelpost.tp.ws.responses.Response;
import cm.travelpost.tp.ws.responses.WebServiceResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.text.MessageFormat;

import static cm.travelpost.tp.constant.WSConstants.MAIL_WS;

@RestController
@RequestMapping(MAIL_WS)
@Api(value = "mail-service", description = "Mail Operations")
public class MailController extends CommonController {

    protected final Log logger = LogFactory.getLog(MailController.class);


    @ApiOperation(value = "Contact us ", response = Response.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
            @ApiResponse(code = 200, message = "Mail envoyé correctement",
                    response = Response.class, responseContainer = "Object")})
    @PostMapping(CONTACT_US_MAIL_WS)
    public ResponseEntity<Response> contactUS(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid ContactUSDTO contactusDTO) {

        logger.info("contact us - request");
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_ORIGIN_VALUE);


        Response pmResponse = new Response();
        try {
            createOpentracingSpan("MailController - contact us");


            if(mailService.contactUS(contactusDTO)){
                pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                pmResponse.setRetDescription(WebServiceResponseCode.CONTACT_US_LABEL);
                response.setStatus(200);
                return new ResponseEntity<>(pmResponse, HttpStatus.OK);

            }else{
                pmResponse.setRetCode(WebServiceResponseCode.NOK_CODE);
                pmResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.ERROR_MAIL_SERVICE_UNAVAILABLE_LABEL,"Veuillez reessayez plutard , Merci!"));
                response.setStatus(503);
                return new ResponseEntity<>(pmResponse, HttpStatus.SERVICE_UNAVAILABLE);
            }

        } catch (Exception e) {
            return getResponseMailResponseEntity(pmResponse, e,"Veuillez reessayez plutard , Merci!");
        } finally {
            finishOpentracingSpan();
        }
    }
}
