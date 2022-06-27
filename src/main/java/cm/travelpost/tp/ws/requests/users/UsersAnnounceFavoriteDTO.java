package cm.travelpost.tp.ws.requests.users;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class UsersAnnounceFavoriteDTO {
    @NotNull
    @Positive
    private Long announceId;

    @NotNull
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
