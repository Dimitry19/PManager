package cm.travelpost.tp.ws.requests.announces;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;


public class ReservationDTO extends CommonReservationDTO {

    @NotNull(message = "L'annonce doit etre valorisée")
    @PositiveOrZero
    private Long announceId;

    public Long getAnnounceId() {
        return announceId;
    }

    public void setAnnounceId(Long announceId) {
        this.announceId = announceId;
    }


}
