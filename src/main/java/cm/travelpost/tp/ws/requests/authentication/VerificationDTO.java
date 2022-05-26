package cm.travelpost.tp.ws.requests.authentication;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class VerificationDTO {

	@NotNull(message = "Valorisez le nom utilisateur")
	private String username;
	@NotNull(message = "Valorisez le code de verification")
	private String code;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
