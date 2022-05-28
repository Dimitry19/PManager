package cm.travelpost.tp.pricing.ent.vo;

import cm.travelpost.tp.constant.FieldConstants;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PricingSubscriptionEmbeddedId implements Serializable {

    private String code;
    private String token;

    @NaturalId
    @Basic(optional = false)
    @Column(name = "CODE", nullable = false, unique = true, length = FieldConstants.CODE_LEN)
    public String getCode() { return code; }

    @Basic(optional = false)
    @Column(name = "TOKEN", nullable = false, length = FieldConstants.CODE_LEN)
    public String getToken() { return token; }

    public void setCode(String code) { this.code = code; }

    public void setToken(String token) { this.token = token; }
}
