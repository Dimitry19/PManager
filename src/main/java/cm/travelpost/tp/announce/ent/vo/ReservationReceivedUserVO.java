package cm.travelpost.tp.announce.ent.vo;

import cm.travelpost.tp.common.enums.Gender;
import cm.travelpost.tp.common.enums.ValidateEnum;
import cm.travelpost.tp.common.utils.DateUtils;
import cm.travelpost.tp.constant.FieldConstants;
import cm.travelpost.tp.user.ent.vo.UserInfo;
import cm.travelpost.tp.user.ent.vo.UserVO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "VALID_RESERVATION_RECEIVED", schema = "VIEWS")
@Immutable
public class ReservationReceivedUserVO implements Serializable {


    private Long id;

    private UserVO user;

    private UserVO userReservation;

    private String firstName;

    private String username;

    private String lastName;

    private String email;

    private String phone;

    private Gender gender;

    private BigDecimal weight;

    private AnnounceVO announce;

    private ValidateEnum validate;

    private String description;

    private AnnounceInfo announceInfo;

    private UserInfo userInfo;

    private Set<CategoryVO> categories = new HashSet<>();

    private Timestamp dateCreated;




    @Transient
    @JsonProperty
    public AnnounceInfo getAnnounceInfo() {
        return announceInfo;
    }

    public ReservationReceivedUserVO() {
        super();
    }

    @Id
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "R_USER_ID",updatable = false)
    @JsonBackReference
    public UserVO getUser() {
        return user;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "R_USER_RESERVATION")
    @JsonBackReference
    public UserVO getUserReservation() {
        return userReservation;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "R_ANNOUNCE_ID", updatable = false, insertable = false)
    @JsonBackReference
    @Immutable
    public AnnounceVO getAnnounce() {
        return announce;
    }


    @Basic(optional = false)
    @Column(name = "WEIGTH", nullable = false)
    public BigDecimal getWeight() {
        return weight;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "VALIDATE")
    public ValidateEnum getValidate() {
        return validate;
    }


    @Column(name = "DESCRIPTION", length = FieldConstants.DESC)
    public String getDescription() {
        return description;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "RESERVATION_CATEGORY", joinColumns = {
            @JoinColumn(name = "RESERVATION_ID", referencedColumnName = "ID", insertable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "CATEGORIES_CODE", insertable = false, updatable = false)})
    @JsonProperty
    public Set<CategoryVO> getCategories() {
        return categories;
    }

    @Basic(optional = false)
    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    @Column(name = "USERNAME")
    public String getUsername() {
        return username;
    }

    @Column(name = "LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    @Column(name = "PHONE")
    public String getPhone() {
        return phone;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER", length = 10)
    public Gender getGender() {
        return gender;
    }

    @JsonFormat(pattern = DateUtils.FORMAT_STD_PATTERN_4)
    @JsonProperty
    @Column(name = "DATECREATED")
    public Timestamp getDateCreated() {
        return dateCreated;
    }

    @Transient
    @JsonProperty
    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }

    public void setUserReservation(UserVO userReservation) {
        this.userReservation = userReservation;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setAnnounce(AnnounceVO announce) {
        this.announce = announce;
        announceInfo = new AnnounceInfo(announce);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setValidate(ValidateEnum validate) {
        this.validate = validate;
    }

    public void setAnnounceInfo(AnnounceInfo announceInfo) {
        this.announceInfo = announceInfo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategories(Set<CategoryVO> categories) {
        this.categories = categories;
    }

}
