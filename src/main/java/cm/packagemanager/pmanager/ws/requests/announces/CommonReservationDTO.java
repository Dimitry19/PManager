package cm.packagemanager.pmanager.ws.requests.announces;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

public class CommonReservationDTO {

	@NotNull(message = "L'utilisateur doit etre valorisé")
	private Long userId;

	@NotNull(message = "Le poids doit etre doit etre valorisé")
	@Positive(message = "Le poids doit etre doit etre valorisé")
	private BigDecimal weight;

	@NotNull(message = "La categorie doit etre valorisée")
	private List<String> categories;

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

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
