package cm.packagemanager.pmanager.ws.requests.users;

import cm.packagemanager.pmanager.common.enums.RoleEnum;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

public class RoleToUserDTO {

	@NotNull(message = "username should not be empty")
	private String email;

	private String username;

	@NotNull(message = "role should not be empty")
	@Enumerated(EnumType.STRING)
	private RoleEnum role;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public RoleEnum getRole() {
		return role;
	}

	public void setRole(RoleEnum role) {
		this.role = role;
	}
}
