package cm.framework.ds.activity.ent.vo;


import cm.framework.ds.common.ent.vo.CommonIdVO;
import cm.travelpost.tp.constant.FieldConstants;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class ActivityIdVO extends CommonIdVO {

    private String code;

    public ActivityIdVO() {

    }

    public ActivityIdVO(String code, String token) {
        this.code = code;
        this.token = token;
    }

    @Basic(optional = false)
    @Column(name = "CODE", nullable = false, length = FieldConstants.CODE_LEN)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActivityIdVO that = (ActivityIdVO) o;

        if (!code.equals(that.code)) return false;
        return token.equals(that.token);
    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + token.hashCode();
        return result;
    }
}
