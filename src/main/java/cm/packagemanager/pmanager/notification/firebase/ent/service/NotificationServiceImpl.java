package cm.packagemanager.pmanager.notification.firebase.ent.service;

import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.notification.firebase.ent.vo.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashSet;
import java.util.Set;

@Service
public class NotificationServiceImpl implements NotificationService{

	private static Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

	// The SimpMessagingTemplate is used to send Stomp over WebSocket messages.
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	private Set<String> listeners = new HashSet<>();


	/**
	 * Send notification to users subscribed on channel "/user/queue/notify".
	 *
	 * The message will be sent only to the user with the given username.
	 *
	 * @param notification The notification message.
	 * @param username The username for the user to send notification.
	 */
	@Override //Test2
	public void notify(NotificationRequest notification, String username) {
		messagingTemplate.convertAndSendToUser(
				username,
				"/queue/notify",
				notification
		);
		return;
	}

	//@Scheduled(fixedDelay = 2000)
	@Override
	public void dispatch() {
		for (String listener : listeners) {
			logger.info("Sending notification to " + listener);

			SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
			headerAccessor.setSessionId(listener);
			headerAccessor.setLeaveMutable(true);

			int value = (int) Math.round(Math.random() * 100d);
			NotificationRequest notification=new  NotificationRequest(
					"title", "messageBody","topicName");
			messagingTemplate.convertAndSendToUser(
					listener,
					"/notification/item",
					notification,
					headerAccessor.getMessageHeaders());
		}
	}


	@Override
	public void sendToUser(String sessionId, long id, String email, String username,NotificationRequest notification) {
		String listener=null;
		for (String listenerTo : listeners) {
			if(StringUtils.equals(sessionId,listener)){
				logger.info("Sending notification to " + listener);
				listener=listenerTo;
			}
		}
			SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
			headerAccessor.setSessionId(listener);
			headerAccessor.setLeaveMutable(true);

			int value = (int) Math.round(Math.random() * 100d);
			messagingTemplate.convertAndSendToUser(
					listener,
					"/notification/item",
					notification,
					headerAccessor.getMessageHeaders());
	}

	@Override
	public void add(String sessionId) {
		listeners.add(sessionId);
	}

	@Override
	public void remove(String sessionId) {
		listeners.remove(sessionId);
	}

	@EventListener
	public void sessionDisconnectionHandler(SessionDisconnectEvent event) {
		String sessionId = event.getSessionId();
		logger.info("Disconnecting " + sessionId + "!");
		remove(sessionId);
	}
}
