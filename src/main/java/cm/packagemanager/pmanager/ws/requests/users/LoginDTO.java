package cm.packagemanager.pmanager.ws.requests.users;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginDTO {

	private String email;
	private String provider;
	private String password;
	private String socialId;

	@NotNull(message = "username should not be empty")
	@Size(min = 1, max = 10, message = "username  should be between 1 and 10 characters")
	private String username;


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
