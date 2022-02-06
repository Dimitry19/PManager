package cm.packagemanager.pmanager.notification.firebase.ent.service;


import cm.packagemanager.pmanager.notification.firebase.enums.NotificationType;

import java.util.*;

public interface NotificationSocketService<T> {


    Set<String> listeners = new HashSet<>();
    Set notifications = new HashSet();
    Map commentListeners = new HashMap<>();
    Map userListeners = new HashMap<>();
    Map announceListeners = new HashMap<>();
    Map connectedUsers = new HashMap();
    Map sessionUserMap = new HashMap<>();


    void dispatch() throws Exception;

    void add(String sessionId, NotificationType notificationType);

    void addAll(String sessionId);

    void remove(String id);



}
