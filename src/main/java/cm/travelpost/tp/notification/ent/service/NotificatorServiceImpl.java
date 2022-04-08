/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.notification.ent.service;


import cm.travelpost.tp.common.enums.StatusEnum;
import cm.travelpost.tp.common.event.Event;
import cm.travelpost.tp.common.session.SessionNotificationManager;
import cm.travelpost.tp.common.utils.CollectionsUtils;
import cm.travelpost.tp.common.utils.StringUtils;
import cm.travelpost.tp.notification.ent.dao.NotificationDAO;
import cm.travelpost.tp.notification.ent.vo.Notification;
import cm.travelpost.tp.notification.ent.vo.NotificationVO;
import cm.travelpost.tp.notification.enums.NotificationType;
import cm.travelpost.tp.user.ent.vo.UserVO;
import cm.travelpost.tp.utils.SecRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

import static cm.travelpost.tp.websocket.constants.WebSocketConstants.SUSCRIBE_QUEUE_ITEM_SEND;


@Service("notificator")
@EnableScheduling
public class NotificatorServiceImpl implements NotificationSocketService  {

    private static Logger logger = LoggerFactory.getLogger(NotificatorServiceImpl.class);


    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    NotificationDAO notificationDAO;

    @Autowired
    SessionNotificationManager sessionManager;

    @Value("${travel.post.stomp.notification.enable}")
    private boolean enableNotification;

    private boolean applicationStarted=false;



    final List<Event> events = new ArrayList();
    Map finalMap = new HashMap();


    public void addEvent(Event event) throws Exception{
        events.add(event);
    }

    @PostConstruct
    public void postApplicationStarted() {
        logger.info("Started after Spring boot application !");
        applicationStarted = true;
    }

    @Override
    public void dispatch() throws Exception{

        logger.info(" dispatch !");
        try {


           List<NotificationVO> notifications= notificationDAO.all(NotificationVO.class);

           if(CollectionsUtils.isEmpty(notifications)) return;

            notifications.stream().filter(n->!n.getStatus().equals(StatusEnum.COMPLETED)).forEach(n -> {
                elaborate(n);
            });

        } catch (Exception e) {
            logger.error("Erreur durant l'elaboration de la notification {}", e);
            throw e;
        }

    }




    private void elaborate(NotificationVO notificationVO) {

        logger.info(" Elaborate notification {} ", notificationVO.getId());
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setLeaveMutable(true);

        Notification notification = new Notification(notificationVO.getId(),notificationVO.getTitle(), notificationVO.getMessage(), notificationVO.getType(), notificationVO.getRandom());

        Map map= new HashMap();

           switch (notification.getType()) {
               case RESERVATION:
               case ANNOUNCE:
               case COMMENT:
                   map= announceListeners;
                   break;

               case USER:
                   map= userListeners;
                   break;
            }

            List<String> listeners = (List<String>) map.keySet().stream().collect(Collectors.toList());
            Set<UserVO> subscribers= notificationVO.getUsers();
            Set users = subscribers.stream().map(UserVO::getUsername).collect(Collectors.toSet());

            finalMap=map;
            listeners.stream().filter(l->users.contains(l)).forEach(l->{

                String sessionId = (String) finalMap.get(l);
                headerAccessor.setSessionId(sessionId);

                notification.setTopic(SUSCRIBE_QUEUE_ITEM_SEND);
                List alreadySentForUser= (List)sessionManager.getFromSession(l);

                if(CollectionsUtils.isEmpty(alreadySentForUser) ||
                        (CollectionsUtils.isNotEmpty(alreadySentForUser) && !alreadySentForUser.contains(notification))){
                    messagingTemplate.convertAndSendToUser(sessionId,notification.getTopic(), notification, headerAccessor.getMessageHeaders());
                    alreadySentForUser.add(notification);
                    sessionManager.removeToSession(l);
                    sessionManager.addToSession(l,alreadySentForUser);
                }
            });

        removeNotification(notification);
    }

    @Async
    @Scheduled(fixedRate = 100000,initialDelay = 75000)
    public void doNotify() throws Exception {

        if(!applicationStarted) return;

        logger.info(" doNotify");

        if (!enableNotification) return;

        List<Event> deadEvents = new ArrayList<>();
        events.forEach(event -> {
            try {

                createNotification(event);
                persistNotification();
                deadEvents.add(event);

            } catch (Exception e) {
                deadEvents.add(event);
            }
        });
        dispatch();
        events.removeAll(deadEvents);
    }


    @Override
    public void add(String sessionId, NotificationType notificationType) {

        logger.info(" add {} type {}", sessionId, notificationType);


        switch (notificationType) {
            case RESERVATION:
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
                logger.error(" Erreur durant la sauvegarde de la notification {}",notification.getMessage());
               e.printStackTrace();
            }
        });

    }

    private void updateNotification(NotificationVO n, StatusEnum statusEnum) {

        n.setStatus(statusEnum);

        notificationDAO.update(n);
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
            notification.setRandom(SecRandom.randomLong());

            notifications.add(notification);
    }


    public void addConnectedUser(String userId, String sessionId) {
        connectedUsers.put(userId, sessionId);
        sessionUserMap.put(sessionId, userId);
        sessionManager.addToSession(userId, new ArrayList());
    }

    public String getConnectedUser(String sessionId) {
        return (String) sessionUserMap.get(sessionId);

    }

    public void removeConnectedUser(String sessionId) {
        String userId = (String) sessionUserMap.get(sessionId);
        sessionManager.removeToSession(userId);

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

    private void removeNotification(Notification notification){

        if(CollectionsUtils.isNotEmpty(notifications)){

            notifications.removeIf(n->((NotificationVO) n).getRandom().equals(notification.getRandom()));

        }

    }

}