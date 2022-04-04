package cm.packagemanager.pmanager.ws.requests.airplane;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class AirlineDTO {


    @NotNull(message = "Le code de la compagnie doit etre valorisé")
    @NotBlank(message = "Le code de la compagnie doit etre valorisé")
    private String code;


    @NotNull(message = "La description doit etre valorisé")
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
