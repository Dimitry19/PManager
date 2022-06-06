package cm.travelpost.tp.ws.requests.users;

public class UsersAnnounceFavoriteDTO {

    private Long idAnnounce;

    private Long idUser;


    public Long getIdAnnounce() {
        return idAnnounce;
    }

    public Long getIdUser() {
        return idUser;
    }


    public void setIdAnnounce(Long idAnnounce) {
        this.idAnnounce = idAnnounce;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }
}
