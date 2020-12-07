package cm.packagemanager.pmanager.ws.requests.users;

import javax.validation.constraints.NotNull;

public class UpdateUserDTO extends RegisterDTO{


	@NotNull(message = "Id user should not be empty")
	private Long id;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
