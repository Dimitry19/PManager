package cm.packagemanager.pmanager.ws.requests.users;

import cm.packagemanager.pmanager.common.enums.RoleEnum;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

public class RoleDTO {

	@NotNull(message = "role should not be empty")
	@Enumerated(EnumType.STRING)
	private RoleEnum role;



	public RoleEnum getRole() {
		return role;
	}

	public void setRole(RoleEnum role) {
		this.role = role;
	}


}