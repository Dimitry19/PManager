package cm.framework.ds.common.listener.audit;



import cm.framework.ds.common.ent.bo.Auditable;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.sql.Timestamp;
import java.time.Instant;

public class TableListener {


    @PrePersist
    void preCreate(Auditable auditable) {
        Timestamp now = Timestamp.from(Instant.now());
        auditable.setDateCreated(now);
        auditable.setLastUpdated(now);
    }

    @PreUpdate
    void preUpdate(Auditable auditable) {
        Timestamp now = Timestamp.from(Instant.now());
        auditable.setLastUpdated(now);
    }
}
