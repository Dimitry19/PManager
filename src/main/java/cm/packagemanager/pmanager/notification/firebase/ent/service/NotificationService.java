package cm.packagemanager.pmanager.notification.firebase.ent.service;

import cm.packagemanager.pmanager.notification.firebase.ent.vo.Notification;
import cm.packagemanager.pmanager.notification.firebase.enums.NotificationType;

public interface NotificationService {

	public void notify(Notification notification, String username);

	void dispatch();

	void sendToUser(String sessionId, long id, String email, String username, Notification notification);

	void add(String sessionId, NotificationType notificationType);

	void remove(String sessionId);

	void addComment(String sessionId, String user);
}
