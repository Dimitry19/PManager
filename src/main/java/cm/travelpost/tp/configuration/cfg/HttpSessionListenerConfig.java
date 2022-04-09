/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.configuration.cfg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class HttpSessionListenerConfig {

    private static Logger logger = LoggerFactory.getLogger(HttpSessionListenerConfig.class);
    Map sessions=new HashMap();

    @Bean
    public HttpSessionListener httpSessionListener() {
        return new HttpSessionListener() {
            @Override
            public void sessionCreated(HttpSessionEvent se) {
                logger.info("Session Created with session id {}" , se.getSession().getId());

                sessions.put(se.getSession().getAttribute("session-user"),se.getSession().getId());

            }
            @Override
            public void sessionDestroyed(HttpSessionEvent se) {
                logger.info("Session Destroyed, Session id {}" , se.getSession().getId());
                sessions.remove(se.getSession().getAttribute("session-user"));
            }
        };
    }
}
