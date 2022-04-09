package cm.travelpost.tp.websocket.listeners;

import cm.travelpost.tp.common.utils.CollectionsUtils;
import cm.travelpost.tp.notification.ent.service.NotificatorServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.Map;

@Component
public class WebSocketEventListener  {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    NotificatorServiceImpl notificationService;


    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {

        StompHeaderAccessor stompAccessor = StompHeaderAccessor.wrap(event.getMessage());
        @SuppressWarnings("rawtypes")
        GenericMessage connectHeader = (GenericMessage) stompAccessor
                .getHeader(SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER);
        // to the server
        @SuppressWarnings("unchecked")
        Map<String, List<String>> nativeHeaders = (Map<String, List<String>>) connectHeader.getHeaders()
                .get(SimpMessageHeaderAccessor.NATIVE_HEADERS);

        if (nativeHeaders != null) {
            if(CollectionsUtils.isNotEmpty(nativeHeaders.get("user"))){
                String user = nativeHeaders.get("user").get(0);
                String sessionId = stompAccessor.getSessionId();
                logger.info(" Connection by user <{}> with sessionId <{}>", user, sessionId);
                notificationService.addConnectedUser(user, sessionId);
                notificationService.addAll(sessionId);
            }
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor stompAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = stompAccessor.getSessionId();

        String user=notificationService.getConnectedUser(sessionId);
        logger.info("Socket disconnection by user <{}> with sessionId <{}>", user, sessionId);
        notificationService.remove(sessionId);
        notificationService.removeConnectedUser(sessionId);
    }
}