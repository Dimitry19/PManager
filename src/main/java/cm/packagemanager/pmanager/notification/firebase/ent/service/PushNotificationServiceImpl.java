package cm.packagemanager.pmanager.notification.firebase.ent.service;


import cm.packagemanager.pmanager.notification.firebase.ent.vo.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class PushNotificationServiceImpl implements PushNotificationService{

	@Autowired
	private FirebaseCloudMessageService fcmService;


	//@Value("${app.notifications.defaults}")
	private Map<String, String> defaults;

	private Logger logger = LoggerFactory.getLogger(PushNotificationService.class);


	//@Scheduled(initialDelay = 60000, fixedDelay = 60000)
	public void sendSamplePushNotification() {
		try {
			fcmService.sendMessageWithoutData(getSamplePushNotificationRequest());
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage());
		}
	}

	public void sendPushNotification(NotificationRequest request) {
		try {
			fcmService.sendMessage(getSamplePayloadData(), request);
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage());
		}
	}

	public void sendPushNotificationWithoutData(NotificationRequest request) {
		try {
			fcmService.sendMessageWithoutData(request);
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage());
		}
	}


	public void sendPushNotificationToToken(NotificationRequest request) {
		try {
			fcmService.sendMessageToToken(request);
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage());
		}
	}


	private Map<String, String> getSamplePayloadData() {
		Map<String, String> pushData = new HashMap<>();
		pushData.put("messageId", defaults.get("payloadMessageId"));
		pushData.put("text", defaults.get("payloadData") + " " + LocalDateTime.now());
		return pushData;
	}


	private NotificationRequest getSamplePushNotificationRequest() {
		NotificationRequest request = new NotificationRequest(defaults.get("title"),
				defaults.get("message"),
				defaults.get("topic"));
		return request;
	}

}
