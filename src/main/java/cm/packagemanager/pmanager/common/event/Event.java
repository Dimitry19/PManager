/*
 * Copyright (c) 2021.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.packagemanager.pmanager.common.event;

import cm.packagemanager.pmanager.notification.firebase.enums.NotificationType;

import java.util.Date;

public abstract class Event {

    protected Date date;
    protected Object object;
    protected NotificationType type;

    public Event(Date date , NotificationType type){
        this.date=date;
        this.type=type;
    }

    public Date getDate() {
        return date;
    }

    public NotificationType getType() {
        return type;
    }

    public Object getObject() { return object;  }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setObject(Object object) {  this.object = object; }

    public void setType(NotificationType type) {
        this.type = type;
    }
}