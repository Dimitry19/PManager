/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.packagemanager.pmanager.notification.ent.service;

import cm.packagemanager.pmanager.notification.ent.vo.Notification;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface FirebaseCloudMessageService {

    void sendMessageWithoutData(Notification request)
            throws InterruptedException, ExecutionException;

    void sendMessage(Map<String, String> data, Notification request)
            throws InterruptedException, ExecutionException;

    void sendMessageToToken(Notification request) throws InterruptedException, ExecutionException;
}
