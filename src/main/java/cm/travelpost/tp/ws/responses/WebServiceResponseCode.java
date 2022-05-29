package cm.travelpost.tp.ws.responses;

public class WebServiceResponseCode {
    public static final int LOGIN_MFA_ENABLED = 8;
    public static final int QR_CODE_MFA_ENABLED = 9;
    public static final int SHARE = 7;
    public static final int OK_CODE = 0;
    public static final int NOK_CODE = -1;


    public static final String MAIL_SPAM=".Vérifier aussi parmi les spams";
    // OK LABEL
    public static final String MAIL_SENT_LABEL = "Le mail envoyé correttement" +MAIL_SPAM;
    public static final String CANCELLED_USER_LABEL = "L'utilisateur a été correctement éliminé ";
    public static final String RETRIVEVE_PASSWORD_LABEL = "Le mot de passe a été correctement envoyé ..."+MAIL_SPAM;
    public static final String LOGIN_OK_LABEL = "Login effectue correctement";
    public static final String LOGOUT_OK_LABEL = "Logout effectue correctement";
    public static final String USER_REGISTER_MAIL_LABEL = "Enregistrement completé, consultez votre email pour confirmer votre enregistrement"+MAIL_SPAM;
    public static final String USER_REGISTER_LABEL = "Enregistrement completé! Vous pouvez vous connecter";
    public static final String QRCODE_LABEL = "Scannez le QrCode avec l'app mobile Google Authenticator / Microsoft Authenticator / Authy pour vous connecter";

    public static final String UPDATE_LABEL = "Données ajournées correctement";
    public static final String PAGINATE_RESPONSE_LABEL = "{0} resultat(s) trouvé(s)";
    public static final String PAGINATE_EMPTY_RESPONSE_LABEL = "Aucun resultat trouvé";

    public static final String CREATE_LABEL = "{0} correctement inserée";
    public static final String CANCELLED_LABEL = "{0} correctement éliminé(e) ";
    public static final String UPDATED_LABEL = "{0}  correctement ajourné(e) ";

    public static final String SUBSCRIBE_LABEL = "Abonnement reussi";
    public static final String UNSUBSCRIBE_LABEL = "Désabonnement reussi";
    public static final String UPDATE_PASSWORD_LABEL = "Mot de passe ajourné correctement";

    public static final String CONTACT_US_LABEL = "Merci pour votre correspondance, vous serez recontacté";
    public static final String UPDATED_RESERV_LABEL = "La reservation été correctement {0}";

    public static final String READ_CODE_LABEL = "{0} déjà lu(e)";
    public static final String READ_ALL_CODE_LABEL = " Notifications lues";

    public static final String MFA_ENABLE_OK_LABEL = "L'authentification à deux facteurs activée ";
    public static final String MFA_DISABLE_OK_LABEL = "L'authentification à deux facteurs desactivée ";

    public static final String NOTIFICATION_ENABLE_OK_LABEL = "Notification activée ";
    public static final String NOTIFICATION_DISABLE_OK_LABEL = "Notification desactivée ";




    // NOK LABEL
    public static final String ERROR_PAGINATE_RESPONSE_LABEL = "Une erreur est survenue durant la recuperation des données";
    public static final String ERROR_DELETE_USER_CODE_LABEL = "L'utilisateur ne peut pas etre elimine car inexistant";
    public static final String ERROR_MAIL_SENT_LABEL = "Le mail n'a pas pu etre envoye";
    public static final String ERROR_RETRIEVE_PASSWORD_LABEL = "Le mot de posse n'a pas été envoye ";
    public static final String ERROR_EMAIL_REGISTER_LABEL = "Cette adresse email a deja été utilise";
    public static final String ERROR_USERNAME_REGISTER_LABEL = "Nom utilisateur non disponible";
    public static final String ERROR_USER_REGISTER_LABEL = "Enregistrement non complété";
    public static final String ERROR_INVALID_TOKEN_REGISTER_LABEL = "Token invalide , peut etre le lien a deja expire";
    public static final String ERROR_USED_TOKEN_REGISTER_LABEL = "Le compte a deje ete active";
    public static final String ERROR_LOGIN_LABEL = "Verifier les données d'accès";
    public static final String ERROR_LOGIN_ATTEMPT_LABEL = "Verifier les données d\''accès il vous reste {0} tentative(s) sur {1}";
    public static final String ERROR_LOGIN_ATTEMPT_KO_LABEL = "Les {0} tentatives de connexion epuisées.Veuillez contacter l\''administrateur à l\'' adresse : {1} ";
    public static final String ERROR_LOGOUT_LABEL = "Erreur durant la déconnexion";

    public static final String ERROR_UPDATE_USER_LABEL = "Erreur durant l'ajournement des données";

    public static final String ERROR_UPD_EMAIL_LABEL = "Email non valide ou deja utilise";

    public static final String ERROR_CREATE_LABEL = "{0} n'a pas pu etre cree";
    public static final String ERROR_DELETE_LABEL = "{0} ne peut pas etre eliminé(e) car inexistant";
    public static final String ERROR_UPDATE_LABEL = "{0} ne peut pas etre ajournee car inexistant";


    public static final String ERROR_SUBSCRIBE_LABEL = "Abonnement non reussi";
    public static final String ERROR_UNSUBSCRIBE_LABEL = "Desabonnement non reussi";
    public static final String CONFLICT_SUBSCRIBE_LABEL = "Impossible de/s'abonner a son propre compte";


    public static final String ERROR_CONTACT_US_LABEL = "Impossible d'envoyer le message pour le moment";
    public static final String ERROR_GET_RESERV_CODE_LABEL = "La reservation inexistante";
    public static final String ERROR_INEXIST_CODE_LABEL = "{0} inexistant(e)";

    public static final String ERROR_MAIL_SERVICE_UNAVAILABLE_LABEL = "Service envoi mail actuellement indisponible.{0} ";

    public static final String ERROR_INVALID_CODE_LABEL = "Code invalide!";

    public static final String ERROR_USER_QRCODE_LABEL = "Erreur durant la generation du QrCode, verifiez les identifiants inserés";


}
