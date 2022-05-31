package cm.travelpost.tp.pricing.ent.vo;

import cm.framework.ds.common.ent.vo.CommonVO;
import cm.travelpost.tp.configuration.filters.FilterConstants;
import cm.travelpost.tp.constant.FieldConstants;
import cm.travelpost.tp.pricing.enums.SubscriptionPricingType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "PricingVO")
@Table(name = "pricing")
@NamedQueries(value = {
        @NamedQuery(name =PricingVO.FINDBYPRICE,query = "select p from PricingVO p where p.price=:price"),
        @NamedQuery(name =PricingVO.FINDBYTYPE,query = "select p from PricingVO p where p.type=:type")
        })
@Where(clause = FilterConstants.FILTER_PRICING_CANC)
public class PricingVO  extends CommonVO {

    public final static String FINDBYPRICE="cm.travelpost.tp.pricing.ent.vo.PricingVO.findByPrice";
    public final static String FINDBYTYPE = "cm.travelpost.tp.pricing.ent.vo.PricingVO.findByType";

    private PricingSubscriptionVOId id;
    private BigDecimal price;

    private SubscriptionPricingType type;
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

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false,unique = true,length = FieldConstants.ENUM_LEN)
    public SubscriptionPricingType getType() {
        return type;
    }

    @OneToMany
    @JoinColumns({@JoinColumn(name = "R_PRICING_CODE"),@JoinColumn(name ="R_PRICING_TOKEN")})
    @JsonManagedReference
    public Set<SubscriptionVO> getSubscriptions() {
        return subscriptions;
    }

    public void setId(PricingSubscriptionVOId id) { this.id = id; }

    public void setPrice(BigDecimal price) { this.price = price; }

    public void setType(SubscriptionPricingType type) { this.type = type; }

    public void setSubscriptions(Set<SubscriptionVO> subscriptions) { this.subscriptions = subscriptions; }

    public void addSubscription(SubscriptionVO subscription) {
        this.subscriptions.add(subscription);
        subscription.setPricing(this);
    }
    public void removeSubscription(SubscriptionVO subscription) {
        subscription.setPricing(null);
        this.subscriptions.remove(subscription);
    }

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
