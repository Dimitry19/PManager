package cm.packagemanager.pmanager.constant;

public class WSConstants {

    public static final String HEADER_ACCEPT = "Accept=application/json";


    public static final String CREATE = "/create";
    public static final String ADD = "/add";
    public static final String UPDATE = "/update";
    public static final String DELETE = "/delete";
    public static final String BY_USER = "/user";
    public static final String FIND = "/find";
    public static final String VALIDATE = "/validate";
    public static final String IMAGE = "/{imageName}";
    public static final String UPLOAD = "/upload";


    /************ USER REQUEST*************/
    public static final String USER_WS = "/ws/user/*";
    public static final String USER_WS_REGISTRATION = "/register";
    public static final String USER_WS_CONFIRMATION = "/confirm";
    public static final String USER_WS_LOGIN = "/ulogin";
    public static final String USER_WS_LOGOUT = "/logout/{username}";
    public static final String USER_WS_USERS = "/users";
    public static final String USER_WS_USERS_PAGE_NO = "/users/{pageno}";
    public static final String USER_WS_MAIL = "/mail";
    public static final String USER_WS_ROLE = "/role";
    public static final String USER_WS_UPDATE_ID = "/update/{userId}";
    public static final String USER_WS_PASSWORD_UPDATE_ID = "/password/{userId}";
    public static final String USER_WS_PASSWORD = "/password";
    public static final String USER_WS_USER_ID = "/info/{userId}";
    public static final String SUBSCRIPTIONS_WS = "/subscriptions";
    public static final String SUBSCRIBERS_WS = "/subscribers";
    public static final String UNSUBSCRIBE_WS = "/unsubscribe";
    public static final String COMMUNICATIONS_WS = "/communications/{userId}";
    public static final String U_MESSAGES_WS = "/messages/{userId}";
    public static final String U_NOTIFICATIONS_WS = "/notifications/{userId}";
    public static final String ENABLE_NOTIFICATION_WS = "/notification";

    public static final String USER_ADD_SUBSCRIPTION_WS = "/subscriptions/add";
    public static final String USER_ADD_SUBSCRIBER_WS = "/subscribers/add";

    public static final String USER_SUBSCRIPTION_WS = "/subscriptions/{userId}";
    public static final String USER_SUBSCRIBER_WS = "/subscribers/{userId}";

    /************ ADMIN REQUEST*************/
    public static final String COMMUNICATION_WS = "/ws/dashboard/communication/*";


    /************ ANNOUNCE REQUEST*************/
    public static final String ANNOUNCE_WS = "/ws/announce/*";
    public static final String ANNOUNCES_WS = "/announces";
    public static final String ANNOUNCE_WS_USER_ID_PAGE_NO = "/announces/{pageno}";
    public static final String ANNOUNCE_WS_BY_ID = "/announce";
    public static final String ANNOUNCE_WS_BY_TYPE = "/type";
    public static final String ANNOUNCE_WS_ALL = "/all";
    public static final String UPDATE_ID = "/update/{id}";


    /************ MESSAGE REQUEST*************/
    public static final String MESSAGE_WS = "/ws/message/*";
    public static final String MESSAGES_WS = "/messages";

    public static final String MESSAGE_WS_BY_USER = "/user";
    public static final String MESSAGE_WS_FIND = "/find";
    public static final String MESSAGE_WS_UPDATE = UPDATE + "/{id}";
    public static final String MESSAGE_WS_ALL = "/all";


    /************ ROLE REQUEST*************/
    public static final String ROLE_WS = "/ws/role/*";
    public static final String ROLE_WS_ADD = "/add";
    public static final String ROLES_WS = "/roles";
    public static final String ROLE_WS_USER_ID_PAGE_NO = "/messages/{pageno}";
    public static final String ROLE_WS_DELETE = "/delete";
    public static final String ROLE_WS_UPDATE = "/update";


    /************ REVIEW REQUEST*************/
    public static final String REVIEW_WS = "/ws/review/*";
    public static final String REVIEW_WS_ADD = "/add";
    public static final String REVIEWS_WS = "/reviews";
    public static final String REVIEW_WS_DELETE = "/delete";
    public static final String REVIEW_WS_UPDATE = "/update/{id}";


    /************ NOTIFICATION REQUEST*************/
    public static final String NOTIFICATION_WS = "/ws/notification/*";

    /************ SOCKET NOTIFICATION REQUEST*************/
    public static final String SOCKET_NOTIFICATION_WS = "/ws/socket/notification/*";
    public static final String SUBSCRIPTIONS_SOCKET_WS = "/subcriptions-socket";



    /********** MAIL REQUEST***************/
    public static final String MAIL_WS = "/ws/mail/*";
    public static final String CONTACT_US_MAIL_WS = "/contactus";


    /******** RESERVATION REQUEST ********/
    public static final String RESERVATION_WS = "/ws/reservation/*";
    public static final String RESERVATION_WS_BY_ANNOUNCE = "/announce";
    public static final String RESERVATION_WS_UPDATE_ID = "/update/{id}";
    public static final String RESERVATION_WS_BY_ID = "/reservation";
    public static final String RESERVATION_WS_CREATED = "/user/own";
    public static final String RESERVATION_WS_RECEIVED = "/user/received";


    /******** IMG REQUEST ********/
    public static final String IMG_WS = "/ws/image/*";
}
