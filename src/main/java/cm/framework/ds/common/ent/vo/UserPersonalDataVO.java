package cm.framework.ds.common.ent.vo;

import cm.framework.ds.activity.ent.vo.ActivityVO;
import cm.travelpost.tp.common.utils.ImageUtils;
import cm.travelpost.tp.user.enums.Gender;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "v_user_personal_data", schema = "views")
@Immutable
public class UserPersonalDataVO {

    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private Gender gender;

    private String country;

    private String city;

    private String origin;

    private byte[] picByte;

    private String phone;

    private String email;

    private Set<ActivityVO> activities;

    @Id
    @Column(name = "ID")
    public Long getId() { return id; }

    @Basic(optional = true)
    @Column(name = "FIRST_NAME")
    public String getFirstName() { return firstName; }

    @Basic(optional = true)
    @Column(name = "LAST_NAME")
    public String getLastName() { return lastName; }


    @Basic(optional = true)
    @Column(name = "USERNAME")
    public String getUsername() { return username; }


    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER", length = 10)
    public Gender getGender() { return gender; }


    @Basic(optional = true)
    @Column(name = "COUNTRY")
    public String getCountry() { return country; }


    @Basic(optional = true)
    @Column(name = "CITY")
    public String getCity() { return city; }

    @Basic(optional = true)
    @Column(name = "ORIGIN")
    public String getOrigin() { return origin; }

    @Basic(optional = true)
    @Column(name = "PIC_BYTE")
    public byte[] getPicByte() {  return ImageUtils.decompressImage(picByte); }


    @Basic(optional = true)
    @Column(name = "PHONE")
    public String getPhone() { return phone; }


    @Basic(optional = true)
    @Column(name = "EMAIL")
    public String getEmail() { return email; }

    @Access(AccessType.PROPERTY)
    @ManyToMany(mappedBy = "usersPersonalData",cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JsonBackReference
    public Set<ActivityVO> getActivities() { return activities; }

    public void setId(Long id) { this.id = id; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setUsername(String username) { this.username = username; }

    public void setGender(Gender gender) { this.gender = gender; }

    public void setCountry(String country) { this.country = country; }

    public void setCity(String city) { this.city = city;}

    public void setOrigin(String origin) { this.origin = origin; }

    public void setPicByte(byte[] picByte) { this.picByte = ImageUtils.compressImage(picByte);; }

    public void setPhone(String phone) { this.phone = phone; }

    public void setEmail(String email) { this.email = email; }

    public void setActivities(Set<ActivityVO> activities) { this.activities = activities; }
}
