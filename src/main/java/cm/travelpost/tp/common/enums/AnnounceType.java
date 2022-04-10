package cm.travelpost.tp.common.enums;

import cm.travelpost.tp.common.utils.StringUtils;

public enum AnnounceType {
    BUYER("Acheteur"),
    SELLER("Vendeur");

    String value;

    AnnounceType(String value) {
        this.value = value;
    }

    public String toValue() {
        return value;
    }

    public static AnnounceType fromValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        for (AnnounceType e : values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        throw new RuntimeException("Valeur  " + value + " non valide");
    }
}