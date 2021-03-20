package cm.packagemanager.pmanager.ws.requests.announces;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ReservationDTO {


	@NotNull(message = "L'utilisateur doit etre valorisé")
	private Long userId;

	@NotNull(message = "Le poids doit etre doit etre valorisé")
	private BigDecimal weight;

	@NotNull(message = "La categorie doit etre valorisée")
	private String category;

	@NotNull(message = "L'annonce doit etre valorisée")
	private Long announceId;

	private String description;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getAnnounceId() {
		return announceId;
	}

	public void setAnnounceId(Long announceId) {
		this.announceId = announceId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
