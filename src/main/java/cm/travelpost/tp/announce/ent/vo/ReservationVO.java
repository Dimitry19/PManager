package cm.travelpost.tp.announce.ent.vo;


import cm.framework.ds.common.ent.vo.CommonVO;
import cm.travelpost.tp.common.enums.StatusEnum;
import cm.travelpost.tp.common.enums.ValidateEnum;
import cm.travelpost.tp.configuration.filters.FilterConstants;
import cm.travelpost.tp.constant.FieldConstants;
import cm.travelpost.tp.user.ent.vo.UserInfo;
import cm.travelpost.tp.user.ent.vo.UserVO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
@Table(name = "reservation")
@NamedQueries({
        @NamedQuery(name = ReservationVO.FIND_BY_ANNOUNCE, query = " select r from  ReservationVO as r where r.announce.id =: announceId"),
        @NamedQuery(name = ReservationVO.FIND_BY_USER, query = " select r from  ReservationVO as r where r.user.id =: userId"),
        @NamedQuery(name = ReservationVO.FIND_ANNOUNCE_USER, query = " select r from  ReservationVO as r inner join r.announce a where a.user.id =: userId"),
        @NamedQuery(name = ReservationVO.FIND_BY_ANNOUNCE_AND_USER_AND_VALIDATE, query = " select r from  ReservationVO as r where r.announce.id =:announceId  and r.user.id =: userId and r.validate =:validate"),
})
@Filters({
        @Filter(name = FilterConstants.CANCELLED)
})
@Where(clause = FilterConstants.FILTER_WHERE_RESERVATION_CANC)
public class ReservationVO extends CommonVO {

    public static final String FIND_BY_ANNOUNCE = "cm.travelpost.tp.announce.ent.vo.ReservationVO.findByAnnounce";
    public static final String FIND_BY_USER = "cm.travelpost.tp.announce.ent.vo.ReservationVO.findByUser";
    public static final String FIND_ANNOUNCE_USER = "cm.travelpost.tp.announce.ent.vo.ReservationVO.findByAnnounceUser";
    public static final String FIND_BY_ANNOUNCE_AND_USER_AND_VALIDATE = "cm.travelpost.tp.announce.ent.vo.ReservationVO.findByAnnounceAndUserValidate";
    public static final String SQL_FIND_BY_USER = " FROM ReservationVO r where r.user.id =:userId";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    //@JsonIgnore
    //@Access(AccessType.PROPERTY)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "R_USER_ID", updatable = false)
    @JsonBackReference
    private UserVO user;


    @Basic(optional = false)
    @Column(name = "WEIGTH", nullable = false)
    private BigDecimal weight;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "reservation_category", joinColumns = @JoinColumn(name = "RESERVATION_ID"), inverseJoinColumns = @JoinColumn(name = "CATEGORIES_CODE"))
    @JsonProperty
    private Set<CategoryVO> categories = new HashSet<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "R_ANNOUNCE_ID", updatable = true)
    @JsonBackReference
    private AnnounceMasterVO announce;

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "VALIDATE")
    private ValidateEnum validate;


    @Basic(optional = false)
    @Column(name = "DESCRIPTION", length = FieldConstants.DESC)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 10)
    private StatusEnum status;

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }


    @Transient
    @JsonProperty
    private UserInfo userInfo;

    @Transient
    @JsonProperty
    private AnnounceInfo announceInfo;


    @Transient
    @JsonProperty
    private String warning;

    public AnnounceInfo getAnnounceInfo() {
        return announceInfo;
    }

    public void setAnnounceInfo(AnnounceInfo announceInfo) {
        this.announceInfo = announceInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public ReservationVO() {
        super();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
        this.userInfo = new UserInfo(user);
    }

    public AnnounceMasterVO getAnnounce() {
        return announce;
    }

    public void setAnnounce(AnnounceMasterVO announce) {
        this.announce = announce;
        announceInfo = new AnnounceInfo(announce);
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Set<CategoryVO> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryVO> categories) {
        this.categories = categories;
    }

    public ValidateEnum getValidate() {
        return validate;
    }

    public void setValidate(ValidateEnum validate) {
        this.validate = validate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public void addCategory(CategoryVO category) {
        categories.add(category);
    }

    public void removeCategory(CategoryVO category) {

        categories.remove(category);
    }

    public void removeCategories() {
        Iterator<CategoryVO> iterator = this.categories.iterator();
        while (iterator.hasNext()) {
            iterator.remove();
        }
    }
}
