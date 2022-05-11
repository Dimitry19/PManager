package cm.travelpost.tp.ws.requests.authentication;

import lombok.Data;

@Data
public class VerificationDTO {

	private String username;
	private String code;
}
