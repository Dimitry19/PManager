package cm.travelpost.tp.ws.requests.users;

import javax.validation.constraints.NotNull;

public class UsersAnnounceFavoriteDTO {

    //@NotNull
    private Long announceId;

    //@NotNull
    private Long userId;

    @NotNull
    private Long idAnnounce;

    @NotNull
    private Long idUser;

    public Long getAnnounceId() {
        return announceId;
    }

    public void setAnnounceId(Long announceId) {
        this.announceId = announceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public Long getIdAnnounce() {
        return idAnnounce;
    }

    public void setIdAnnounce(Long idAnnounce) {
        this.idAnnounce = idAnnounce;
        this.announceId=idAnnounce;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
        this.userId=idUser;
    }
}
