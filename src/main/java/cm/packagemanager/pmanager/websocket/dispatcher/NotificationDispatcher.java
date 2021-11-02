package cm.packagemanager.pmanager.websocket.dispatcher;


import cm.packagemanager.pmanager.notification.firebase.ent.vo.Notification;
import cm.packagemanager.pmanager.websocket.constants.WebSocketConstants;
import cm.packagemanager.pmanager.ws.controller.rest.notification.NotificationController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.HashSet;
import java.util.Set;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


//@Service
public class NotificationDispatcher {


    protected final Log logger = LogFactory.getLog(NotificationController.class);

    private final SimpMessagingTemplate template;

    private Set<String> listeners = new HashSet<>();

    public NotificationDispatcher(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void add(String sessionId) {
        listeners.add(sessionId);
    }

    public void remove(String sessionId) {
        listeners.remove(sessionId);
    }



    @Scheduled(fixedDelay = 2000)
    public void dispatch() {
        for (String listener : listeners) {
            logger.info("Sending notification to " + listener);

            SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
            headerAccessor.setSessionId(listener);
            headerAccessor.setLeaveMutable(true);

            template.convertAndSendToUser(
                    listener,
                    WebSocketConstants.SEND,
                    new Notification("title","message","topic","url"),
                    headerAccessor.getMessageHeaders());
        }
    }

    @EventListener
    public void sessionDisconnectionHandler(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        logger.info("Disconnecting " + sessionId + "!");
        remove(sessionId);
    }
}


