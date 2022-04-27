package cm.travelpost.tp.common.enums;

import cm.travelpost.tp.common.utils.StringUtils;

public enum Gender {

    MALE("Male"),
    FEMALE("Feminin"),
    NOT_DEFINE("Indefini");

    String value;

    Gender(String value) {
        this.value = value;
    }

    public String toValue() {
        return value;
    }

    public static Gender fromValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        for (Gender e : values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        throw new RuntimeException("Valore " + value + " non valido");
    }
}