package cm.packagemanager.pmanager.announce.ent.vo;


import cm.packagemanager.pmanager.constant.FieldConstants;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 */

@Entity
@Table(name="PROD_CATEGORY", schema = "PUBLIC")
@NamedQueries({
		@NamedQuery(name =ProductCategoryVO.FIND_BY_CODE ,query = "select pc from ProductCategoryVO pc where pc.code =:code"),
})
public class ProductCategoryVO implements Serializable {


	public static final String FIND_BY_CODE="cm.packagemanager.pmanager.announce.ent.vo.ProductCategoryVO.findByCode";
	@Id
	private String code;

	private String description;

	//private boolean cancelled;

	public ProductCategoryVO(){}

	@Basic(optional = false)
	@Column(name = "CODE", nullable = false,length = FieldConstants.AUTH_USER_LEN)
	public String getCode() {
		return code;

	}

	@Basic(optional = false)
	@Column(name="DESCRITPTION", nullable = false)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCode(String code) {
		this.code = code;
	}


	/*@Basic(optional = false)
	@Column(name="CANCELLED")
	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}*/


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ProductCategoryVO that = (ProductCategoryVO) o;

		if (!code.equals(that.code)) return false;
		return code.equals(that.code);

	}

	@Override
	public int hashCode() {
		int result = code.hashCode();
		result = 31 * result + description.hashCode();
		return result;
	}


}
