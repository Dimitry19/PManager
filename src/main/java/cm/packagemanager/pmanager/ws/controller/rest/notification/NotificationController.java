package cm.packagemanager.pmanager.ws.controller.rest.notification;

import cm.packagemanager.pmanager.notification.firebase.ent.vo.Notification;
import cm.packagemanager.pmanager.notification.firebase.ent.vo.NotificationResponse;
import cm.packagemanager.pmanager.ws.controller.rest.CommonController;
import io.swagger.annotations.Api;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static cm.packagemanager.pmanager.constant.WSConstants.NOTIFICATION_WS;


@RestController
@RequestMapping(NOTIFICATION_WS)
@Api(value = "notifications-service", description = "Reviews Operations")
public class NotificationController extends CommonController {

    protected final Log logger = LogFactory.getLog(NotificationController.class);


    @PostMapping("/notification/topic")
    public ResponseEntity sendNotification(@RequestBody Notification request) {
        pushNotificationService.sendPushNotificationWithoutData(request);
        return new ResponseEntity<>(new NotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }

    @PostMapping("/notification/token")
    public ResponseEntity sendTokenNotification(@RequestBody Notification request) {
        pushNotificationService.sendPushNotificationToToken(request);
        return new ResponseEntity<>(new NotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }

    @PostMapping("/notification/data")
    public ResponseEntity sendDataNotification(@RequestBody Notification request) {
        pushNotificationService.sendPushNotification(request);
        return new ResponseEntity<>(new NotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }

    @GetMapping("/notification")
    public ResponseEntity sendSampleNotification() {
        pushNotificationService.sendSamplePushNotification();
        return new ResponseEntity<>(new NotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }


}
