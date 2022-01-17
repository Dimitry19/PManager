package cm.packagemanager.pmanager.notification.firebase.ent.vo;

import cm.packagemanager.pmanager.notification.firebase.enums.NotificationType;

public class Notification {

    private Long id;
    private String title;
    private String message;
    private String topic;
    private String token;
    private NotificationType type;
    private Long  elementId;

    public Notification() {
    }

    public Notification(Long id,String title, String messageBody, NotificationType type, Long  elementId) {
        this.title = title;
        this.message = messageBody;
        this.type = type;
        this.elementId = elementId;
        this.id=id;
    }

    public Notification(String title, String messageBody, String topic, Long elementId) {
        this.title = title;
        this.message = messageBody;
        this.topic = topic;
        this.elementId = elementId;
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

    public Long getElementId() {  return elementId;  }

    public void setElementId(Long elementId) {  this.elementId = elementId;  }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {  return token;  }

    public void setToken(String token) {  this.token = token;  }
}
