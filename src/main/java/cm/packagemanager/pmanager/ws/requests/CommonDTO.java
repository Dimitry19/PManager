package cm.packagemanager.pmanager.ws.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public abstract class CommonDTO {

	@NotNull(message = "Le code de la compagnie doit etre valorisé")
	@NotBlank(message = "Le code de la compagnie doit etre valorisé")
	private String code;


	@NotNull(message = "Le nom / description  doit etre valorisé(e)")
	private String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
