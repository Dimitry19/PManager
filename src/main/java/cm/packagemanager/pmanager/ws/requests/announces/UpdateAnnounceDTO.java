package cm.packagemanager.pmanager.ws.requests.announces;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAnnounceDTO extends AnnounceDTO {

	@NotNull(message = "L'id de l'annonce doit etre valorisé")
	private Long id;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
