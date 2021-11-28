package cm.packagemanager.pmanager.notification.firebase.ent.service;

import cm.packagemanager.pmanager.notification.firebase.ent.vo.Notification;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface FirebaseCloudMessageService {

	public void sendMessageWithoutData(Notification request)
			throws InterruptedException, ExecutionException;

	public void sendMessage(Map<String, String> data, Notification request)
			throws InterruptedException, ExecutionException;

	void sendMessageToToken(Notification request) throws InterruptedException, ExecutionException;
}
