package cm.travelpost.tp.announce.ent.vo;

import cm.travelpost.tp.common.ent.vo.CommonIdVO;

import javax.persistence.Embeddable;


@Embeddable
public class AnnounceIdVO extends CommonIdVO {


    public AnnounceIdVO(String token) {
        setToken(token);
    }

    public AnnounceIdVO() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnnounceIdVO that = (AnnounceIdVO) o;

        if (!that.token.equals(that.token)) return false;
        return token.equals(that.token);
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + token.hashCode();
        return result;
    }


}
