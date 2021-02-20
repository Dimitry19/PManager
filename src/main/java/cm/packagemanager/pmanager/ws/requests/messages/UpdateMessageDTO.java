package cm.packagemanager.pmanager.ws.requests.messages;

import cm.packagemanager.pmanager.ws.requests.announces.AnnounceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMessageDTO extends MessageDTO {

	@NotNull(message = "L'id du message doit etre valorisé")
	private Long id;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
