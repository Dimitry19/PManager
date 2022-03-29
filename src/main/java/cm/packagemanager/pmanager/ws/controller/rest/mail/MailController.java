package cm.packagemanager.pmanager.ws.controller.rest.mail;

import cm.packagemanager.pmanager.common.mail.ent.vo.ContactUSVO;
import cm.packagemanager.pmanager.ws.controller.rest.CommonController;
import cm.packagemanager.pmanager.ws.requests.mail.ContactUSDTO;
import cm.packagemanager.pmanager.ws.responses.Response;
import cm.packagemanager.pmanager.ws.responses.WebServiceResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static cm.packagemanager.pmanager.constant.WSConstants.CONTACT_US_MAIL_WS;
import static cm.packagemanager.pmanager.constant.WSConstants.MAIL_WS;

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
    public Response contactUS(HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid ContactUSDTO contactusDTO) {
        logger.info("contact us - request");
        response.setHeader("Access-Control-Allow-Origin", "*");

        Response pmResponse = new Response();
        try {
            createOpentracingSpan("MailController - contact us");

            //com.sendgrid.Response sent =

            if(mailService.contactUS(contactusDTO)){
                pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                pmResponse.setRetDescription(WebServiceResponseCode.CONTACT_US_LABEL);
                response.setStatus(200);

                return pmResponse;
            }


         /*   if (mailSenderSendGrid.manageResponse(sent)) {

                pmResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                pmResponse.setRetDescription(WebServiceResponseCode.CONTACT_US_LABEL);
                response.setStatus(200);

                return pmResponse;
            } else {
                pmResponse.setRetCode(sent.getStatusCode());
                pmResponse.setRetDescription(sent.getBody());
                response.setStatus(sent.getStatusCode());
            }*/
        } catch (Exception e) {
                new Exception("Une erreur est survenue durant l'envoi du mail , Veuillez reessayer plutard");
        } finally {
            finishOpentracingSpan();
        }
        return null;
    }
}
