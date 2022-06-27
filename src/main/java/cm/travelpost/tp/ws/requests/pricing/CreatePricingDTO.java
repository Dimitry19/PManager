package cm.travelpost.tp.ws.requests.pricing;

import cm.travelpost.tp.pricing.enums.SubscriptionPricingType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class CreatePricingDTO {

    @PositiveOrZero(message = "Le prix doit etre positif")
    protected BigDecimal amount;
    @NotNull(message = "Le type d'abonnement doit etre valorisé")
    @Enumerated(EnumType.STRING)
    private SubscriptionPricingType type;

    public BigDecimal getAmount() {
        return amount;
    }

    public SubscriptionPricingType getType() {
        return type;
    }
    public void setAmount(BigDecimal amount) {this.amount = amount;    }

    public void setType(SubscriptionPricingType type) { this.type = type;  }
}
