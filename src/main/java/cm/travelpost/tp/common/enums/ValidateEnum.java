package cm.travelpost.tp.common.enums;

import cm.travelpost.tp.common.utils.StringUtils;

public enum ValidateEnum {
    INSERTED(" Inserée"),
    ACCEPTED("Acceptée"),
    REFUSED("Refusée");


    private String value;

    ValidateEnum(String value) {
        this.value = value;
    }

    public String toValue() {
        return value;
    }

    public static ValidateEnum fromValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        for (ValidateEnum e : values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        throw new RuntimeException("Valeur " + value + " invalide");
    }
}