package cm.travelpost.tp.ws.requests.pricing;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class UpdatePricingDTO {

    @Positive(message = "Le montant doit etre une valeur positive")
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
