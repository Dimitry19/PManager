package cm.packagemanager.pmanager.ws.responses;

public class WebServiceResponseCode
{
	public static final int OK_CODE = 0;
	public static final int NOK_CODE = -1;


	 
	// OK LABEL
	public static final String MAIL_SENT_LABEL = "Le mail envoye correttement";
	public static final String CANCELLED_USER_LABEL = "L'utilisateur a ete elimine correctement";
	public static final String RETRIVEVE_PASSWORD_LABEL = "Le mot de posse a ete envoye correctement...";
	public static final String LOGIN_OK_LABEL = "Login effectue correctement";
	public static final String USER_REGISTER_LABEL = "Enregistrement complete, consultez votre email pour confirmer votre enregistrement";
	public static final String USER_REGISTER_ACTIVE_LABEL = "Votre compte a ete active.. effectuez le login";


	public static final String ANNOUNCE_CREATE_LABEL="Annonce creee correctement";
	public static final String CANCELLED_ANNOUNCE_LABEL = "L'annonce a ete elimine correctement";
	public static final String UPDATED_ANNOUNCE_LABEL = "L'annonce a ete ajourne correctement";


	public static final String MESSAGE_CREATE_LABEL="commentaire creee correctement";
	public static final String CANCELLED_MESSAGE_LABEL = "Le commentaire a ete elimine correctement";
	public static final String UPDATED_MESSAGE_LABEL = "Le commentaire  a ete ajourne correctement";

	public static final String REVIEW_CREATE_LABEL=" Avis cree correctement";
	public static final String CANCELLED_REVIEW_LABEL = "L'avis a ete elimine correctement";
	public static final String UPDATED_REVIEW_LABEL = "L'avis  a ete ajourne correctement";
	public static final String SUBSCRIBE_LABEL = "Abonnement reussi";
	public static final String UNSUBSCRIBE_LABEL = "Desabonnement reussi";
	public static final String UPDATE_PASSWORD_LABEL = "Mot de passe ajourné correctement";

	public static final String CONTACT_US_LABEL = "Merci pour votre correspondance, vous serez recontacté";


	public static final String RESERV_CREATE_LABEL="Reservation creee correctement";
	public static final String UPDATED_RESERV_LABEL = "La reservation ete modifiée correctement";
	public static final String CANCELLED_RESERV_OK_LABEL = "Votre reservation a été correctement effacé";


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

	public static final String ERROR_ANNOUNCE_CREATE_LABEL = "Annonce n'a pas pu etre cree";
	public static final String ERROR_DELETE_ANNOUNCE_CODE_LABEL = "L' annonce ne peut pas etre eliminee car inexistant";
	public static final String ERROR_UPDATE_ANNOUNCE_CODE_LABEL = "L' annonce ne peut pas etre ajournee car inexistant";


	public static final String ERROR_MESSAGE_CREATE_LABEL = "Commentaire n'a pas pu etre cree";
	public static final String ERROR_DELETE_MESSAGE_CODE_LABEL = "Le Commentaire ne peut pas etre elimine car inexistant";
	public static final String ERROR_UPDATE_MESSAGE_CODE_LABEL = "Le Commentaire ne peut pas etre ajourne car inexistant";


	public static final String ERROR_REVIEW_CREATE_LABEL = " L'avis n'a pas pu etre cree";
	public static final String ERROR_DELETE_REVIEW_CODE_LABEL = "L'avis ne peut pas etre elimine car inexistant";
	public static final String ERROR_UPDATE_REVIEW_CODE_LABEL = "L'avis ne peut pas etre ajourne car inexistant";


	public static final String ERROR_SUBSCRIBE_LABEL = "Abonnement non reussi";
	public static final String ERROR_UNSUBSCRIBE_LABEL = "Desabonnement non reussi";
	public static final String CONFLICT_SUBSCRIBE_LABEL = "Impossible de/s'abonner a son propre compte";


	public static final String ERROR_CONTACT_US_LABEL = "Impossible d'envoyer le message pour le moment";


	public static final String ERROR_RESERV_CREATE_LABEL = "La reservation n'a pas pu etre cree";
	public static final String ERROR_DELETE_RESERV_CODE_LABEL = "La reservation ne peut pas etre eliminee car inexistant";
	public static final String ERROR_UPDATE_RESERV_CODE_LABEL = "La reservation ne peut pas etre ajournee car inexistant";
	public static final String ERROR_GET_RESERV_CODE_LABEL = "La reservation inexistante";

}
