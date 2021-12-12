package cm.packagemanager.pmanager.notification.firebase.ent.service;

import cm.packagemanager.pmanager.notification.firebase.ent.vo.Notification;
import cm.packagemanager.pmanager.notification.firebase.enums.NotificationType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface NotificationService {


    Set<String> listeners = new HashSet<>();
    Set notifications = new HashSet<>();
    Set notificationsToPersist = new HashSet<>();
    Map commentListeners = new HashMap<>();
    Map userListeners = new HashMap<>();
    Map announceListeners = new HashMap<>();
    Map connectedUsers = new HashMap();
    Map sessionUserMap = new HashMap<>();


    void notify(Notification notification, String username);

    void dispatch();

    void sendToUser(String sessionId, long id, String email, String username, Notification notification);

    void add(String sessionId, NotificationType notificationType);
    void addAll(String sessionId);

    void remove(String id);


}
