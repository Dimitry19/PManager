package cm.travelpost.tp.announce.ent.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Transient;
import java.io.Serializable;

public class CommonReservationTransient implements Serializable {

    protected AnnounceInfo announceInfo;

    protected String warning;

    @Transient
    @JsonProperty
    public AnnounceInfo getAnnounceInfo() {
        return announceInfo;
    }

    @Transient
    @JsonProperty
    public String getWarning(){
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

}
