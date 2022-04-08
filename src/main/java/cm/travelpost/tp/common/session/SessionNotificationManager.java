/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.common.session;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SessionNotificationManager implements ISessionManager{

    protected Map sessionNotifications = new HashMap();

    public Map getSession() {
        return sessionNotifications;
    }

    public void setSession(Map session) {
        this.sessionNotifications = session;
    }

    @Override
    public void addToSession(Object k, Object v) {
        sessionNotifications.putIfAbsent(k,v);

    }

    @Override
    public void removeToSession(Object k) {
        if(sessionNotifications.containsKey(k)){
            sessionNotifications.remove(k);
        }
    }

    @Override
    public void replaceIntoSession(Object k, Object v) {
        if(sessionNotifications.containsKey(k)){
            sessionNotifications.replace(k,v);
        }
    }

    @Override
    public Object getFromSession(Object k) {
        return sessionNotifications.get(k);

    }
}
