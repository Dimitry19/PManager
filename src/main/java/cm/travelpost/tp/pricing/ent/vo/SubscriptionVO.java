package cm.travelpost.tp.pricing.ent.vo;

import cm.framework.ds.common.ent.vo.CommonVO;
import cm.travelpost.tp.common.utils.DateUtils;
import cm.travelpost.tp.constant.FieldConstants;
import cm.travelpost.tp.user.ent.vo.UserVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "pricing_subscription")
public class SubscriptionVO extends CommonVO {

    protected PricingSubscriptionEmbeddedId id;
    protected String description;
    protected Date startDate;
    protected Date endDate;
    protected Set<UserVO> users= new HashSet<>();

    public SubscriptionVO() {
        super();
    }

    @EmbeddedId
    public PricingSubscriptionEmbeddedId getId(){
        return this.id;
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


    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY )
    @JoinTable(name = "tp_user_pricing_subscriptions", joinColumns = {@JoinColumn(name = "R_SUBSCRIPTION_CODE"),@JoinColumn(name ="R_SUBSCRIPTION_TOKEN")} , inverseJoinColumns = @JoinColumn(name = "R_USER_ID"))
    public Set<UserVO> getUsers() {
        return users;
    }

    public void setId(PricingSubscriptionEmbeddedId id) { this.id = id; }

    public void setDescription(String description) { this.description = description; }

    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public void setUsers(Set<UserVO> users) { this.users = users; }


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
        if (startDate.equals(other.startDate))
            return false;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }
}
