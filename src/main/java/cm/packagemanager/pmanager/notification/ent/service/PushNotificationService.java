/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.packagemanager.pmanager.notification.ent.service;

import cm.packagemanager.pmanager.notification.ent.vo.Notification;

public interface PushNotificationService {
    void sendSamplePushNotification();

    void sendPushNotification(Notification request);

    void sendPushNotificationToToken(Notification request);

    void sendPushNotificationWithoutData(Notification request);

}
