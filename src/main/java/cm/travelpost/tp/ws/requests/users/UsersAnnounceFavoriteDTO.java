package cm.travelpost.tp.ws.requests.users;

import javax.validation.constraints.NotNull;

public class UsersAnnounceFavoriteDTO {

    //@NotNull
    //private Long announceId;

    //@NotNull
   // private Long userId;

    @NotNull
    private Long idAnnounce;

    @NotNull
    private Long idUser;

    public Long getIdAnnounce() {  return idAnnounce;}

    public Long getIdUser() {    return idUser; }

    public Long getAnnounceId() {
        return getIdAnnounce();
    }

    public Long getUserId() {   return getIdUser(); }

    public void setIdAnnounce(Long idAnnounce) { this.idAnnounce = idAnnounce;}
    public void setIdUser(Long idUser) { this.idUser = idUser;}

       /*public void setAnnounceId(Long announceId) {
        this.announceId = announceId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }*/
}
