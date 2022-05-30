package cm.travelpost.tp.pricing.ent.vo;

import cm.framework.ds.common.ent.vo.CommonVO;
import cm.travelpost.tp.common.utils.DateUtils;
import cm.travelpost.tp.constant.FieldConstants;
import cm.travelpost.tp.pricing.enums.PricingSubscriptionType;
import cm.travelpost.tp.user.ent.vo.UserVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity(name = "SubscriptionVO")
@Table(name = "subscription_pricing")
public class SubscriptionVO extends CommonVO {

    protected PricingSubscriptionVOId id;
    protected PricingSubscriptionType type;
    protected String description;
    protected Date startDate;
    protected Date endDate;
    protected Set<UserVO> users= new HashSet<>();
    protected PricingVO pricing;


    public SubscriptionVO() {
        super();
    }

    @EmbeddedId
    public PricingSubscriptionVOId getId(){
        return this.id;
    }

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false,length = FieldConstants.ENUM_LEN)
    public PricingSubscriptionType getType() {
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

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "R_PRICING_CODE", referencedColumnName = "CODE"),
            @JoinColumn(name = "R_PRICING_TOKEN", referencedColumnName = "TOKEN")
    })
    public PricingVO getPricing() {
        return pricing;
    }

    public void setId(PricingSubscriptionVOId id) { this.id = id; }

    public void setType(PricingSubscriptionType type) { this.type = type; }

    public void setDescription(String description) { this.description = description; }

    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public void setUsers(Set<UserVO> users) { this.users = users; }

    public void setPricing(PricingVO pricing) {this.pricing = pricing; }

    public void addUser(UserVO user) {
        this.users.add(user);
        user.setSubscription(this);
    }
    public void removeAnnounce(UserVO user) {
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
