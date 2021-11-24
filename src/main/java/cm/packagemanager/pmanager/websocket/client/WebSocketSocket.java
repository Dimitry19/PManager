package cm.packagemanager.pmanager.websocket.client;

import static cm.packagemanager.pmanager.constant.WSConstants.*;
import static cm.packagemanager.pmanager.websocket.constants.WebSocketConstants.*;

import cm.packagemanager.pmanager.websocket.constants.WebSocketConstants;
import cm.packagemanager.pmanager.websocket.handler.MyStompFrameHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class WebSocketSocket {

    protected static final Log logger = LogFactory.getLog(WebSocketSocket.class);

    //@EventListener(ApplicationReadyEvent.class) // Permet d'executer la methode après le demarrage de springboot
    public static  void client() {


        try {
           /* session = stompClient.connect(,
                    new StompSessionHandlerAdapter() {})
                    .get();*/

            WebSocketClient webSocketClient = new StandardWebSocketClient();
            List transports= new ArrayList();
            transports.add(new WebSocketTransport(webSocketClient));
            SockJsClient sockJsClient =new SockJsClient(transports);

            WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());

            StompSessionHandler sessionHandler= new  MyStompFrameHandler();

            ListenableFuture<StompSession> connect= stompClient.connect(URL,sessionHandler);
            StompSession session=connect.get();
            logger.info("Session id "+ session.getSessionId());




            //session.subscribe(NOTIFY_SEND, sessionHandler);
            session.send(PREFIX_DESTINATION_APP+ WebSocketConstants.ANNOUNCE_SEND, null);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

}