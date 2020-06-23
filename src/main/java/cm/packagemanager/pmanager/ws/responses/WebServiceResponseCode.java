package cm.packagemanager.pmanager.ws.responses;

public class WebServiceResponseCode
{
	public static final int OK_CODE = 0;
	public static final int NOK_CODE = -1;


	 
	// OK LABEL
	public static final String MAIL_SENT_LABEL = "Le mail envoyé correttement";
	public static final String CANCELLED_USER_LABEL = "[Dati non compatibili] - Intervento non modificabile";

	// NOK LABEL
	public static final String ERROR_DELETE_USER_CODE_LABEL = "L'utilisateur ne peut pas etre eliminé ccar n'existe plus";
	public static final String ERROR_MAIL_SENT_LABEL = "Le mail n'apas pu etre envoyé";


	public static final String INTERVENTION_DA_PIANNIFICARE_LABEL = "Intervento da pianificare";
	public static final String UPDATE_INTERVENTION_SUCCESS_LABEL = "Intervento aggiornato correttamente";
	public static final String CANNOT_UPDATE_INTERVENTION_FROM_TO_CODE_LABEL = "[Errore Stato ] - Intervento non modificabile causa del suo stato";
	public static final String CANNOT_CANCEL_INTERVENTION_CODE_LABEL = "[Dati non compatibili] - Dati non compatibili con l’operazioni di cancelS30Intervention";
	public static final String CANNOT_CANCEL_INTERVENTION_INCONSISTENT_INTERVENTION_STATUS_CODE_LABEL = "[Error Stato] - Intervento non cancellabile causa del suo Stato.";
	public static final String INTERVENTION_CANCELLATO_LABEL = "Intervento Cancellato";
	public static final String ERROR_CANCEL_INTERVENTION_TIMESTAMP_LABEL ="Timestamp della data non valido";
	public static final String ERROR_ADD_INTERVENTION_LABEL ="Errore aggiunta intervento";

}
