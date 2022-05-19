package cm.travelpost.tp.constant;

import cm.travelpost.tp.common.properties.CommonProperties;

public class WSConstants extends CommonProperties {


    public static final String DEFAULT_SIZE = "12";
    public static final String DEFAULT_PAGE = "0";

    public static final String HEADER_ACCEPT = "Accept=application/json";



    public static final String BASE_PATTERN= "/ws/*";


    public static final String ONLY_SERVICE = "/services";
    public static final String DASHBOARD = "/dashboard";
    public static final String CREATE = "/create";
    public static final String ADD = "/add";
    public static final String UPDATE = "/update";
    public static final String DELETE = "/delete";
    public static final String BY_USER = "/user";
    public static final String BY_ANNOUNCE = "/announce";
    public static final String FIND = "/find";
    public static final String VALIDATE = "/validate";
    public static final String IMAGE = "/{imageName}";
    public static final String UPLOAD = "/upload";
    public static final String UPDATE_ID = "/update/{id}";
    public static final String AUTOCOMPLETE = "/autocomplete/{search}";

    public static final String REGISTRATION = "/register";
    public static final String LOGOUT = "/logout";

    /************ AUTHENTICATION REQUEST*************/
    public static final String AUTHENTICATION_WS = "/ws/authentication/*";
    public static final String AUTHENTICATION_WS_REGISTRATION = "/register";
    public static final String AUTHENTICATION_WS_VERIFICATION = "/verify";


    /************ USER REQUEST*************/
    public static final String USER_WS = "/ws/user/*";

    public static final String USER_WS_CONFIRMATION = "/confirm";
    public static final String USER_WS_LOGIN = "/authenticate";
    public static final String USER_WS_USERS = "/users";
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

    public static final String USER_WS_OTP = "/otp";


    /************ ANNOUNCE REQUEST*************/
    public static final String ANNOUNCE_WS = "/ws/announce/*";
    public static final String ANNOUNCES_WS = "/announces";
    public static final String ANNOUNCE_WS_USER_ID_PAGE_NO = "/announces/{pageno}";
    public static final String ANNOUNCE_WS_BY_ID_AND_SOURCE = "/announce";
    public static final String ANNOUNCE_WS_COMPLETED_BY_ID = "/announceCompleted";
    public static final String ANNOUNCE_WS_BY_TYPE = "/type";
    public static final String ANNOUNCE_WS_BY_TRANSPORT = "/transport";
    public static final String ANNOUNCE_WS_ALL = "/all";


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
    public static final String NOTIFICATIION_WS_BY_ID = "/notification";

    /************ SOCKET NOTIFICATION REQUEST*************/
    public static final String SOCKET_NOTIFICATION_WS = "/ws/socket/notification/*";
    public static final String SOCKET_NOTIFICATION_WS_BIS = "/swns/*";
    public static final String SUBSCRIPTIONS_SOCKET_WS = "/subcriptions-socket";



    /********** MAIL REQUEST***************/
    public static final String MAIL_WS = "/ws/mail/*";
    public static final String CONTACT_US_MAIL_WS = "/contactus";


    /******** RESERVATION REQUEST ********/
    public static final String RESERVATION_WS = "/ws/reservation/*";
    public static final String RESERVATION_WS_BY_ANNOUNCE = "/announce";
    public static final String RESERVATION_WS_UPDATE_ID = "/update/{id}";
    public static final String RESERVATION_WS_BY_ID = "/reservation";



    /******** IMG REQUEST ********/
    public static final String IMG_WS = "/ws/image/*";

    /******** IMG REQUEST ********/
    public static final String CITY_WS = "/ws/city/*";
    public static final String CITIES_WS = "/ws/city/cities";



    /************ ADMIN DASHBOARD REQUEST*************/
    public static final String DASHBOARD_WS = "/ws" + DASHBOARD+"/*";
    public static final String DASHBOARD_DELETE = "/delete/{code}";

    /*******************SMS TWILLO ******************/
    public static final String SMS_WS = "/ws/sms/*";
    public static final String SMS_SEND = "/send";
    public static final String TM_SMS_SEND = "/tmsend";
    public static final String SMS_CALLBACK = "/smscallback";



    /************ TOTP REQUEST*************/
    public static final String TOTP_WS = "/ws/totp/*";

}
