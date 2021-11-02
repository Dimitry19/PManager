package cm.packagemanager.pmanager.ws.controller.rest.notification;


import cm.packagemanager.pmanager.notification.firebase.ent.service.NotificationService;
import cm.packagemanager.pmanager.websocket.dispatcher.NotificationDispatcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketNotificationController {

    protected final Log logger = LogFactory.getLog(WebSocketNotificationController.class);

    @Autowired
    private  NotificationService dispatcher;


    @MessageMapping("/start")
    public void start(StompHeaderAccessor stompHeaderAccessor) {
        dispatcher.add(stompHeaderAccessor.getSessionId());
    }

    @MessageMapping("/comment/start")
    public void commentStart(StompHeaderAccessor stompHeaderAccessor) {
        dispatcher.addComment(stompHeaderAccessor.getSessionId());
    }

    @MessageMapping("/stop")
    public void stop(StompHeaderAccessor stompHeaderAccessor) {
        logger.info("stop websocket request");
        dispatcher.remove(stompHeaderAccessor.getSessionId());
    }
}