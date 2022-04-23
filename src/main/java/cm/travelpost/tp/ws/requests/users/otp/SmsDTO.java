package cm.travelpost.tp.ws.requests.users.otp;

import javax.validation.constraints.NotNull;

public class SmsDTO {

	@NotNull
	private String to;


	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
}
