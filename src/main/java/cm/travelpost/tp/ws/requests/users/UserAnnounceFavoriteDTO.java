package cm.travelpost.tp.ws.requests.users;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class UserAnnounceFavoriteDTO {
    @NotNull
    @Positive
    private Long announceId;

    @NotNull
    @Positive
    private Long userId;


    @NotNull
    @Positive
    private Long idAnnounce;

    @NotNull
    @Positive
    private Long idUser;

    public Long getAnnounceId() {
        return announceId;
    }

    public Long getUserId() {   return userId; }

     public void setAnnounceId(Long announceId) {
        this.announceId = announceId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getIdAnnounce() {
        return idAnnounce;
    }

    public void setIdAnnounce(Long idAnnounce) {
        this.idAnnounce = idAnnounce;
        this.announceId = idAnnounce;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
        this.userId=idUser;
    }
}
