package cm.framework.ds.common.ent.vo;


import javax.persistence.Transient;

public class WSCommonResponseVO {

    private long retCode;

    private String retDescription;

    private String message;

    @Transient
    public long getRetCode() {
        return retCode;
    }

    public void setRetCode(long retCode) {
        this.retCode = retCode;
    }

    @Transient
    public String getRetDescription() {
        return retDescription;
    }

    public void setRetDescription(String retDescription) {
        this.retDescription = retDescription;
        setMessage(null);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        setRetDescription(null);
    }
}
