package cm.packagemanager.pmanager.notification.firebase.enums;

import cm.packagemanager.pmanager.common.utils.StringUtils;

public enum NotificationType {

    USER("Notification utilisateur"),
    ANNOUNCE("Notification annonce");

    String value;
    private NotificationType(String value) {
        this.value = value;
    }

    public String toValue(){
        return value;
    }

    public static NotificationType fromValue(String value){
        if(StringUtils.isEmpty(value)){
            return null;
        }

        for (NotificationType e : values()) {
            if(e.value.equals(value)){
                return e;
            }
        }
        throw new RuntimeException("Valeur  " + value + " non valide");
    }
}
