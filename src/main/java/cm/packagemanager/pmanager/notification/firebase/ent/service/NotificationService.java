package cm.packagemanager.pmanager.notification.firebase.ent.service;

import cm.packagemanager.pmanager.notification.firebase.ent.vo.NotificationRequest;

public interface NotificationService {

	public void notify(NotificationRequest notification, String username);

	void dispatch();

	void sendToUser(String sessionId, long id, String email, String username, NotificationRequest notification);

	void add(String sessionId);

	void remove(String sessionId);
}
