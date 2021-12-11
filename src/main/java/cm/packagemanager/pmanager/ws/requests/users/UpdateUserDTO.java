package cm.packagemanager.pmanager.ws.requests.users;

import cm.packagemanager.pmanager.common.enums.Gender;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateUserDTO {

    private long id;

    @ApiModelProperty(notes = "An email for login", required = true)
    private String email;

    @NotNull(message = "Entrez un prenom valide")
    @NotBlank(message = "Entrez un prenom valide")
    @Size(min = 3, max = 60, message = "Entrez un prenom valide ayant un minimum de 3 caracteres")
    private String firstName;

    @NotNull(message = "Entrez un nom valide")
    @NotBlank(message = "Entrez un nom valide")
    @Size(min = 3, max = 60, message = "Entrez un nom valide ayant un minimum de 3 caracteres")
    private String lastName;

    @NotNull(message = "Le genre doit etre valorisé")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String phone;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Email(message = "Email : format non valide")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

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
