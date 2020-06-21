package cm.packagemanager.pmanager.ws.responses;

public class WebServiceResponseCode
{
	public static final long ID_PARTENAIRE = 120;
	public static final int CANCEL_STATE = 0;
	public static final int CANCEL_DATA = 1;
	public static final int OK_CODE = 0;
	public static final int INTERNAL_SERVER_ERROR_CODE = -1;
	public static final int ERROR_DELETE_USER_CODE = -10;
	public static final int DELETE_USER_CODE_OK = 10;
	public static final int CANNOT_UPDATE_INTERVENTION_CODE = -20;
	public static final int CANNOT_UPDATE_INTERVENTION_FROM_TO_CODE = -21;
	public static final int CANNOT_CANCEL_INTERVENTION_CODE = -30;
	public static final int CANNOT_CANCEL_INTERVENTION_INCONSISTENT_INTERVENTION_STATUS_CODE = -31;
	public static final int LATE_UPDATE_CODE = -17;
	public static final int DATE_FORMAT_ERROR_CODE = -18;
	public static final int ERROR_MODEL_VALUE_INTERVENTION_CODE = -4;
	public static final int ERROR_UPDATE_INTERVENTION_TYPE_CODE =-5;
	public static final int ERROR_CANCEL_INTERVENTION_TIMESTAMP_CODE =-15;
	public static final int MAX_COUNTER_VALUE = 10;
	 
	
	

	public static final String ERROR_DELETE_USER_CODE_LABEL = "L'utilisateur ne peut pas etre eliminé ccar n'existe plus";
	public static final String INTERVENTION_DA_PIANNIFICARE_LABEL = "Intervento da pianificare";
	public static final String UPDATE_INTERVENTION_SUCCESS_LABEL = "Intervento aggiornato correttamente";
	public static final String CANNOT_UPDATE_INTERVENTION_CODE_LABEL = "[Dati non compatibili] - Intervento non modificabile";
	public static final String CANNOT_UPDATE_INTERVENTION_FROM_TO_CODE_LABEL = "[Errore Stato ] - Intervento non modificabile causa del suo stato";
	public static final String CANNOT_CANCEL_INTERVENTION_CODE_LABEL = "[Dati non compatibili] - Dati non compatibili con l’operazioni di cancelS30Intervention";
	public static final String CANNOT_CANCEL_INTERVENTION_INCONSISTENT_INTERVENTION_STATUS_CODE_LABEL = "[Error Stato] - Intervento non cancellabile causa del suo Stato.";
	public static final String INTERVENTION_CANCELLATO_LABEL = "Intervento Cancellato";
	public static final String ERROR_CANCEL_INTERVENTION_TIMESTAMP_LABEL ="Timestamp della data non valido";
	public static final String ERROR_ADD_INTERVENTION_LABEL ="Errore aggiunta intervento";

}
