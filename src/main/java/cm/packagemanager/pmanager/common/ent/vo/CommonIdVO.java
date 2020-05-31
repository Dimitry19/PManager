package cm.packagemanager.pmanager.common.ent.vo;

import cm.packagemanager.pmanager.common.ent.bo.Auditable;
import cm.packagemanager.pmanager.common.listener.audit.TableListener;

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
public class CommonIdVO implements Serializable {

	protected String token;

}
