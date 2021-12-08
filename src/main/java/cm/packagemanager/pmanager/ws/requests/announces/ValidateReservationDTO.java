package cm.packagemanager.pmanager.ws.requests.announces;

import javax.validation.constraints.NotNull;

public class ValidateReservationDTO {

    @NotNull(message = "L'id de la reservation doit etre valorisé")
    private Long id;
    private boolean validate;


    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
