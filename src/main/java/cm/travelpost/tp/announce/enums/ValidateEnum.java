package cm.travelpost.tp.announce.enums;

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


    public static ValidateEnum getValidateEnum(String type) {

        for (ValidateEnum a : ValidateEnum.values()) {
            if (StringUtils.equals(type, a.toString())) {
                return a;
            }
        }
        throw new RuntimeException("Valeur  " + type + " non valide");
    }
}