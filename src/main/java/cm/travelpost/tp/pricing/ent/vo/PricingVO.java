package cm.travelpost.tp.pricing.ent.vo;

import cm.framework.ds.common.ent.vo.CommonVO;
import cm.travelpost.tp.configuration.filters.FilterConstants;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "PricingVO")
@Table(name = "pricing")
@NamedQueries(value = {
        @NamedQuery(name =PricingVO.FINDBYPRICE,query = "select p from PricingVO p where p.price=:price")
        })
@Where(clause = FilterConstants.FILTER_PRICING_CANC)
public class PricingVO  extends CommonVO {

    public final static String FINDBYPRICE="cm.travelpost.tp.pricing.ent.vo.findByPrice";

    private PricingSubscriptionVOId id;
    private BigDecimal price;
    private Set<SubscriptionVO> subscriptions = new HashSet();

    public PricingVO() {
        super();
    }

    public PricingVO(String code, String token) {
        this.id= new PricingSubscriptionVOId(code,token);
    }

    @EmbeddedId
    public PricingSubscriptionVOId getId(){
        return id;
    }

    @Basic(optional = false)
    @Column(name = "PRICE", nullable = false, unique = true)
    public BigDecimal getPrice() {
        return price;
    }

    @OneToMany
    @JoinColumns({@JoinColumn(name = "R_PRICING_CODE"),@JoinColumn(name ="R_PRICING_TOKEN")})
    @JsonManagedReference
    public Set<SubscriptionVO> getSubscriptions() {
        return subscriptions;
    }

    public void setId(PricingSubscriptionVOId id) { this.id = id; }

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
