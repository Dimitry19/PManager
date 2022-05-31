package cm.travelpost.tp.ws.requests.pricing;

import cm.travelpost.tp.common.utils.DateUtils;
import cm.travelpost.tp.pricing.enums.SubscriptionPricingType;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class CreateSubscriptionDTO {

    @NotNull(message = "La description doit etre valorisée")
    protected String description;

    @NotNull(message = "La date de debut de validité  doit etre valorisée")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = DateUtils.FORMAT_STD_PATTERN_4)
    @JsonFormat(pattern = DateUtils.FORMAT_STD_PATTERN_4)
    protected long startDate;

    @NotNull(message = "La date de fin de validité  doit etre valorisée")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = DateUtils.FORMAT_STD_PATTERN_4)
    @JsonFormat(pattern = DateUtils.FORMAT_STD_PATTERN_4)
    protected long endDate;
    @NotNull(message = "Le type d'abonnement doit etre valorisé")
    @Enumerated(EnumType.STRING)
    protected SubscriptionPricingType type;

    public String getDescription() {
        return description;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public SubscriptionPricingType getType() {
        return type;
    }

    public void setDescription(String description) { this.description = description;}

    public void setStartDate(long startDate) { this.startDate = startDate;  }

    public void setEndDate(long endDate) {  this.endDate = endDate; }

    public void setType(SubscriptionPricingType type) { this.type = type;  }

    public Date getConvertedStartDate() {
        return DateUtils.milliSecondToDate(this.startDate);
    }

    public Date getConvertedEndDate() {
        return DateUtils.milliSecondToDate(this.endDate);
    }

}
