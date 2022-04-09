package cm.travelpost.tp.common.ent.vo;

import cm.travelpost.tp.airline.ent.vo.AirlineIdVO;
import cm.travelpost.tp.common.listener.audit.TableListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 *
 */
@MappedSuperclass
@EntityListeners(TableListener.class)
public class CommonIdVO implements Serializable {

    protected String token;


    @Column(name = "TOKEN", nullable = false)
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AirlineIdVO that = (AirlineIdVO) o;

        if (!token.equals(that.token)) return false;
        return token.equals(that.token);
    }

    @Override
    public int hashCode() {
        int result = token.hashCode();
        result = 31 * result + token.hashCode();
        return result;
    }
}
