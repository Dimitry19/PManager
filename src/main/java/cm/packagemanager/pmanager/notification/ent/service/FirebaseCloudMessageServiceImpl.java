/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.packagemanager.pmanager.notification.ent.service;

import cm.packagemanager.pmanager.notification.ent.vo.Notification;
import cm.packagemanager.pmanager.notification.enums.NotificationParameter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseCloudMessageServiceImpl implements FirebaseCloudMessageService {

    @Value("${app.firebase-configuration-file}")
    private String firebaseConfigPath;

    Logger logger = LoggerFactory.getLogger(FirebaseCloudMessageServiceImpl.class);

    @PostConstruct
    public void initialize() {
        try {

            final InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(firebaseConfigPath);

            if (resourceAsStream==null){
                logger.info(" Firebase config file not found!");
                return;
            }
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(resourceAsStream)).build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                logger.info("Firebase application has been initialized");
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void sendMessageWithoutData(cm.packagemanager.pmanager.notification.ent.vo.Notification request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageWithoutData(request);
        String response = sendAndGetResponse(message);
        logger.info("Sent message without data. Topic: " + request.getTopic() + ", " + response);
    }

    @Override
    public void sendMessage(Map<String, String> data, cm.packagemanager.pmanager.notification.ent.vo.Notification request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageWithData(data, request);
        String response = sendAndGetResponse(message);
        logger.info("Sent message with data. Topic: " + request.getTopic() + ", " + response);
    }

    @Override
    public void sendMessageToToken(cm.packagemanager.pmanager.notification.ent.vo.Notification request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageToToken(request);
        String response = sendAndGetResponse(message);
        logger.info("Sent message to token. Device token: " + request.getToken() + ", " + response);
    }


    private Message getPreconfiguredMessageWithData(Map<String, String> data, cm.packagemanager.pmanager.notification.ent.vo.Notification request) {
        return getPreconfiguredMessageBuilder(request).putAllData(data).setTopic(request.getTopic())
                .build();
    }

    private Message getPreconfiguredMessageToToken(cm.packagemanager.pmanager.notification.ent.vo.Notification request) {
        return getPreconfiguredMessageBuilder(request).setToken(request.getToken())
                .build();
    }

    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    private AndroidConfig getAndroidConfig(String topic) {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder().setSound(NotificationParameter.SOUND.getValue())
                        .setColor(NotificationParameter.COLOR.getValue()).setTag(topic).build()).build();
    }

    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig.builder()
                .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
    }

    private Message getPreconfiguredMessageWithoutData(cm.packagemanager.pmanager.notification.ent.vo.Notification request) {
        return getPreconfiguredMessageBuilder(request).setTopic(request.getTopic())
                .build();
    }


    private Message.Builder getPreconfiguredMessageBuilder(Notification request) {
        AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
        ApnsConfig apnsConfig = getApnsConfig(request.getTopic());

        return Message.builder()
                .setApnsConfig(apnsConfig)
                .setAndroidConfig(androidConfig);
				/*.setNotification(
						new Notification(request.getTitle(), request.getMessage()));*/
    }
}
