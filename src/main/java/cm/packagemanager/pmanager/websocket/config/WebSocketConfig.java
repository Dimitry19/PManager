package cm.packagemanager.pmanager.websocket.config;

import cm.packagemanager.pmanager.common.interceptor.HttpHandshakeInterceptor;
import cm.packagemanager.pmanager.component.CrossDomainFilter;
import cm.packagemanager.pmanager.websocket.constants.WebSocketConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.client.standard.WebSocketContainerFactoryBean;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static cm.packagemanager.pmanager.websocket.constants.WebSocketConstants.*;


/*https://www.codesandnotes.be/2020/03/31/websocket-based-notification-system-using-spring/*/


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(BROKER_NOTIF);
        registry.setApplicationDestinationPrefixes(WebSocketConstants.PREFIX_DESTINATION_APP);
    }

    //Version OK en HTTTP
  /*  @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WebSocketConstants.END_POINT)
                //.setAllowedOrigins("http://localhost:4200", "http://127.0.0.1:4200")
                .setAllowedOrigins("*")
                .withSockJS();
    }*/

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WebSocketConstants.END_POINT)
                .addInterceptors(new HttpHandshakeInterceptor())
                //.setAllowedOrigins("http://localhost:4200", "http://127.0.0.1:4200")
                .setAllowedOrigins("*").withSockJS();
    }

    /***
     * Permet de faire fonctionner la WSocket avec tomcat(Cas de Spring)
     * http://host:port/{path-to-sockjs-endpoint}/{server-id}/{session-id}/{transport}
     */

   /* @Bean
    public WebSocketContainerFactoryBean createWebSocketContainer() {
        WebSocketContainerFactoryBean container = new WebSocketContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }
    */

    //@Bean
    public CrossDomainFilter corsFilter() throws Exception {
        return new CrossDomainFilter();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration){
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if(StompCommand.CONNECT.equals(accessor.getCommand())){
                    //System.out.println("Connect ");
                } else if(StompCommand.SUBSCRIBE.equals(accessor.getCommand())){
                    //System.out.println("Subscribe to :"+message);
                } else if(StompCommand.SEND.equals(accessor.getCommand())){
                    //System.out.println("Send message " );
                } else if(StompCommand.DISCONNECT.equals(accessor.getCommand())){
                    //System.out.println("Exit ");
                } else {
                }
                return message;
            }
        });
    }
}
