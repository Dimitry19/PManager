package cm.packagemanager.pmanager.ws.controller.rest.notification;


import cm.packagemanager.pmanager.notification.firebase.ent.service.NotificationService;
import cm.packagemanager.pmanager.notification.firebase.enums.NotificationType;
import cm.packagemanager.pmanager.websocket.constants.WebSocketConstants;
import io.swagger.annotations.Api;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static cm.packagemanager.pmanager.constant.WSConstants.SOCKET_NOTIFICATION_WS;

@Controller
@RestController
@RequestMapping(SOCKET_NOTIFICATION_WS)
@Api(value="socket-notifications-service", description="Socket Notifications ")
public class WebSocketNotificationController {

    protected final Log logger = LogFactory.getLog(WebSocketNotificationController.class);

    @Autowired
    private  NotificationService dispatcher;


    @MessageMapping("/start")
    public void start(StompHeaderAccessor stompHeaderAccessor) {
        dispatcher.add(stompHeaderAccessor.getSessionId(),NotificationType.USER);
    }


    @MessageMapping(WebSocketConstants.ANNOUNCE_SEND)
    @SendTo(WebSocketConstants.ANNOUNCE_SEND)
    public void announceNotification(StompHeaderAccessor stompHeaderAccessor) {
        dispatcher.add(stompHeaderAccessor.getSessionId() , NotificationType.ANNOUNCE);
    }

    @MessageMapping("/comment/start")
    public void commentStart(StompHeaderAccessor stompHeaderAccessor) {
        dispatcher.addComment(stompHeaderAccessor.getSessionId(), null);
    }

    @MessageMapping("/stop")
    public void stop(StompHeaderAccessor stompHeaderAccessor) {
        logger.info("stop websocket request");
        dispatcher.remove(stompHeaderAccessor.getSessionId());
    }
}