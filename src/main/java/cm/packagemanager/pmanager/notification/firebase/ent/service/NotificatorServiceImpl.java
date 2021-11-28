/*
 * Copyright (c) 2021.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.packagemanager.pmanager.notification.firebase.ent.service;

import java.io.IOException;
import java.util.*;

import cm.packagemanager.pmanager.common.event.Event;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.notification.firebase.ent.dao.NotificationDAO;
import cm.packagemanager.pmanager.notification.firebase.ent.vo.Notification;
import cm.packagemanager.pmanager.notification.firebase.ent.vo.NotificationVO;
import cm.packagemanager.pmanager.notification.firebase.enums.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import static cm.packagemanager.pmanager.websocket.constants.WebSocketConstants.*;



@Service("notificator")
@EnableScheduling
public class NotificatorServiceImpl implements NotificationService {



    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    NotificationDAO  notificationDAO;


    final List<Event> events= new ArrayList();


    public void addEvent(Event event) { events.add(event);}


    public void dispatch(String sessionId, NotificationType type) throws Exception{


            SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
            headerAccessor.setSessionId(sessionId);
            headerAccessor.setLeaveMutable(true);

            String queue=null;

            NotificationVO notification=new NotificationVO();

            switch (type){
                case USER:

                    queue=SUSCRIBE_QUEUE_ITEM_SEND;
                    break;
                case ANNOUNCE:
                    queue=SUSCRIBE_QUEUE_ITEM_SEND;
                    break;
            }

        notificationDAO.add(notification);
        messagingTemplate.convertAndSendToUser(sessionId,queue,notification,headerAccessor.getMessageHeaders());

    }

    @Async
    @Scheduled(fixedRate = 5000)
    public void doNotify() throws IOException {
        List<Event> deadEmitters = new ArrayList<>();
        events.forEach(event -> {
            try {

            } catch (Exception e) {
                deadEmitters.add(event);
            }
        });
        events.removeAll(deadEmitters);
    }


    @Override
    public void add(String sessionId, NotificationType notificationType) {

        switch (notificationType){
            case ANNOUNCE:
                announceListeners.put(sessionUserMap.get(sessionId),sessionId);

                break;
            case COMMENT:
                commentListeners.put(sessionUserMap.get(sessionId),sessionId);

                break;

            case USER:

                userListeners.put(sessionUserMap.get(sessionId),sessionId);
                break;
        }
        if (StringUtils.isNotEmpty(sessionId)){
            listeners.add(sessionId);
        }
    }

    @Override
    public void remove(String sessionId) {
        String  userId=(String)sessionUserMap.get(sessionId);

        if(connectedUsers.containsKey(userId)){
            cleanListener(userId);
        }

    }


    @Override
    public void notify(Notification notification, String username) {

    }

    @Override
    public void dispatch() {

    }

    @Override
    public void sendToUser(String sessionId, long id, String email, String username, Notification notification) {

    }


    public void addConnectedUser(String userId, String sessionId){
        connectedUsers.put(userId,sessionId);
        sessionUserMap.put(sessionId,userId);
    }

    public void  removeConnectedUser(String sessionId){
        String userId=(String)sessionUserMap.get(sessionId);
        connectedUsers.remove(userId);
        sessionUserMap.remove(sessionId);
    }

    private void cleanListener(String userId){

        if(commentListeners.containsKey(userId)){
            commentListeners.remove(userId);
        }
        if(announceListeners.containsKey(userId)){
            announceListeners.remove(userId);
        }
        if(userListeners.containsKey(userId)){
            userListeners.remove(userId);
        }

    }
}