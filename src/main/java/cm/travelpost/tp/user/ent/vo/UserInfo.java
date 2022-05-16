package cm.travelpost.tp.user.ent.vo;

import cm.travelpost.tp.common.utils.CollectionsUtils;
import cm.travelpost.tp.common.utils.DateUtils;
import cm.travelpost.tp.configuration.filters.CommonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1905122041532201207L;

    private Long id;

    private String firstName;

    private String username;

    private String lastName;

    private String email;

    private String phone;

    private int count;

    private double rating = 0;

    private byte[] picByte;

    private String origin;

    private Timestamp dateCreated;

    private List<RoleVO> roles;


    public UserInfo(Long id, String firstName, String username, String lastName, String email, String phone, int count) {
        this.id = id;
        this.firstName = firstName;
        this.username = username;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.count = count;

    }

    public UserInfo(UserVO user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.username = user.getUsername();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.dateCreated = user.getDateCreated();
        this.rating = user.getRating();
        this.count = CollectionsUtils.size(user.getAnnounces());
        this.picByte=user.getImage()!=null?user.getImage().getPicByte():null;
        this.origin=user.getImage()!=null?user.getImage().getOrigin():null;
        this.roles=CommonFilter.getRolesAuthoritiesUser(user);
    }

    @JsonProperty
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @JsonProperty
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonProperty
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @JsonProperty
    @JsonFormat(pattern = DateUtils.FORMAT_STD_PATTERN_4)
    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    @JsonProperty
    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @JsonProperty
    public byte[] getPicByte() {
        return picByte;
    }

    public void setPicByte(byte[] picByte) {
        this.picByte = picByte;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
    @JsonProperty
    public List<RoleVO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleVO> roles) {
        this.roles = roles;
    }
}
