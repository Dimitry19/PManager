package cm.travelpost.tp.ws.requests.users;

import cm.travelpost.tp.common.enums.RoleEnum;
import cm.travelpost.tp.ws.requests.CommonSearchDTO;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class UserSeachDTO extends CommonSearchDTO {

    private String email;
    private String provider;
    private String socialId;
    private String username;
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }
}
