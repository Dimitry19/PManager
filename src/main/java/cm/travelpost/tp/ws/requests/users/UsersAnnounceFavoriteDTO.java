package cm.travelpost.tp.ws.requests.users;

import javax.validation.constraints.Positive;

public class UsersAnnounceFavoriteDTO {
    @Positive
    private Long announceId;

    @Positive
    private Long userId;


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
}
