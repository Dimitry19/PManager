package cm.packagemanager.pmanager.websocket.config;


import cm.packagemanager.pmanager.common.interceptor.HttpHandshakeInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


/*https://www.codesandnotes.be/2020/03/31/websocket-based-notification-system-using-spring/*/


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    @Value("${travel.post.stomp.notification.origin}")
    private String origin;

    @Value("${travel.post.stomp.notification.origin.localhost}")
    private String originLocalhost;

    @Value("${travel.post.stomp.notification.origin.localhost.ssl}")
    private String originLocalhostSsl;

    @Value("${travel.post.stomp.notification.origin.localhost.127}")
    private String originLocalhost127;

    @Value("${travel.post.stomp.notification.origin.localhost.ssl.127}")
    private String originLocalhostSsl127;

    @Value("${travel.post.stomp.notification.origin.prod.ssl}")
    private String originProdSsl;

    @Value("${travel.post.stomp.notification.endpoint}")
    private String endpoint;

    @Value("${travel.post.stomp.notification.subscribe.item.send}")
    private String subscribeItemSend;

    @Value("${travel.post.stomp.notification.subscribe.user.send}")
    private String subscribeUserSend;

    @Value("${travel.post.stomp.notification.subscribe.comment.send}")
    private String subscribeCommentSend;

    @Value("${travel.post.stomp.notification.subscribe.announce.send}")
    private String subscribeAnnounceSend;

    @Value("${travel.post.stomp.notification.broker.notification}")
    private String brokerNotification;

    @Value("${travel.post.stomp.notification.broker.user}")
    private String brokerUser;

    @Value("${travel.post.stomp.notification.prefix.destination}")
    private String prefixDestination;



    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(brokerNotification);
        registry.setApplicationDestinationPrefixes(prefixDestination);
    }


    //https://github.com/kkojot/spring-vue-websocket-stomp
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(endpoint)
                .addInterceptors(new HttpHandshakeInterceptor())
                .setAllowedOrigins(originLocalhost,originLocalhost127,originLocalhostSsl,
                        originLocalhostSsl127,originProdSsl)
                .withSockJS();
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration){
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if(StompCommand.CONNECT.equals(accessor.getCommand())){
                    logger.info("Connect ");
                } else if(StompCommand.SUBSCRIBE.equals(accessor.getCommand())){
                    logger.info("Subscribe to :"+message);
                } else if(StompCommand.SEND.equals(accessor.getCommand())){
                    logger.info("Send message!" );
                } else if(StompCommand.DISCONNECT.equals(accessor.getCommand())){
                    logger.info("Disconnect!");
                } else {
                }
                return message;
            }
        });
    }
}
