package cm.travelpost.tp.airline.ent.vo;


import cm.framework.ds.common.ent.vo.CommonIdVO;
import cm.travelpost.tp.constant.FieldConstants;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class AirlineIdVO extends CommonIdVO {

    private String code;

    public AirlineIdVO() {

    }

    public AirlineIdVO(String code, String token) {
        this.code = code;
        this.token = token;
    }

    @Basic(optional = false)
    @Column(name = "CODE", nullable = false, length = FieldConstants.AIR_ID_LEN)
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

        AirlineIdVO that = (AirlineIdVO) o;

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
