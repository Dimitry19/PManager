package cm.packagemanager.pmanager.ws.responses;

public class WebServiceResponseCode {
    public static final int OK_CODE = 0;
    public static final int NOK_CODE = -1;


    // OK LABEL
    public static final String MAIL_SENT_LABEL = "Le mail envoye correttement";
    public static final String CANCELLED_USER_LABEL = "L'utilisateur a ete elimine correctement";
    public static final String RETRIVEVE_PASSWORD_LABEL = "Le mot de posse a ete envoye correctement...";
    public static final String LOGIN_OK_LABEL = "Login effectue correctement";
    public static final String LOGOUT_OK_LABEL = "Logout effectue correctement";
    public static final String USER_REGISTER_LABEL = "Enregistrement complete, consultez votre email pour confirmer votre enregistrement";
    public static final String USER_REGISTER_ACTIVE_LABEL = "Votre compte a ete active.. effectuez le login";


    public static final String CREATE_LABEL = "{0} cree(e) correctement";
    public static final String CANCELLED_LABEL = "{0} a ete elimine correctement";
    public static final String UPDATED_LABEL = "{0}  a ete ajourne correctement";


    public static final String SUBSCRIBE_LABEL = "Abonnement reussi";
    public static final String UNSUBSCRIBE_LABEL = "Desabonnement reussi";
    public static final String UPDATE_PASSWORD_LABEL = "Mot de passe ajourné correctement";

    public static final String CONTACT_US_LABEL = "Merci pour votre correspondance, vous serez recontacté";
    public static final String UPDATED_RESERV_LABEL = "La reservation ete {0} correctement";


    // NOK LABEL
    public static final String ERROR_DELETE_USER_CODE_LABEL = "L'utilisateur ne peut pas etre elimine car inexistant";
    public static final String ERROR_MAIL_SENT_LABEL = "Le mail n'a pas pu etre envoye";
    public static final String ERROR_RETRIVEVE_PASSWORD_LABEL = "Le mot de posse n'a pas ete envoye ";
    public static final String ERROR_EMAIL_REGISTER_LABEL = "Cette adresse email a deja ete utilise";
    public static final String ERROR_USERNAME_REGISTER_LABEL = "Nom utilisateur non disponible";
    public static final String ERROR_USER_REGISTER_LABEL = "Enregistrement non complete";
    public static final String ERROR_INVALID_TOKEN_REGISTER_LABEL = "Token invalide , peut etre le lien a deja expire";
    public static final String ERROR_USED_TOKEN_REGISTER_LABEL = "Le compte a deje ete active";
    public static final String ERROR_LOGIN_LABEL = "Verifier les donn?es d'accès";

    public static final String ERROR_UPD_EMAIL_LABEL = "Email non valide ou deja utilise";

    public static final String ERROR_CREATE_LABEL = "{0} n'a pas pu etre cree";
    public static final String ERROR_DELETE_LABEL = "{0} ne peut pas etre eliminee car inexistant";
    public static final String ERROR_UPDATE_LABEL = "{0} ne peut pas etre ajournee car inexistant";


    public static final String ERROR_SUBSCRIBE_LABEL = "Abonnement non reussi";
    public static final String ERROR_UNSUBSCRIBE_LABEL = "Desabonnement non reussi";
    public static final String CONFLICT_SUBSCRIBE_LABEL = "Impossible de/s'abonner a son propre compte";


    public static final String ERROR_CONTACT_US_LABEL = "Impossible d'envoyer le message pour le moment";
    public static final String ERROR_GET_RESERV_CODE_LABEL = "La reservation inexistante";
    public static final String ERROR_INEXIST_CODE_LABEL = "{0} inexistant(e)";

}
