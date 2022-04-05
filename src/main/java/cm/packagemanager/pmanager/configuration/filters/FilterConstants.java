package cm.packagemanager.pmanager.configuration.filters;

public class FilterConstants {

    //@Where(clause= MessageFormat.format(FilterConstants.FILTER_WHERE_CANCELLED,"announce"))
    public static final String CANCELLED = "cancelled";
    public static final String ACTIVE_MBR = "active";
    public static final String FILTER_ANNOUNCE_CANC_COMPLETED = "exists (select id from announce where cancelled = false and status<> 'COMPLETED')";
    public static final String FILTER_AIRLINE_CANC = "exists (select id from airline where cancelled = false)";
    public static final String FILTER_NOTIFICATION_CANC_COMPLETED = "exists (select id from notification where cancelled = false and status<> 'COMPLETED')";
    public static final String FILTER_WHERE_MESSAGE_CANCELLED = "exists (select id,token from message where cancelled = false)";
    public static final String FILTER_WHERE_USER_CANCELLED = "exists (select id from tp_user where cancelled = false)";
    public static final String FILTER_WHERE_RESERVATION_CANC_COMPLETED = "exists (select id from reservation where cancelled = false and status<> 'COMPLETED')";
}
