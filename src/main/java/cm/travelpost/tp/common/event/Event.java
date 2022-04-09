/*
 * Copyright (c) 2021.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.common.event;

import cm.travelpost.tp.notification.enums.NotificationType;

import java.util.Date;
import java.util.Set;

public  class Event {

    private Long id;
    private String message;
    protected Long userId;
    protected String username;
    protected Date date;
    protected Object object;
    protected NotificationType type;
    protected Set users;

    public Event(Date date, NotificationType type) {
        this.date = date;
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public NotificationType getType() {
        return type;
    }

    public Object getObject() {
        return object;
    }

    public Long getUserId() {  return userId;   }

    public Set getUsers() {  return users;  }

    public void setUsers(Set users) {
        this.users = users;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
