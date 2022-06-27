package cm.travelpost.tp.common.mail.mailjet;

public class CustomMailJetResponse {

	public static final int OK = 200;
	public static final int CREATED = 201;
	public static final int NO_CONTENT = 204;
	public static final int NOT_MODIFIED = 304;
	public static final int BAD_REQUEST = 400;
	public static final int UNAUTHORIZED = 401;
	public static final int FORBIDDEN = 403;
	public static final int NOT_FOUND = 404;
	public static final int METHOD_NOT_ALLOWED = 405;
	public static final int TOO_MANY_REQUESTS = 429;
	public static final int INTERNAL_SERVER_ERROR = 500;




	public static boolean managedResponse(int status){

		switch (status){
			case OK:

			case CREATED:

			case NO_CONTENT:
				return Boolean.TRUE;
			case NOT_MODIFIED:
			case INTERNAL_SERVER_ERROR:
			case BAD_REQUEST:
			case TOO_MANY_REQUESTS:
			case UNAUTHORIZED:
			case FORBIDDEN:
			case METHOD_NOT_ALLOWED:
				return Boolean.FALSE;
		}
		return Boolean.FALSE;
	}

}
