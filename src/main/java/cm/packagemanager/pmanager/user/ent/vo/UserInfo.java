package cm.packagemanager.pmanager.user.ent.vo;

import cm.packagemanager.pmanager.common.utils.CollectionsUtils;
import cm.packagemanager.pmanager.common.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class UserInfo {

    private Long id;

    private String firstName;

    private String username;

    private String lastName;

    private String email;

    private String phone;

    private int count;

    private double rating = 0;

    private Timestamp dateCreated;


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
}
