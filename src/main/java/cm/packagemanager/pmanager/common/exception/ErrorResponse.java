package cm.packagemanager.pmanager.common.exception;

import java.util.List;

public class ErrorResponse {

	private String message;
	private List<String> details;
	private  int code;


	public ErrorResponse() {
		super();

	}
	public ErrorResponse(String message, List<String> details) {
		super();
		this.message = message;
		this.details = details;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getDetails() {
		return details;
	}

	public void setDetails(List<String> details) {
		this.details = details;
	}
}
