package cm.travelpost.tp.websocket.client.handler;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {
    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
        return super.getPayloadType(stompHeaders);
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object o) {

    }

    @Override
    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {

    }

    @Override
    public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {

    }

    @Override
    public void handleTransportError(StompSession stompSession, Throwable throwable) {

    }
}
