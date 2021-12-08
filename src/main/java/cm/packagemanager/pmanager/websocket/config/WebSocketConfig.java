package cm.packagemanager.pmanager.websocket.config;

import cm.packagemanager.pmanager.websocket.constants.WebSocketConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
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
        registry.enableSimpleBroker(PREFIX_DESTINATION_BROKER, PREFIX_DESTINATION_BROKER_ANNOUNCE, PREFIX_DESTINATION_BROKER_USER);
        registry.setApplicationDestinationPrefixes(WebSocketConstants.PREFIX_DESTINATION_APP);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WebSocketConstants.END_POINT)
                .setAllowedOrigins(WebSocketConstants.ORIGIN_ALL)
                .withSockJS();
    }

    /***
     * Permet de faire fonctionner la WSocket avec tomcat(Cas de Spring)
     * http://host:port/{path-to-sockjs-endpoint}/{server-id}/{session-id}/{transport}
     */

    @Bean
    public WebSocketContainerFactoryBean createWebSocketContainer() {
        WebSocketContainerFactoryBean container = new WebSocketContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }
}
