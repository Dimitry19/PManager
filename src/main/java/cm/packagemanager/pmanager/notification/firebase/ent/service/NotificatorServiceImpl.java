/*
 * Copyright (c) 2021.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.packagemanager.pmanager.notification.firebase.ent.service;

import cm.packagemanager.pmanager.announce.event.AnnounceEvent;
import cm.packagemanager.pmanager.common.enums.StatusEnum;
import cm.packagemanager.pmanager.common.event.Event;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.constant.FieldConstants;
import cm.packagemanager.pmanager.notification.firebase.ent.dao.NotificationDAO;
import cm.packagemanager.pmanager.notification.firebase.ent.vo.Notification;
import cm.packagemanager.pmanager.notification.firebase.ent.vo.NotificationVO;
import cm.packagemanager.pmanager.notification.firebase.enums.NotificationType;
import cm.packagemanager.pmanager.user.ent.dao.UserDAO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
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
import java.util.*;
import java.util.stream.Collectors;

import static cm.packagemanager.pmanager.websocket.constants.WebSocketConstants.*;


@Service("notificator")
@EnableScheduling
public class NotificatorServiceImpl implements NotificationSocketService {

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
        try {


            List<NotificationVO> notifications=notificationDAO.all(NotificationVO.class);

            notifications.stream().filter(n->!n.getStatus().equals(StatusEnum.COMPLETED)).forEach(n -> {

                try {

                    elaborate(n);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            });

        } catch (Exception e) {
            logger.error("Erreur durant l'elaboration de la notification {}", e);
        }

    }


    private void elaborate(NotificationVO notification) throws Exception {

        logger.info(" elaborate  !");
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setLeaveMutable(true);

        if (notification.getStatus().equals(StatusEnum.VALID) ||  notification.getStatus().equals(StatusEnum.TO_DELIV)) {

            Notification notif = new Notification(notification.getId(),notification.getTitle(), notification.getMessage(), null, null);

            Map map= new HashMap();

            switch (notification.getType()) {
                case ANNOUNCE:
                case COMMENT:
                    map= announceListeners;
                    break;

                case USER:
                    map= userListeners;
                    break;

            }

            List<String> listeners = (List<String>) map.keySet().stream().collect(Collectors.toList());


            Set<UserVO> subscribers= notification.getUsers();
            Set users = subscribers.stream().map(UserVO::getUsername).collect(Collectors.toSet());
            Map finalMap = map;
            listeners.stream().filter(l->users.contains(l)).forEach(l->{

                String sessionId = (String) finalMap.get(l);
                headerAccessor.setSessionId(sessionId);

                notif.setTopic(SUSCRIBE_QUEUE_ITEM_SEND);
                logger.info(" notification  {}", notif.getMessage());
                logger.info(" elaborate {} queue {}", sessionId, notif.getTopic());
                messagingTemplate.convertAndSendToUser(sessionId,notif.getTopic(), notif, headerAccessor.getMessageHeaders());

            });
        }
        notifications.remove(notification);
    }

    @Async
    @Scheduled(fixedRate = 50000)
    public void doNotify() throws IOException {
        logger.info(" doNotify");

        List<Event> deadEvents = new ArrayList<>();
        events.forEach(event -> {
            try {

                createNotification(event);
                persistNotification();
                dispatch();
                deadEvents.add(event);

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
            case COMMENT:
                announceListeners.put(sessionUserMap.get(sessionId), sessionId);
                break;

            case USER:
                userListeners.put(sessionUserMap.get(sessionId), sessionId);
                break;
        }
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
        notifications.forEach(n -> {
            NotificationVO notification =(NotificationVO)n;
            notification.getUsers().stream().forEach(u->{

                u.addNotification(notification);
                notification.getUsers().add(u);

            });

            try {
                notificationDAO.save(notification);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    protected void createNotification(Event event) {

            NotificationVO notification = new NotificationVO();

            notification.setTitle("Notification");
            notification.setAnnounceId(event.getId());
            notification.setUserId(event.getUserId());
            notification.setMessage(event.getMessage());
            notification.setUsername(event.getUsername());

            notification.getUsers().addAll(event.getUsers());
            notification.setStatus(StatusEnum.VALID);
            notification.setType(event.getType());

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

}