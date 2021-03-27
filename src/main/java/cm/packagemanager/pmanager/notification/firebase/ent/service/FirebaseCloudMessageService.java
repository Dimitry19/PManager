package cm.packagemanager.pmanager.notification.firebase.ent.service;

import cm.packagemanager.pmanager.notification.firebase.ent.vo.NotificationRequest;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface FirebaseCloudMessageService {

	public void sendMessageWithoutData(NotificationRequest request)
			throws InterruptedException, ExecutionException;

	public void sendMessage(Map<String, String> data, NotificationRequest request)
			throws InterruptedException, ExecutionException;

	void sendMessageToToken(NotificationRequest request) throws InterruptedException, ExecutionException;
}
