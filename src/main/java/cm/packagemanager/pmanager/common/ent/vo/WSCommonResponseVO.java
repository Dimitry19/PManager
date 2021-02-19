package cm.packagemanager.pmanager.common.ent.vo;

import javax.persistence.Transient;

public class WSCommonResponseVO extends CommonVO{


	private long retCode;

	private String retDescription;

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
	}
}
