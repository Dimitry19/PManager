package cm.travelpost.tp.ws.requests.users;

import cm.travelpost.tp.user.enums.RoleEnum;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class RoleToUserDTO {

    @NotNull(message = "Entrez un email valide")
    @Email(message = "Email : format non valide")
    private String email;

    private String username;

    @NotNull(message = "role doit etre valoriser")
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }
}
