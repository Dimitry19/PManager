/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.notification.ent.vo;

import cm.travelpost.tp.notification.enums.NotificationType;

import java.util.Objects;

public class Notification {

    private Long id;
    private String title;
    private String message;
    private String topic;
    private String token;
    private NotificationType type;
    private Long  random;
    private Long  elementId;

    public Notification() {
    }

    public Notification(Long id, Long elementId,String title, String messageBody, NotificationType type, Long  random) {
        this.title = title;
        this.message = messageBody;
        this.type = type;
        this.random = random;
        this.id=id;
        this.elementId=elementId;
    }

    public Notification(String title, String messageBody, String topic, Long random) {
        this.title = title;
        this.message = messageBody;
        this.topic = topic;
        this.random = random;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public NotificationType getType() {    return type;  }

    public void setType(NotificationType type) {  this.type = type;  }

    public Long getRandom() {  return random; }

    public void setRandom(Long random) {   this.random = random; }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {  return token;  }

    public void setToken(String token) {  this.token = token;  }

    public Long getElementId() {
        return elementId;
    }

    public void setElementId(Long elementId) {
        this.elementId = elementId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(message, that.message) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, type);
    }
}
