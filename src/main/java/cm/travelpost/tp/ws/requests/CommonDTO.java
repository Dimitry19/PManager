package cm.travelpost.tp.ws.requests;

import cm.travelpost.tp.administrator.ent.enums.DashBoardObjectType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public abstract class CommonDTO {

	@NotNull(message = "Le code de la compagnie doit etre valorisé")
	@NotBlank(message = "Le code de la compagnie doit etre valorisé")
	private String code;

	@NotNull(message = "Le nom / description  doit etre valorisé(e)")
	private String name;

	@NotNull(message = " Valoriser le type d'element (CITY/AIRLINE)")
	@Enumerated(EnumType.STRING)
	private DashBoardObjectType objectType;

	public DashBoardObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(DashBoardObjectType objectType) {
		this.objectType = objectType;
	}

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
