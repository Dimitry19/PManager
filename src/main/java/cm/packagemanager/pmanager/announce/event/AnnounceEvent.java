/*
 * Copyright (c) 2021.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.packagemanager.pmanager.announce.event;

import cm.packagemanager.pmanager.common.event.Event;
import cm.packagemanager.pmanager.notification.firebase.enums.NotificationType;

import java.util.Date;

public class AnnounceEvent extends Event {

    private Long id;
    private String message;


    public AnnounceEvent(Date date, NotificationType type){
        super(date,type);
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {  return message;  }

    public void setMessage(String message) {  this.message = message;  }

    public void setId(Long id) {
        this.id = id;
    }
}
