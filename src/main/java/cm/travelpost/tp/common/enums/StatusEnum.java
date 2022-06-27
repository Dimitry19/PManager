package cm.travelpost.tp.common.enums;

import cm.travelpost.tp.common.utils.StringUtils;

public enum StatusEnum {
    VALID("Valid"),
    DELETED("Supprimé"),
    TO_DELIV("A Notifier"),
    COMPLETED("Completé");

    String value;

    StatusEnum(String value){this.value=value;}

    public String toValue() {
        return value;
    }

    public static StatusEnum fromValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        for (StatusEnum e : values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        throw new RuntimeException("Valeur  " + value + " non valide");
    }

    public static StatusEnum getStatusEnum(String type) {

        for (StatusEnum a : StatusEnum.values()) {
            if (StringUtils.equals(type, a.toString())) {
                return a;
            }
        }
        throw new RuntimeException("Valeur  " + type + " non valide");
    }
}