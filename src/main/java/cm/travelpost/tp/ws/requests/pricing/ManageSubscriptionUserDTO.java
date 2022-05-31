package cm.travelpost.tp.ws.requests.pricing;

import cm.travelpost.tp.pricing.enums.SubscriptionPricingType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.util.List;

public class ManageSubscriptionUserDTO {


    @NotNull(message = "Le code de l'abonnement doit etre valorisé")
    private String code;
    @NotNull(message = "Le token de l'abonnement doit etre valorisé")
    private String token;

    @NotNull(message = "Le type d'abonnement doit etre valorisé")
    @Enumerated(EnumType.STRING)
    protected SubscriptionPricingType type;

    @NotNull.List({@NotNull(groups=Long.class,message="Valoriser l\'id d\'au moins un utilisateur!"),@NotNull(groups=String.class,message="Valoriser l\'username d\'au moins un utilisateur")})
    private List users;



    public SubscriptionPricingType getType() {
        return type;
    }

    public List getUsers() {
        return users;
    }

    public String getToken() {
        return token;
    }

    public String getCode() {
        return code;
    }

    public void setToken(String token) {  this.token = token;    }

    public void setType(SubscriptionPricingType type) { this.type = type;  }

    public void setUsers(List users) {this.users = users;    }

    public void setCode(String code) {  this.code = code;  }
}
