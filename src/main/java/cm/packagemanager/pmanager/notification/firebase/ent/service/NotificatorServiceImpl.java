/*
 * Copyright (c) 2021.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.packagemanager.pmanager.notification.firebase.ent.service;

import cm.packagemanager.pmanager.announce.event.AnnounceEvent;
import cm.packagemanager.pmanager.common.enums.StatusEnum;
import cm.packagemanager.pmanager.common.event.Event;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.notification.firebase.ent.dao.NotificationDAO;
import cm.packagemanager.pmanager.notification.firebase.ent.vo.Notification;
import cm.packagemanager.pmanager.notification.firebase.ent.vo.NotificationVO;
import cm.packagemanager.pmanager.notification.firebase.enums.NotificationType;
import cm.packagemanager.pmanager.user.event.UserEvent;
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

        //notifications.clear();
        //persistNotification();
    }


    private void elaborate(NotificationVO notification) throws Exception {

        logger.info(" elaborate  !");
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setLeaveMutable(true);


        if (connectedUsers.get(notification.getUsername()) == null) {
            notification.setStatus(StatusEnum.TO_DELIV);
            notification.setSessionId((String) sessionUserMap.get(notification.getUsername()));
            notificationsToPersist.add(notification);
            return;
        }

        if (notification.getStatus().equals(StatusEnum.VALID)) {

            Notification notif = new Notification(notification.getTitle(), notification.getMessage(), null, null);


            switch (notification.getType()) {
                case ANNOUNCE:

                    announceListeners.forEach((k, v) -> {

                        String sessionId = (String) v;
                        headerAccessor.setSessionId(sessionId);

                        notif.setTopic(SUSCRIBE_QUEUE_ITEM_SEND);
                        logger.info(" notification  {}", notif.getMessage());
                        logger.info(" elaborate {} queue {}", sessionId, notif.getTopic());
                        messagingTemplate.convertAndSendToUser(sessionId,notif.getTopic(), notif, headerAccessor.getMessageHeaders());

                    });

                    break;
                case COMMENT:

                    commentListeners.forEach((k, v) -> {

                        String sessionId = (String) v;
                        headerAccessor.setSessionId(sessionId);

                        notif.setTopic(SUSCRIBE_QUEUE_ITEM_SEND);
                        logger.info(" notification  {}", notif.getMessage());
                        logger.info(" elaborate {} queue {}", sessionId, notif.getTopic());
                        messagingTemplate.convertAndSendToUser(sessionId,
                                 notif.getTopic(), notif, headerAccessor.getMessageHeaders());

                    });
                    break;

                case USER:
                    userListeners.forEach((k, v) -> {

                        String sessionId = (String) v;
                        headerAccessor.setSessionId(sessionId);

                        notif.setTopic(SUSCRIBE_QUEUE_ITEM_SEND);

                        logger.info(" notification  {}", notification.getMessage());
                        logger.info(" elaborate {} queue {}", sessionId, notif.getTopic());
                        messagingTemplate.convertAndSendToUser( sessionId,notif.getTopic(), notif, headerAccessor.getMessageHeaders());

                    });

                    break;
            }

        }
        //notifications.remove(notification);
    }

    @Async
    @Scheduled(fixedRate = 5000)
    public void doNotify() throws IOException {
        logger.info(" doNotify");

        notifyUser();
        List<Event> deadEvents = new ArrayList<>();
        events.forEach(event -> {
            try {

                createNotification(event);
                dispatch();
               // deadEvents.add(event);

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
                //userListeners.put(sessionUserMap.get(sessionId), sessionId);
                userListeners.put(sessionId, sessionId);
                break;
        }
        /*if (StringUtils.isNotEmpty(sessionId)){
            listeners.add(sessionId);
        }*/
    }

    @Override
    public void addAll(String sessionId) {
        announceListeners.put(sessionUserMap.get(sessionId), sessionId);
        commentListeners.put(sessionUserMap.get(sessionId), sessionId);
        userListeners.put(sessionUserMap.get(sessionId), sessionId);

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
                notificationDAO.save(n);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        notificationsToPersist.clear();
    }

    protected void createNotification(Event event) {

        NotificationVO notification = new NotificationVO();

        if (event instanceof AnnounceEvent) {
            notification.setTitle("Announce Notification");

            AnnounceEvent evt = (AnnounceEvent) event;
            notification.setAnnounceId(evt.getId());
            notification.setUserId(event.getUserId());
            notification.setMessage(evt.getMessage());
            notification.setUsername(evt.getUsername());

        }

        if (event instanceof UserEvent) {
            notification.setTitle("User Notification");

            UserEvent evt = (UserEvent) event;
            notification.setUserId(evt.getId());
            notification.setRuserId(event.getUserId());
            notification.setMessage(evt.getMessage());
            notification.setUsername(evt.getUsername());

        }

        if (notification != null) {
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

        if(StringUtils.isEmpty(userId)) return;

        if (connectedUsers.containsKey(userId)) {
            connectedUsers.remove(userId);
        }
        sessionUserMap.remove(sessionId);
    }

    private void cleanListener(String userId) {

        commentListeners.remove(userId);
        announceListeners.remove(userId);
        userListeners.remove(userId);

    }


    void notifyUser(){
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setLeaveMutable(true);
        userListeners.forEach((k, v) -> {


            String sessionId = (String) v;
            headerAccessor.setSessionId(sessionId);

            Notification notif = new Notification("Title", "message", SUSCRIBE_QUEUE_ITEM_SEND, null);

            notif.setTopic(SUSCRIBE_QUEUE_ITEM_SEND);
            logger.info(" notification  {}", notif.getMessage());
            logger.info(" elaborate {} queue {}", sessionId, notif.getTopic());
            messagingTemplate.convertAndSendToUser(sessionId,notif.getTopic(), notif, headerAccessor.getMessageHeaders());

        });

    }
}