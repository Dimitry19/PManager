package cm.travelpost.tp.announce.ent.vo;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Access(AccessType.PROPERTY)
public class ReservationUserIdVO implements Serializable {

    private Long id;

    private Long userId;

    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    @Column(name = "R_USER_ID")
    public Long getUserId() {
        return userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationUserIdVO that = (ReservationUserIdVO) o;
        return id.equals(that.id) && userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }
}
