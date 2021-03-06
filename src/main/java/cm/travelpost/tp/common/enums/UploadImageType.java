package cm.travelpost.tp.common.enums;

import cm.travelpost.tp.common.utils.StringUtils;

public enum UploadImageType {

    USER("User"),
    ANNOUNCE("Announce");

    String value;

    UploadImageType(String value) {
        this.value = value;
    }

    public String toValue() {
        return value;
    }

    public static UploadImageType fromValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        for (UploadImageType e : values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        throw new RuntimeException("Valore " + value + " non valido");
    }
}
