package cm.travelpost.tp.ws.requests.announces;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class CommonReservationDTO extends CategoriesDTO {

    @NotNull(message = "L'utilisateur doit etre valorisé")
    private Long userId;

    @NotNull(message = "Le poids doit etre doit etre valorisé")
    @Positive(message = "Le poids doit etre doit etre valorisé")
    private BigDecimal weight;

    private BigDecimal estimateValue;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getEstimateValue() {  return estimateValue;  }

    public void setEstimateValue(BigDecimal estimateValue) { this.estimateValue = estimateValue; }


}
