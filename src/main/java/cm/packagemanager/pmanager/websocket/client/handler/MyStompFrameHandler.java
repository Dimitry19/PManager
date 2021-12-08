package cm.packagemanager.pmanager.websocket.client.handler;

import cm.packagemanager.pmanager.notification.firebase.ent.vo.Notification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;

public class MyStompFrameHandler extends StompSessionHandlerAdapter implements StompFrameHandler {

    private Logger logger = LogManager.getLogger(MyStompSessionHandler.class);

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {

        System.out.println("Connected!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders
            headers, byte[] payload, Throwable exception) {
        logger.error("Got an exception", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Notification.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        Notification msg = (Notification) payload;
        System.out.println(msg.toString());
        logger.info("Received : " + msg.toString());
    }
}
