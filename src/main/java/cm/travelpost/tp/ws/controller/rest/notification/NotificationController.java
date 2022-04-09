package cm.travelpost.tp.ws.controller.rest.notification;

import cm.travelpost.tp.announce.ent.vo.AnnounceVO;
import cm.travelpost.tp.common.ent.vo.WSCommonResponseVO;
import cm.travelpost.tp.common.exception.UserException;
import cm.travelpost.tp.constant.WSConstants;
import cm.travelpost.tp.notification.ent.vo.NotificationVO;
import cm.travelpost.tp.ws.controller.rest.CommonController;
import cm.travelpost.tp.ws.responses.WebServiceResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.text.MessageFormat;

import static cm.travelpost.tp.constant.WSConstants.*;


@RestController
@RequestMapping(NOTIFICATION_WS)
@Api(value = "notifications-service", description = "Reviews Operations")
public class NotificationController extends CommonController {

    protected final Log logger = LogFactory.getLog(NotificationController.class);

    @ApiOperation(value = "Retrieve an notification with an ID", response = AnnounceVO.class)
    @GetMapping(value = NOTIFICATIION_WS_BY_ID, headers = WSConstants.HEADER_ACCEPT)
    public ResponseEntity<Object> read(HttpServletResponse response, HttpServletRequest request, @RequestParam @Valid Long id) throws Exception {

        response.setHeader("Access-Control-Allow-Origin", "*");

        try {
            logger.info("retrieve notification request in");
            createOpentracingSpan("NotificationController - read");
            NotificationVO notification = null;
            if (id != null) {
                notification = notificationService.read(id);
                if (notification == null) {
                    WSCommonResponseVO wsResponse = new WSCommonResponseVO();
                    wsResponse.setRetCode(WebServiceResponseCode.OK_CODE);
                    wsResponse.setRetDescription(MessageFormat.format(WebServiceResponseCode.READ_CODE_LABEL, "La notification"));
                    return new ResponseEntity<>((WSCommonResponseVO) wsResponse, HttpStatus.NOT_FOUND);
                }
            }
            return new ResponseEntity<>(notification, HttpStatus.OK);
        } catch (UserException e) {
            logger.info(" NotificationController -read:Exception occurred while fetching the response from the database.", e);
            throw e;
        } catch (Exception e) {
            logger.info(" NotificationController -read:Exception occurred while fetching the response from the database.", e);
            throw e;

        } finally {
            finishOpentracingSpan();
        }
    }


}