package cm.packagemanager.pmanager.notification.firebase.ent.service;

import cm.packagemanager.pmanager.notification.firebase.ent.vo.NotificationRequest;

public interface PushNotificationService {
	void sendSamplePushNotification();

	void sendPushNotification(NotificationRequest request);

	void sendPushNotificationToToken(NotificationRequest request);

	void sendPushNotificationWithoutData(NotificationRequest request);
}
