package cm.travelpost.tp.pricing.enums;

import cm.travelpost.tp.common.utils.StringUtils;

public enum PricingSubscriptionType {

    BASE("BASE","Abonnement Base",3),
    MEDIUM("MEDIUM","Abonnement Medium",6),
    PRENIUM("PRENIUM","Abonnement Prenium",12);

    private String name;
    private String description;
    private Integer month;

    public String toName() {
        return name;
    }

    public String toDescription() {
        return description;
    }

    public Integer toMonth() {
        return month;
    }


    PricingSubscriptionType(String name, String description, Integer month) {
        this.name=name;
        this.description=description;
        this.month=month;
    }

    public static PricingSubscriptionType fromName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }

        for (PricingSubscriptionType e : values()) {
            if (e.name.equals(name)) {
                return e;
            }
        }
        throw new RuntimeException("Valeur  " + name + " non valide");
    }

    public static PricingSubscriptionType fromDescription(String description) {
        if (StringUtils.isEmpty(description)) {
            return null;
        }

        for (PricingSubscriptionType e : values()) {
            if (e.name.equals(description)) {
                return e;
            }
        }
        throw new RuntimeException("Valeur  " + description + " non valide");
    }
    public static PricingSubscriptionType fromMonth(Integer month) {
        if (month == null ) {
            return null;
        }

        for (PricingSubscriptionType e : values()) {
            if (e.month.equals(month)) {
                return e;
            }
        }
        throw new RuntimeException("Valeur  " + month + " non valide");
    }
}
