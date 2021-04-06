package cm.packagemanager.pmanager.ws.requests.users;

import javax.validation.constraints.NotNull;

public class ManagePasswordDTO {


	@NotNull(message = "Valoriser l'ancien mot de passe")
	private String oldPassword;

	@NotNull(message = "Valoriser le nouveau mot de passe")
	private String newPassword;


	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
