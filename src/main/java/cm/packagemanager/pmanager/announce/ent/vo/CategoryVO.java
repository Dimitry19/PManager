package cm.packagemanager.pmanager.announce.ent.vo;


import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.constant.FieldConstants;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 */

@Entity
@Table(name= "CATEGORY", schema = "PUBLIC")
@NamedQueries({
		@NamedQuery(name = CategoryVO.FIND_BY_CODE ,query = "select pc from CategoryVO pc where pc.code =:code"),
})
public class CategoryVO implements Serializable {


	public static final String FIND_BY_CODE="cm.packagemanager.pmanager.announce.ent.vo.ProductCategoryVO.findByCode";
	private static final long serialVersionUID = -6108078165603686675L;


	@Id
	@Basic(optional = false)
	@Column(name = "CODE", nullable = false,length = FieldConstants.AUTH_USER_LEN)
	private String code;

	@Basic(optional = false)
	@Column(name="DESCRIPTION")
	private String description;

	//private boolean cancelled;

	public CategoryVO(){}

	public String getCode() {
		return code;

	}


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

		CategoryVO that = (CategoryVO) o;

		if (!code.equals(that.code)) return false;
		return code.equals(that.code);

	}

	@Override
	public int hashCode() {
		int result = code.hashCode();
		if(StringUtils.isNotEmpty(description)){
			result = 31 * result +description.hashCode();
		}
		return result;
	}


	@Override
	public String toString() {
		return "CategoryVO{" + "code='" + code + '\'' + ", description='" + description + '\'' + '}';
	}
}
