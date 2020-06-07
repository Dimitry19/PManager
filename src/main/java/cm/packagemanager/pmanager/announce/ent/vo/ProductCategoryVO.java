package cm.packagemanager.pmanager.announce.ent.vo;

import cm.packagemanager.pmanager.common.ent.vo.CommonVO;

import javax.persistence.*;

/**
 *
 */

@Entity
@Table(name="PROD_CATEGORY", schema = "PUBLIC")
public class ProductCategoryVO extends CommonVO {


	@EmbeddedId
	private ProductCategoryIdVO id;

	private String description;

	private boolean cancelled;

	public ProductCategoryIdVO getId() {
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

	public void setId(ProductCategoryIdVO id) {
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
