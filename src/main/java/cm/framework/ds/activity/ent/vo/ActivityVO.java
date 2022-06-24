package cm.framework.ds.activity.ent.vo;

import cm.framework.ds.activity.enums.ActivityOperation;
import cm.framework.ds.common.constants.DefaultFilterConstants;
import cm.framework.ds.common.ent.vo.CommonVO;
import cm.framework.ds.common.ent.vo.UserPersonalDataVO;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "activity")
@NamedQueries(value = {
        @NamedQuery(name = ActivityVO.FINDBYUSER, query = "select a from ActivityVO a where a.userId =:userId order by a.dateCreated desc"),
})
@Where(clause = DefaultFilterConstants.FILTER_ACTIVITY_CANC)
public class ActivityVO extends CommonVO {

    public static final String FINDBYUSER = "cm.framework.ds.activity.ent.vo.ActivityVO.findByUserId";

    private ActivityIdVO id;

    private String activity;

    private Long userId;

    private ActivityOperation operation;

    private Set<UserPersonalDataVO> usersPersonalData;

    @EmbeddedId
    public ActivityIdVO getId() { return id; }

    @Basic(optional = false)
    @Column(name = "activity", nullable = false)
    public String getActivity() { return activity; }

    @Basic(optional = false)
    @Column(name = "r_user_id", nullable = false)
    public Long getUserId(){ return userId;}

    @Enumerated(EnumType.STRING)
    @Column(name = "operation", length = 10)
    public ActivityOperation getOperation() {
        return operation;
    }


    @ManyToMany(cascade = {CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinTable(name = "activities_user_personal_data", joinColumns = {@JoinColumn(name = "code"),@JoinColumn(name = "token")},
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    public Set<UserPersonalDataVO> getUsersPersonalData() { return usersPersonalData; }

    public void setId(ActivityIdVO id) { this.id = id; }

    public void setActivity(String activity) { this.activity = activity; }

    public void setUserId(Long userId) {   this.userId = userId;    }

    public void setOperation(ActivityOperation operation) { this.operation = operation; }

    public void setUsersPersonalData(Set<UserPersonalDataVO> usersPersonalData) { this.usersPersonalData = usersPersonalData; }


}