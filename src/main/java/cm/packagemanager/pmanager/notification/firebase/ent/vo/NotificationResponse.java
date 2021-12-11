package cm.packagemanager.pmanager.notification.firebase.ent.vo;

public class NotificationResponse {
    private int status;
    private String message;

    public NotificationResponse() {
    }

    public NotificationResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
