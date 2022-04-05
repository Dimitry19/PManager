package cm.packagemanager.pmanager.common.ent.vo;

import cm.packagemanager.pmanager.common.ent.bo.Auditable;
import cm.packagemanager.pmanager.common.listener.audit.TableListener;
import cm.packagemanager.pmanager.common.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 */
@MappedSuperclass
@EntityListeners(TableListener.class)
public class CommonVO extends WSCommonResponseVO implements Auditable, Serializable {


    private Timestamp dateCreated;

    private Timestamp lastUpdated;

    @Basic(optional = false)
    @Column(name = "CANCELLED")
    @JsonIgnore
    protected boolean cancelled;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }


    @Override
    @JsonFormat(pattern = DateUtils.FORMAT_STD_PATTERN_4)
    //@JsonIgnore
    public Timestamp getDateCreated() {
        return dateCreated;
    }

    @Override
    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    @JsonFormat(pattern = DateUtils.FORMAT_STD_PATTERN_4)
    @JsonIgnore
    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
