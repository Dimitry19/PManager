package cm.travelpost.tp.websocket.constants;

public class WebSocketConstants {


    public static final String END_POINT = "/notifications";
    public static final String SUSCRIBE_QUEUE_ITEM_SEND = "/notification/item";
    public static final String SUSCRIBE_QUEUE_COMMENT_SEND = "/notification/comment";
    public static final String SUSCRIBE_QUEUE_ANNOUNCE_SEND = "/notification/announce";
    public static final String SUSCRIBE_QUEUE_USER_SEND = "/notification/user";


    public static final String BROKER_NOTIF = "/notification";
    public static final String BROKER_USER = "/user";
    public static final String PREFIX_DESTINATION_APP = "/swns";

    public static final  String LOCALHOST_ORIGIN=  "http://localhost:8080";
    public static final  String LOCALHOST_ORIGIN_127=  "http://127.0.0.1:8080";
    public static final  String LOCALHOST_ORIGIN_SSL=  "https://localhost:8443";
    public static final  String LOCALHOST_ORIGIN_SSL_127=  "https://127.0.0.1:8443";
    public static final  String HEROKU_ORIGIN=  "http://travelpost-cm.herokuapp.com";
    public static final  String HEROKU_ORIGIN_SSL=  "https://travelpost-cm.herokuapp.com/";


    public static final String ORIGIN_ALL = "*";
    public static final String URL = "http://localhost:8080/pmanager/notifications";
    public static final String URL_SSL = "https://localhost:8443/pmanager/notifications";

}
