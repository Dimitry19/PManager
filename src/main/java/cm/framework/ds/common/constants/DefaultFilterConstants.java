package cm.framework.ds.common.constants;

public class DefaultFilterConstants {

    public DefaultFilterConstants() {
    }

    public static final String CANCELLED = "cancelled";
    public static final String COMPLETED = "completed";
    public static final String STATUS = "byStatus";
    public static final String NOT_COMPLETED = "not_completed";

    public static final String FILTER_ACTIVITY_CANC = "exists (select code, token from activity where cancelled = false)";
}
