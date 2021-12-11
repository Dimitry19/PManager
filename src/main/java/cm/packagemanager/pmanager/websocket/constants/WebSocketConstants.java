package cm.packagemanager.pmanager.websocket.constants;

public class WebSocketConstants {


    public static final String END_POINT = "/notifications";
    public static final String SUSCRIBE_QUEUE_ITEM_SEND = "/notification/item";
    public static final String SUSCRIBE_QUEUE_COMMENT_SEND = "/notification/comment";
    public static final String SUSCRIBE_QUEUE_ANNOUNCE_SEND = "/notification/announce";
    public static final String SUSCRIBE_QUEUE_USER_SEND = "/notification/user";


    public static final String PREFIX_DESTINATION_BROKER = "/notification";
    public static final String PREFIX_DESTINATION_BROKER_ANNOUNCE = "/announce";
    public static final String PREFIX_DESTINATION_BROKER_USER = "/user";
    public static final String PREFIX_DESTINATION_APP = "/pm-swns";


    public static final String NOTIFY_SEND = "/queue/notify";
    //public static final String ORIGINS="http://localhost:4200", "http://127.0.0.1:4200";
    public static final String ORIGIN_ALL = "*";
    public static final String URL = "http://localhost:8080/pmanager/notifications";

}
