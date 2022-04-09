package cm.travelpost.tp.websocket.client;

import cm.travelpost.tp.websocket.client.handler.MyStompFrameHandler;
import cm.travelpost.tp.websocket.constants.WebSocketConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

//@Component
public class WebSocketSocketClient {

    protected static final Log logger = LogFactory.getLog(WebSocketSocketClient.class);

    //@EventListener(ApplicationReadyEvent.class) // Permet d'executer la methode après le demarrage de springboot
    public static void client() {


        try {
           /* session = stompClient.connect(,
                    new StompSessionHandlerAdapter() {})
                    .get();*/

            WebSocketClient webSocketClient = new StandardWebSocketClient();
            List transports = new ArrayList();
            transports.add(new WebSocketTransport(webSocketClient));
            SockJsClient sockJsClient = new SockJsClient(transports);

            WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());

            StompSessionHandler sessionHandler = new MyStompFrameHandler();

            ListenableFuture<StompSession> connect = stompClient.connect(WebSocketConstants.URL, sessionHandler);
            StompSession session = connect.get();
            logger.info("Session id " + session.getSessionId());


            //session.subscribe(NOTIFY_SEND, sessionHandler);
            session.send(WebSocketConstants.PREFIX_DESTINATION_APP + WebSocketConstants.SUSCRIBE_QUEUE_ANNOUNCE_SEND, null);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

}