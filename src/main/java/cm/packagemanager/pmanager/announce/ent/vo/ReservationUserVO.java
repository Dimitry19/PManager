package cm.packagemanager.pmanager.announce.ent.vo;

import cm.packagemanager.pmanager.common.enums.Gender;
import cm.packagemanager.pmanager.common.enums.ValidateEnum;
import cm.packagemanager.pmanager.common.utils.DateUtils;
import cm.packagemanager.pmanager.constant.FieldConstants;
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
@Table(name = "VALID_RESERVATION_CREATED", schema = "VIEWS")
@Immutable
public class ReservationUserVO implements Serializable {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "R_USER_ID")
    private Long userId;

    @Basic(optional = false)
    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER", length = 10)
    private Gender gender;

    @Basic(optional = false)
    @Column(name = "WEIGTH", nullable = false)
    private BigDecimal weight;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "RESERVATION_CATEGORY", joinColumns = {
            @JoinColumn(name = "RESERVATION_ID", referencedColumnName = "ID", insertable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "CATEGORIES_CODE", insertable = false, updatable = false)})
    @JsonProperty
    private Set<CategoryVO> categories = new HashSet<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "R_ANNOUNCE_ID", updatable = false, insertable = false)
    @JsonBackReference
    private AnnounceVO announce;

    @Enumerated(EnumType.STRING)
    @Column(name = "VALIDATE")
    private ValidateEnum validate;

    @Column(name = "DESCRIPTION", length = FieldConstants.DESC)
    private String description;


    @JsonFormat(pattern = DateUtils.FORMAT_STD_PATTERN_4)
    @JsonProperty
    @Column(name = "DATECREATED")
    private Timestamp dateCreated;



    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Transient
    @JsonProperty
    private AnnounceInfo announceInfo;

    public AnnounceInfo getAnnounceInfo() {
        return announceInfo;
    }

    public void setAnnounceInfo(AnnounceInfo announceInfo) {
        this.announceInfo = announceInfo;
    }

    public ReservationUserVO() {
        super();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public AnnounceVO getAnnounce() {
        return announce;
    }

    public void setAnnounce(AnnounceVO announce) {
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
