package cm.packagemanager.pmanager.review.ent.vo;

import cm.packagemanager.pmanager.common.ent.vo.CommonIdVO;

import javax.persistence.Embeddable;


@Embeddable
public class ReviewIdVO extends CommonIdVO {


    public ReviewIdVO(String token) {
        setToken(token);
    }

    public ReviewIdVO() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReviewIdVO that = (ReviewIdVO) o;

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
