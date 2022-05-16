package cm.travelpost.tp.ws.responses;

import lombok.Data;

@Data
public class RegistrationResponse  extends Response{


	private boolean mfa;
	private String secretImageUri;

	public boolean isMfa() {
		return mfa;
	}

	public void setMfa(boolean mfa) {
		this.mfa = mfa;
	}

	public String getSecretImageUri() {
		return secretImageUri;
	}

	public void setSecretImageUri(String secretImageUri) {
		this.secretImageUri = secretImageUri;
	}
}
