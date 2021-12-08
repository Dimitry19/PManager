package cm.packagemanager.pmanager.notification.firebase.ent.service;

import cm.packagemanager.pmanager.notification.firebase.ent.vo.Notification;

public interface PushNotificationService {
    void sendSamplePushNotification();

    void sendPushNotification(Notification request);

    void sendPushNotificationToToken(Notification request);

    void sendPushNotificationWithoutData(Notification request);

}
