/*
 * Copyright (c) 2021.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.packagemanager.pmanager.websocket.client.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


//Client side
public class SessionListener {

    @Component
    public class SessionConnectedListener extends SessionsListener implements ApplicationListener<SessionConnectedEvent> {

        @Override
        public void onApplicationEvent(SessionConnectedEvent event) {
            if (event.getUser() != null) {
                users.add(event.getUser().getName());

            }
        }

    }

    @Component
    class SessionDisconnectListener extends SessionsListener implements ApplicationListener<SessionDisconnectEvent> {

        @Override
        public void onApplicationEvent(SessionDisconnectEvent event) {
            if (event.getUser() != null) {
                users.remove(event.getUser().getName());
            }
        }
    }

    @Component
    class SessionSubscribeListener extends SessionsListener implements ApplicationListener<SessionSubscribeEvent> {

        @Override
        public void onApplicationEvent(SessionSubscribeEvent event) {
            if (event.getUser() != null) {
                users.add(event.getUser().getName());
            }
        }
    }

    @Component
    class SessionUnsubscribeListener extends SessionsListener implements ApplicationListener<SessionUnsubscribeEvent> {

        @Override
        public void onApplicationEvent(SessionUnsubscribeEvent event) {
            if (event.getUser() != null) {
                users.remove(event.getUser().getName());
            }
        }
    }

    class SessionsListener {

        protected List<String> users = Collections.synchronizedList(new LinkedList<String>());

        public List<String> getUsers() {
            return users;
        }
    }
}






