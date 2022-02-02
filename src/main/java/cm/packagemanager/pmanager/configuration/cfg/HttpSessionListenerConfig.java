/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.packagemanager.pmanager.configuration.cfg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


@Configuration
public class HttpSessionListenerConfig {


    private  Logger logger = LoggerFactory.getLogger(HttpSessionListenerConfig.class);

    private Map sessions;

    private final AtomicInteger activeSessions;


    public HttpSessionListenerConfig() {
        super();
        sessions = new HashMap();
        activeSessions = new AtomicInteger();
    }


    // bean for http session listener
    @Bean
    public HttpSessionListener httpSessionListener() {
        return new HttpSessionListener() {
            @Override
            public void sessionCreated(HttpSessionEvent se) {
                System.out.println("Session Created with session id+" + se.getSession().getId());
            }
            @Override
            public void sessionDestroyed(HttpSessionEvent se) {
                System.out.println("Session Destroyed, Session id:" + se.getSession().getId());
            }
        };
    }
}
