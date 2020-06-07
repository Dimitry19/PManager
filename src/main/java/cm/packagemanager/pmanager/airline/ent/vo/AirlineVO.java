package cm.packagemanager.pmanager.airline.ent.vo;

import cm.packagemanager.pmanager.common.ent.vo.CommonVO;
import javax.persistence.*;

/**
 *
 */

@Entity
@Table(name="AIRLINE", schema = "PUBLIC")
public class AirlineVO extends CommonVO {

	@EmbeddedId
	private AirlineIdVO id;

	private String description;

	private boolean cancelled;


	public AirlineIdVO getId() {
		return id;

	}

	@Basic(optional = false)
	@Column(name="DESCRITPTION", nullable = false)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(AirlineIdVO id) {
		this.id = id;
	}

	@Basic(optional = false)
	@Column(name="CANCELLED")
	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
