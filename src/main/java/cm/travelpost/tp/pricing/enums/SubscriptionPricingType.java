package cm.travelpost.tp.pricing.enums;

import cm.travelpost.tp.common.utils.StringUtils;

public enum SubscriptionPricingType {

    BASE("BASE","Abonnement Base",3,20),
    MEDIUM("MEDIUM","Abonnement Medium",6, 50),
    PRENIUM("PRENIUM","Abonnement Prenium",12,100);

    private String name;
    private String description;
    private Integer month;
    private Integer publications;

    public String toName() {
        return name;
    }

    public String toDescription() {
        return description;
    }

    public Integer toMonth() {
        return month;
    }

    public Integer toPublications() {
        return publications;
    }


    SubscriptionPricingType(String name, String description, Integer month,  Integer publications) {
        this.name=name;
        this.description=description;
        this.month=month;
        this.publications=publications;
    }

    public static SubscriptionPricingType fromName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }

        for (SubscriptionPricingType e : values()) {
            if (e.name.equals(name)) {
                return e;
            }
        }
        throw new RuntimeException("Valeur  " + name + " non valide");
    }

    public static SubscriptionPricingType fromDescription(String description) {
        if (StringUtils.isEmpty(description)) {
            return null;
        }

        for (SubscriptionPricingType e : values()) {
            if (e.name.equals(description)) {
                return e;
            }
        }
        throw new RuntimeException("Valeur  " + description + " non valide");
    }
    public static SubscriptionPricingType fromMonth(Integer month) {
        if (month == null ) {
            return null;
        }

        for (SubscriptionPricingType e : values()) {
            if (e.month.equals(month)) {
                return e;
            }
        }
        throw new RuntimeException("Valeur  " + month + " non valide");
    }

    public static SubscriptionPricingType fromPublications(Integer publications) {
        if (publications == null ) {
            return null;
        }

        for (SubscriptionPricingType e : values()) {
            if (e.publications.equals(publications)) {
                return e;
            }
        }
        throw new RuntimeException("Valeur  " + publications + " non valide");
    }
}
