package cm.packagemanager.pmanager.configuration.filters;

public class FilterConstants {

	//@Where(clause= MessageFormat.format(FilterConstants.FILTER_WHERE_CANCELLED,"announce"))
	public static final String  CANCELLED ="cancelled";
	public static  final String ACTIVE_MBR="active";
	public static final String  FILTER_WHERE_ANNOUNCE_CANCELLED ="exists (select id from announce where cancelled = false)";
	public static final String  FILTER_WHERE_MESSAGE_CANCELLED ="exists (select id,token from message where cancelled = false)";
	public static final String  FILTER_WHERE_USER_CANCELLED ="exists (select id from user where cancelled = false)";
}
