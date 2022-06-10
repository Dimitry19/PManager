package cm.travelpost.tp.ws.requests.users;

import javax.validation.constraints.NotNull;

public class UsersAnnounceFavoriteDTO {

    @NotNull
    private Long announceId;

    @NotNull
    private Long userId;

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
}
