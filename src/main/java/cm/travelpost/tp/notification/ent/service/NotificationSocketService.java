/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.notification.ent.service;


import cm.travelpost.tp.notification.enums.NotificationType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
