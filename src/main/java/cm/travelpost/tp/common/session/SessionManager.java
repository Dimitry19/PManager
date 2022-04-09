/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.common.session;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SessionManager implements ISessionManager{

    protected Map session = new HashMap();
    protected Map sessionNotifications = new HashMap();

    public Map getSession() {
        return session;
    }

    public void setSession(Map session) {
        this.session = session;
    }

    @Override
    public void addToSession(Object k, Object v) {
        session.putIfAbsent(k,v);

    }

    @Override
    public void removeToSession(Object k) {
        if(session.containsKey(k)){
            session.remove(k);
        }
    }

    @Override
    public void replaceIntoSession(Object k, Object v) {
        if(session.containsKey(k)){
            session.replace(k,v);
        }
    }

    @Override
    public Object getFromSession(Object k) {
        return session.get(k);

    }
}
