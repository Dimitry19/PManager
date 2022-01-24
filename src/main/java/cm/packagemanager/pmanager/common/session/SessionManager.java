/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.packagemanager.pmanager.common.session;

import java.util.HashMap;
import java.util.Map;

public class SessionManager<K,V> {

    protected Map session = new HashMap();

    public void  addToSession(K k, V v){

        session.putIfAbsent(k,v);
    }

    public void  removeToSession(K k){

        if(session.containsKey(k)){
            session.remove(k);
        }
    }

    public void  replaceIntoSession(K k, V v){

        if(session.containsKey(k)){
            session.replace(k,v);
        }
    }

    public V getFromSession(K k){
        return (V)session.get(k);
    }

    public Map getSession() {
        return session;
    }

    public void setSession(Map session) {
        this.session = session;
    }

}
