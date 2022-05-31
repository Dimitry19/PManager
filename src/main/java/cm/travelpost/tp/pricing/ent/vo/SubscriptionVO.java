package cm.travelpost.tp.pricing.ent.vo;

import cm.framework.ds.common.ent.vo.CommonVO;
import cm.travelpost.tp.common.utils.DateUtils;
import cm.travelpost.tp.configuration.filters.FilterConstants;
import cm.travelpost.tp.constant.FieldConstants;
import cm.travelpost.tp.pricing.enums.SubscriptionPricingType;
import cm.travelpost.tp.user.ent.vo.UserVO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity(name = "SubscriptionVO")
@Table(name = "subscription_pricing")
@NamedQueries(value = {
        @NamedQuery(name =SubscriptionVO.FINDBYTYPE,query = "select s from SubscriptionVO s where s.type=:type")
})
@Where(clause = FilterConstants.FILTER_SUBSCRIPTION_CANC)
public class SubscriptionVO extends CommonVO {


    public final static String FINDBYTYPE = "cm.travelpost.tp.pricing.ent.vo.SubscriptionVO.findByType";

    private PricingSubscriptionVOId id;
    private SubscriptionPricingType type;
    private String description;
    private Date startDate;
    private Date endDate;
    private Set<UserVO> users= new HashSet<>();
    private PricingVO pricing;


    public SubscriptionVO() {
        super();
    }

    public SubscriptionVO(String code, String token) {
        this.id= new PricingSubscriptionVOId(code,token);
    }

    @EmbeddedId
    public PricingSubscriptionVOId getId(){
        return this.id;
    }

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false,length = FieldConstants.ENUM_LEN)
    public SubscriptionPricingType getType() {
        return type;
    }

    @Basic(optional = false)
    @Column(name = "DESCRIPTION", nullable = false, length = FieldConstants.DESC)
    public String getDescription() {
        return description;
    }

    @Basic(optional = false)
    @Column(name = "START_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = DateUtils.STD_PATTERN)
    public Date getStartDate() {
        return startDate;
    }

    @Basic(optional = false)
    @Column(name = "END_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = DateUtils.STD_PATTERN)
    public Date getEndDate() {
        return endDate;
    }

    @Access(AccessType.PROPERTY)
    @JsonIgnore
    @OneToMany(mappedBy = "subscription", fetch = FetchType.LAZY )
    public Set<UserVO> getUsers() {
        return users;
    }

    @JsonBackReference
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "R_PRICING_CODE", referencedColumnName = "CODE", updatable = true, insertable = false),
            @JoinColumn(name = "R_PRICING_TOKEN", referencedColumnName = "TOKEN", updatable = true,insertable = false)
    })
    public PricingVO getPricing() {
        return pricing;
    }

    public void setId(PricingSubscriptionVOId id) { this.id = id; }

    public void setType(SubscriptionPricingType type) { this.type = type; }

    public void setDescription(String description) { this.description = description; }

    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public void setUsers(Set<UserVO> users) { this.users = users; }

    public void setPricing(PricingVO pricing) {this.pricing = pricing; }

    public void addUser(UserVO user) {
        this.users.add(user);
        user.setSubscription(this);
    }
    public void removeUser(UserVO user) {
        user.setSubscription(null);
        this.users.remove(user);
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
        SubscriptionVO other = (SubscriptionVO) obj;
        if (!startDate.equals(other.startDate))
            return false;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }
}
