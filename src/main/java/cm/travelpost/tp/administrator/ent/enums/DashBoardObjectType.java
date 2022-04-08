package cm.travelpost.tp.administrator.ent.enums;

import cm.travelpost.tp.common.utils.StringUtils;

public enum DashBoardObjectType {

    AIRLINE("Compagnie"),
    CITY("Ville");


    private String  value;

    DashBoardObjectType(String value){this.value=value;}

    public String toValue() {
        return value;
    }

    public static DashBoardObjectType fromValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        for (DashBoardObjectType e : values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        throw new RuntimeException("Valeur " + value + " invalide");
    }

}
