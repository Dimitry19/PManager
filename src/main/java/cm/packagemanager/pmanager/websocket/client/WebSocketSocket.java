package cm.packagemanager.pmanager.websocket.client;

import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;

import java.util.concurrent.ExecutionException;


public class WebSocketSocket {

    @LocalServerPort
    protected int testServerPort;


    public void client() {

        WebSocketClient webSocketClient = new StandardWebSocketClient();
        SockJsClient sockJsClient = null;//new SockJsClient(listOf(new WebSocketTransport(webSocketClient)));
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession session=null;
        try {
            session = stompClient.connect("http://localhost:" + testServerPort + "/notifications",
                    new StompSessionHandlerAdapter() {})
                    .get();

            session.send("/swns/start", null);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

}