/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.notification.enums;

import cm.travelpost.tp.common.utils.StringUtils;

public enum NotificationType {

    USER("Notification utilisateur"),
    COMMENT("Notification commentaire"),
    RESERVATION("Notification reservation"),
    ANNOUNCE("Notification annonce");

    String value;

    NotificationType(String value) {
        this.value = value;
    }

    public String toValue() {
        return value;
    }

    public static NotificationType fromValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        for (NotificationType e : values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        throw new RuntimeException("Valeur  " + value + " non valide");
    }
}
