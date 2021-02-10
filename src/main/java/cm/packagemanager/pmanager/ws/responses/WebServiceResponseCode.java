package cm.packagemanager.pmanager.ws.responses;

public class WebServiceResponseCode
{
	public static final int OK_CODE = 0;
	public static final int NOK_CODE = -1;


	 
	// OK LABEL
	public static final String MAIL_SENT_LABEL = "Le mail envoy? correttement";
	public static final String CANCELLED_USER_LABEL = "L'utilisateur a ?t? elimin? correctement";
	public static final String RETRIVEVE_PASSWORD_LABEL = "Le mot de posse a ?t? envoy? correctement ? <<";
	public static final String LOGIN_OK_LABEL = "Login effectu? correctement";
	public static final String USER_REGISTER_LABEL = "Enregistrement complet?, consultez votre email pour confirmer votre enregistrement";
	public static final String USER_REGISTER_ACTIVE_LABEL = "Votre compte a ?t? activ?.. effectuez le login";


	public static final String ANNOUNCE_CREATE_LABEL="Annonce cr?ee correctement";
	public static final String CANCELLED_ANNOUNCE_LABEL = "L'annonce a ?t? elimin? correctement";

	// NOK LABEL
	public static final String ERROR_DELETE_USER_CODE_LABEL = "L'utilisateur ne peut pas etre elimin? car inexistant";
	public static final String ERROR_MAIL_SENT_LABEL = "Le mail n'a pas pu etre envoy?";
	public static final String ERROR_RETRIVEVE_PASSWORD_LABEL = "Le mot de posse n'a pas ?t? envoy? ";
	public static final String ERROR_EMAIL_REGISTER_LABEL = "C'est email a deja ?t? utilis?";
	public static final String ERROR_USERNAME_REGISTER_LABEL = "Nom utilisateur non disponible";
	public static final String ERROR_USER_REGISTER_LABEL = "Enregistrement non complet?";
	public static final String ERROR_INVALID_TOKEN_REGISTER_LABEL = "Token invalide , peut etre le lien a deja expir?";
	public static final String ERROR_USED_TOKEN_REGISTER_LABEL = "Le compte a dej? ?t? activ?";
	public static final String ERROR_LOGIN_LABEL = "Verifier les donn?es d'acc?s";

	public static final String ERROR_UPD_EMAIL_LABEL = "Email non valide ou deja utilis?";

	public static final String ERROR_ANNOUNCE_CREATE_LABEL = "Annonce n'a pas pu etre cree";
	public static final String ERROR_DELETE_ANNOUNCE_CODE_LABEL = "L' annonce ne peut pas etre elimin?e car inexistant";
}
