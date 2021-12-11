/*
 * Copyright (c) 2021.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.packagemanager.pmanager.notification.firebase.ent.service;

import cm.packagemanager.pmanager.announce.event.AnnounceEvent;
import cm.packagemanager.pmanager.common.enums.StatusEnum;
import cm.packagemanager.pmanager.common.event.Event;
import cm.packagemanager.pmanager.notification.firebase.ent.dao.NotificationDAO;
import cm.packagemanager.pmanager.notification.firebase.ent.vo.Notification;
import cm.packagemanager.pmanager.notification.firebase.ent.vo.NotificationVO;
import cm.packagemanager.pmanager.notification.firebase.enums.NotificationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static cm.packagemanager.pmanager.websocket.constants.WebSocketConstants.*;


@Service("notificator")
@EnableScheduling
public class NotificatorServiceImpl implements NotificationService {

    private static Logger logger = LoggerFactory.getLogger(NotificatorServiceImpl.class);


    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    NotificationDAO notificationDAO;


    final List<Event> events = new ArrayList();


    public void addEvent(Event event) {
        events.add(event);
    }


    @Override
    public void dispatch() {

        logger.info(" dispatch !");

        notifications.forEach(n -> {

            try {
                elaborate((NotificationVO) n);
            } catch (Exception e) {
                logger.error("Erreur durant l'elaboration de la notification {} {}", n, e);
            }

        });

        persistNotification();
    }


    private void elaborate(NotificationVO notification) throws Exception {

        logger.info(" elaborate  !");
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setLeaveMutable(true);
        String queue = null;


        if (connectedUsers.get(notification.getUserId()) == null) {
            notification.setStatus(StatusEnum.TO_DELIVERED);
            notificationsToPersist.add(notification);
            return;
        }

        if (notification.getStatus().equals(StatusEnum.VALID)) {
            switch (notification.getType()) {
                case ANNOUNCE:

                    announceListeners.forEach((k, v) -> {

                        String sessionId = (String) v;
                        headerAccessor.setSessionId(sessionId);

                        logger.info(" notification  {}", notification.getMessage());
                        logger.info(" elaborate {} queue {}", sessionId, SUSCRIBE_QUEUE_ANNOUNCE_SEND);
                        messagingTemplate.convertAndSendToUser(sessionId, SUSCRIBE_QUEUE_ANNOUNCE_SEND, notification, headerAccessor.getMessageHeaders());

                    });

                    break;
                case COMMENT:
                    queue = SUSCRIBE_QUEUE_COMMENT_SEND;
                    //commentListeners.put(sessionUserMap.get(sessionId),sessionId);
                    break;

                case USER:
                    queue = SUSCRIBE_QUEUE_USER_SEND;
                    //userListeners.put(sessionUserMap.get(sessionId),sessionId);
                    break;
            }

        }
        notifications.remove(notification);
    }

    @Async
    @Scheduled(fixedRate = 5000)
    public void doNotify() throws IOException {
        logger.info(" doNotify");

        List<Event> deadEvents = new ArrayList<>();
        events.forEach(event -> {
            try {

                createNotification(event);
                dispatch();

            } catch (Exception e) {
                deadEvents.add(event);
            }
        });
        events.removeAll(deadEvents);
    }


    @Override
    public void add(String sessionId, NotificationType notificationType) {

        logger.info(" add {} type {}", sessionId, notificationType);


        switch (notificationType) {
            case ANNOUNCE:
                announceListeners.put(sessionUserMap.get(sessionId), sessionId);
                break;
            case COMMENT:
                commentListeners.put(sessionUserMap.get(sessionId), sessionId);
                break;

            case USER:
                userListeners.put(sessionUserMap.get(sessionId), sessionId);
                break;
        }
        /*if (StringUtils.isNotEmpty(sessionId)){
            listeners.add(sessionId);
        }*/
    }

    @Override
    public void remove(String sessionId) {
        String userId = (String) sessionUserMap.get(sessionId);

        if (connectedUsers.containsKey(userId)) {
            cleanListener(userId);
        }

    }

    private void persistNotification() {
        notificationsToPersist.forEach(n -> {
            try {
                notificationDAO.persist((NotificationVO) n);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    protected void createNotification(Event event) {

        NotificationVO notification = new NotificationVO();

        if (event instanceof AnnounceEvent) {
            notification.setTitle("Announce Notification");

            AnnounceEvent evt = (AnnounceEvent) event;
            notification.setAnnounceId(evt.getId());
            notification.setMessage(evt.getMessage());
        }


        if (notification != null) {
            notification.setUserId(event.getUserId());
            notification.setStatus(StatusEnum.VALID);
            notification.setType(event.getType());
        }
        notifications.add(notification);
    }

    @Override
    public void notify(Notification notification, String username) {

    }


    @Override
    public void sendToUser(String sessionId, long id, String email, String username, Notification notification) {

    }


    public void addConnectedUser(String userId, String sessionId) {
        connectedUsers.put(userId, sessionId);
        sessionUserMap.put(sessionId, userId);
    }

    public void removeConnectedUser(String sessionId) {
        String userId = (String) sessionUserMap.get(sessionId);
        connectedUsers.remove(userId);
        sessionUserMap.remove(sessionId);
    }

    private void cleanListener(String userId) {

        commentListeners.remove(userId);
        announceListeners.remove(userId);
        userListeners.remove(userId);

    }
}