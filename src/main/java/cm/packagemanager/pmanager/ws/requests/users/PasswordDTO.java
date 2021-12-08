package cm.packagemanager.pmanager.ws.requests.users;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class PasswordDTO {

    @NotNull(message = "Entrez un email valide")
    @Email(message = "Email : format non valide")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
