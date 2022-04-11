/*
 * Copyright (c) 2022.  PManager entièrement realisé par Dimitri Sime.
 * Tous les droits lui sont exclusivement réservés
 */

package cm.travelpost.tp.notification.enums;

import cm.travelpost.tp.common.utils.StringUtils;

public enum NotificationType {

    USER("Utilisateur"),
    COMMENT("Commentaire"),
    RESERVATION("Reservation"),
    SUBSCRIBE("Abonnement"),
    UNSUBSCRIBE("Desabonnement"),
    ANNOUNCE(" Annonce"),

    COMMENT_UPD("Commentaire Modifié"),
    RESERVATION_UPD("Reservation Modifiée"),
    RESERVATION_VALIDATE("Reservation acceptée"),
    RESERVATION_UNVALIDATE("Reservation refusée"),
    RESERVATION_DEL("Reservation Supprimée"),

    ANNOUNCE_UPD(" Annonce Modifiée"),
    ANNOUNCE_DEL(" Annonce Supprimée");

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
