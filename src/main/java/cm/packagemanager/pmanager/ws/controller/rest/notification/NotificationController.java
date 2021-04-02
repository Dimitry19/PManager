package cm.packagemanager.pmanager.ws.controller.rest.notification;

import cm.packagemanager.pmanager.notification.firebase.ent.service.NotificationService;
import cm.packagemanager.pmanager.notification.firebase.ent.service.PushNotificationService;
import cm.packagemanager.pmanager.notification.firebase.ent.vo.NotificationRequest;
import cm.packagemanager.pmanager.notification.firebase.ent.vo.NotificationResponse;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import static cm.packagemanager.pmanager.constant.WSConstants.*;



@RestController
@RequestMapping(NOTIFICATION_WS)
@Api(value="notifications-service", description="Reviews Operations")
public class  NotificationController {

	@Autowired
	private PushNotificationService pushNotificationService;

	@Autowired
	private NotificationService notificationService;


	@PostMapping("/notification/topic")
	public ResponseEntity sendNotification(@RequestBody NotificationRequest request) {
		pushNotificationService.sendPushNotificationWithoutData(request);
		return new ResponseEntity<>(new NotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
	}

	@PostMapping("/notification/token")
	public ResponseEntity sendTokenNotification(@RequestBody NotificationRequest request) {
		pushNotificationService.sendPushNotificationToToken(request);
		return new ResponseEntity<>(new NotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
	}

	@PostMapping("/notification/data")
	public ResponseEntity sendDataNotification(@RequestBody NotificationRequest request) {
		pushNotificationService.sendPushNotification(request);
		return new ResponseEntity<>(new NotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
	}

	@GetMapping("/notification")
	public ResponseEntity sendSampleNotification() {
		pushNotificationService.sendSamplePushNotification();
		return new ResponseEntity<>(new NotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
	}

	@MessageMapping("/start")
	public void start(StompHeaderAccessor stompHeaderAccessor) {
		notificationService.add(stompHeaderAccessor.getSessionId());
	}
	@MessageMapping("/stop")
	public void stop(StompHeaderAccessor stompHeaderAccessor) {
		notificationService.remove(stompHeaderAccessor.getSessionId());
	}
}
