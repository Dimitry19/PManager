package cm.packagemanager.pmanager.ws.requests.users;

import cm.packagemanager.pmanager.common.enums.Gender;
import cm.packagemanager.pmanager.common.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO extends LoginDTO {


    @NotNull(message = "Entrez un prenom valide")
    @NotBlank(message = "Entrez un prenom valide")
    @Size(min = 3, max = 60, message = "Entrez un prenom valide ayant un minimum de 3 caracteres")
    private String firstName;

    @NotNull(message = "Entrez un nom valide")
    @NotBlank(message = "Entrez un nom valide")
    @Size(min = 3, max = 60, message = "Entrez un nom valide ayant un minimum de 3 caracteres")
    //@Min(value = 1, message = "Age must be greater than or equal to 18")
    //@Max(value = 60, message = "Age must be less than or equal to 150")
    private String lastName;

    @NotNull(message = "Le genre doit etre valorisé")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull(message = "Specifiez une role")
    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }


    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

}
