/*
 * Copyright (c) 2021.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.announce.event;

import cm.travelpost.tp.common.event.Event;
import cm.travelpost.tp.notification.enums.NotificationType;

import java.util.Date;

public class AnnounceEvent extends Event {

    private Long id;
    private String message;

    public AnnounceEvent(Date date, NotificationType type) {
        super(date, type);
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
