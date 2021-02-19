package cm.packagemanager.pmanager.common.ent.vo;

import cm.packagemanager.pmanager.common.ent.bo.Auditable;
import cm.packagemanager.pmanager.common.listener.audit.TableListener;


import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

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

/*	private long retCode;

	private String retDescription;*/


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


	/*@Transient
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
	}*/




}
