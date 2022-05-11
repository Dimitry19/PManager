package cm.travelpost.tp.ws.responses;

import lombok.Data;

@Data
public class RegistrationResponse  extends Response{


	private boolean mfa;
	private String secretImageUri;
}
