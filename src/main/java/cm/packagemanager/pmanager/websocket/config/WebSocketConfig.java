package cm.packagemanager.pmanager.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.client.standard.WebSocketContainerFactoryBean;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/*@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.setApplicationDestinationPrefixes("/app")
				.enableSimpleBroker("/socket");
	}

	@Override public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/socket")
				.setAllowedOrigins("*")
				.withSockJS();
	}
}*/

/*https://www.codesandnotes.be/2020/03/31/websocket-based-notification-system-using-spring/*/


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/notification");
		registry.setApplicationDestinationPrefixes("/swns");
	}
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/notifications")
				.setAllowedOrigins("*")
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