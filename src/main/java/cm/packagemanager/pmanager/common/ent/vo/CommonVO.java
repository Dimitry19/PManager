package cm.packagemanager.pmanager.common.ent.vo;

import cm.packagemanager.pmanager.common.ent.bo.Auditable;
import cm.packagemanager.pmanager.common.listener.audit.TableListener;


import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 */
@MappedSuperclass
@EntityListeners(TableListener.class)
public class CommonVO implements Auditable, Serializable {


	private Timestamp dateCreated;

	private Timestamp lastUpdated;


	@Override
	public Timestamp getDateCreated() {
		return  dateCreated;
	}

	@Override
	public void setDateCreated(Timestamp dateCreated) {
		this.dateCreated=dateCreated;
	}

	@Override
	public Timestamp getLastUpdated() {
		return lastUpdated;
	}

	@Override
	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated=lastUpdated;
	}
}
