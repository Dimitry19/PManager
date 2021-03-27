package cm.packagemanager.pmanager.ws.requests.review;


import javax.validation.constraints.NotNull;

public class UpdateReviewDTO extends ReviewDTO {

	@NotNull(message = "L'id de l'avis doit être valorisé")
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
