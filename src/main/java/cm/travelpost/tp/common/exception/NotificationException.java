package cm.travelpost.tp.common.exception;

public class NotificationException extends Exception{

	public NotificationException(String message) {
		super(message);
	}

	public NotificationException(Exception e) {
		super(e.getMessage());
	}

}
