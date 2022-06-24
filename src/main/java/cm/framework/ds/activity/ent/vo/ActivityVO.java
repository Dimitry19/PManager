package cm.framework.ds.activity.ent.vo;

import cm.framework.ds.activity.enums.ActivityOperation;
import cm.framework.ds.common.constants.DefaultFilterConstants;
import cm.framework.ds.common.ent.vo.CommonVO;
import cm.framework.ds.common.ent.vo.UserPersonalDataVO;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Set;

@Entity
@Filters({
        @Filter(name = DefaultFilterConstants.CANCELLED)
})
@Table(name = "activity")
@Where(clause = DefaultFilterConstants.FILTER_ACTIVITY_CANC)
public class ActivityVO extends CommonVO {

    private ActivityIdVO id;

    private String activity;

    private ActivityOperation operation;

    private Set<UserPersonalDataVO> usersPersonalData;

    @EmbeddedId
    public ActivityIdVO getId() { return id; }

    @Basic(optional = false)
    @Column(name = "ACTIVITY", nullable = false)
    public String getActivity() { return activity; }

    @Enumerated(EnumType.STRING)
    @Column(name = "OPERATION", length = 10)
    public ActivityOperation getOperation() {
        return operation;
    }


    @ManyToMany(cascade = {CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinTable(name = "activity_user_persona_data", joinColumns = {@JoinColumn(name = "CODE"),@JoinColumn(name = "TOKEN")},
            inverseJoinColumns = @JoinColumn(name = "USERS_ID"))
    public Set<UserPersonalDataVO> getUsersPersonalData() { return usersPersonalData; }

    public void setId(ActivityIdVO id) { this.id = id; }

    public void setActivity(String activity) { this.activity = activity; }

    public void setOperation(ActivityOperation operation) { this.operation = operation; }

    public void setUsersPersonalData(Set<UserPersonalDataVO> usersPersonalData) { this.usersPersonalData = usersPersonalData; }
}