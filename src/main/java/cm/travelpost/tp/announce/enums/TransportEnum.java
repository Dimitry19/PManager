package cm.travelpost.tp.announce.enums;

import cm.travelpost.tp.common.utils.StringUtils;

public enum TransportEnum {
    PLANE("Avion"),
    AUTO("Voiture"),
    NAVE("Bateau");

    private String value;

    TransportEnum(String value) {
        this.value = value;
    }

    public String toValue() {
        return value;
    }

    public static TransportEnum fromValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        for (TransportEnum e : values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        throw new RuntimeException("Valeur " + value + " invalide");
    }
}
