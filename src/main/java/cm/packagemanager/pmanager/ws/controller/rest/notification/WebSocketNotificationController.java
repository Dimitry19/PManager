package cm.packagemanager.pmanager.ws.controller.rest.notification;


import cm.packagemanager.pmanager.constant.WSConstants;
import cm.packagemanager.pmanager.notification.firebase.ent.service.NotificationService;
import cm.packagemanager.pmanager.notification.firebase.enums.NotificationType;
import cm.packagemanager.pmanager.websocket.constants.WebSocketConstants;
import cm.packagemanager.pmanager.ws.controller.rest.CommonController;
import io.swagger.annotations.Api;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.web.PageableDefault;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.awt.print.Pageable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static cm.packagemanager.pmanager.constant.WSConstants.*;

@Controller
@RestController
@RequestMapping(SOCKET_NOTIFICATION_WS)
@Api(value = "socket-notifications-service", description = "Socket Notifications ")
public class WebSocketNotificationController extends CommonController {

    protected final Log logger = LogFactory.getLog(WebSocketNotificationController.class);

    @Resource(name = "notificator")
    private NotificationService dispatcher;


    @MessageMapping("/start")
    public void start(StompHeaderAccessor stompHeaderAccessor) {
        dispatcher.add(stompHeaderAccessor.getSessionId(), NotificationType.USER);
    }


    @MessageMapping(WebSocketConstants.SUSCRIBE_QUEUE_ANNOUNCE_SEND)
    /// @SendTo(WebSocketConstants.SUSCRIBE_QUEUE_ANNOUNCE_SEND)
    public void announceNotification(StompHeaderAccessor stompHeaderAccessor) {
        dispatcher.add(stompHeaderAccessor.getSessionId(), NotificationType.ANNOUNCE);
    }

    @MessageMapping("/comment/start")
    public void commentStart(StompHeaderAccessor stompHeaderAccessor) {
        dispatcher.add(stompHeaderAccessor.getSessionId(), NotificationType.COMMENT);
    }

    @MessageMapping("/stop")
    public void stop(StompHeaderAccessor stompHeaderAccessor) {
        logger.info("stop websocket request");
        dispatcher.remove(stompHeaderAccessor.getSessionId());
    }


    @RequestMapping(value = SUBSCRIPTIONS_SOCKET_WS, method = RequestMethod.GET, headers = WSConstants.HEADER_ACCEPT)
    @ResponseBody  public List<String> topics(@RequestParam @Valid String topic) throws Exception {

        logger.info("retrieve  subcribe list request in");
        return Arrays.asList(WebSocketConstants.SUSCRIBE_QUEUE_ITEM_SEND,WebSocketConstants.SUSCRIBE_QUEUE_COMMENT_SEND,WebSocketConstants.SUSCRIBE_QUEUE_ANNOUNCE_SEND,
                WebSocketConstants.SUSCRIBE_QUEUE_USER_SEND);

    }
}