package cm.travelpost.tp.ws.requests.users.otp;

import javax.validation.constraints.Positive;

public class OTPNumberDTO {

	@Positive
	private int otp;

	public int getOtp() {
		return otp;
	}

	public void setOtp(int otp) {
		this.otp = otp;
	}
}
