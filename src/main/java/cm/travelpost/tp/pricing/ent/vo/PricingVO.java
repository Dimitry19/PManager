package cm.travelpost.tp.pricing.ent.vo;

import cm.framework.ds.common.ent.vo.CommonVO;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pricing")
public class PricingVO  extends CommonVO {

    private PricingSubscriptionEmbeddedId id;
    private BigDecimal price;
    private Set<SubscriptionVO> subscriptions = new HashSet();

    public PricingVO() {
        super();
    }

    @EmbeddedId
    public PricingSubscriptionEmbeddedId getId(){
        return id;
    }

    @Basic(optional = false)
    @Column(name = "PRICE", nullable = false)
    public BigDecimal getPrice() {
        return price;
    }

    @OneToMany
    @JoinColumns({@JoinColumn(name = "R_PRICING_CODE"),@JoinColumn(name ="R_PRICING_TOKEN")})
    @JsonManagedReference
    public Set<SubscriptionVO> getSubscriptions() {
        return subscriptions;
    }

    public void setId(PricingSubscriptionEmbeddedId id) { this.id = id; }

    public void setPrice(BigDecimal price) { this.price = price; }

    public void setSubscriptions(Set<SubscriptionVO> subscriptions) { this.subscriptions = subscriptions; }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id.hashCode();
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PricingVO other = (PricingVO) obj;
        if (!id.equals(other.id))
            return false;
        if (price == null) {
            return other.price == null;
        } else return price.equals(other.price);
    }
}
