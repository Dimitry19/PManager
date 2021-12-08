package cm.packagemanager.pmanager.common.enums;

import cm.packagemanager.pmanager.common.utils.StringUtils;

public enum CommunicationType {
    SERVICE("Service"),
    WARNING("Warning"),
    NEWS("Nouveautés");

    String value;

    CommunicationType(String value) {
        this.value = value;
    }

    public String toValue() {
        return value;
    }

    public static CommunicationType fromValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        for (CommunicationType e : values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        throw new RuntimeException("Valeur  " + value + " non valide");
    }
}
