package cm.packagemanager.pmanager.notification.firebase.ent.service;

import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.notification.firebase.ent.vo.Notification;
import cm.packagemanager.pmanager.notification.firebase.enums.NotificationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import static cm.packagemanager.pmanager.websocket.constants.WebSocketConstants.NOTIFY_SEND;
import static cm.packagemanager.pmanager.websocket.constants.WebSocketConstants.SUSCRIBE_QUEUE_ITEM_SEND;

@Service("notificationService")
public class NotificationServiceImpl implements NotificationService {

    private static Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    // The SimpMessagingTemplate is used to send Stomp over WebSocket messages.
    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    /**
     * Send notification to users subscribed on channel "/user/queue/notify".
     * <p>
     * The message will be sent only to the user with the given username.
     *
     * @param notification The notification message.
     * @param username     The username for the user to send notification.
     */
    @Override //Test2
    public void notify(Notification notification, String username) {
        messagingTemplate.convertAndSendToUser(
                username,
                NOTIFY_SEND,
                notification
        );
        return;
    }

    @Scheduled(fixedDelay = 2000)
    @Override
    public void dispatch() {
        for (String listener : listeners) {

            //TODO passer l'id de l'utilisateur et celui de l'annonce
            logger.info("Sending notification to " + listener);

            SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
            headerAccessor.setSessionId(listener);
            headerAccessor.setLeaveMutable(true);

            Notification notification = new Notification("title", "message", "topic", "url");
            messagingTemplate.convertAndSendToUser(listener,
                    SUSCRIBE_QUEUE_ITEM_SEND,
                    notification,
                    headerAccessor.getMessageHeaders());
        }
    }


    @Override
    public void sendToUser(String sessionId, long id, String email, String username, Notification notification) {
        String listener = null;
        for (String listenerTo : listeners) {
            if (StringUtils.equals(sessionId, listener)) {
                logger.info("Sending notification to " + listener);
                listener = listenerTo;
            }
        }
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(listener);
        headerAccessor.setLeaveMutable(true);

        int value = (int) Math.round(Math.random() * 100d);
        messagingTemplate.convertAndSendToUser(
                listener,
                SUSCRIBE_QUEUE_ITEM_SEND,
                notification,
                headerAccessor.getMessageHeaders());
    }

    @Override
    public void add(String sessionId, NotificationType notificationType) {

        switch (notificationType) {
            case ANNOUNCE:
                //announceListeners.put("",sessionId);
                break;
            case USER:
                break;
        }
        if (StringUtils.isNotEmpty(sessionId)) {
            listeners.add(sessionId);
        }
    }


    @Override
    public void remove(String sessionId) {
        listeners.remove(sessionId);
    }

    @EventListener
    public void sessionDisconnectionHandler(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        logger.info("Disconnecting " + sessionId + "!");
        remove(sessionId);
    }
}
