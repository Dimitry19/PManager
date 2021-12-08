package cm.packagemanager.pmanager.rating.enums;

public enum Rating {
    TERRIBLE(1),
    POOR(2),
    AVERAGE(3),
    GOOD(4),
    EXCELLENT(5);

    private int value;

    Rating(int value) {
        this.value = value;
    }

    public int toValue() {
        return value;
    }

    public static Rating fromValue(int value) {
        for (Rating e : values()) {
            if (e.value == value) {
                return e;
            }
        }
        throw new RuntimeException("Valeur " + value + " invalide");
    }
}
