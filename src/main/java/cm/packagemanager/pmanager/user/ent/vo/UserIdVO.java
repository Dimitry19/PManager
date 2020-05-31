package cm.packagemanager.pmanager.user.ent.vo;


import cm.packagemanager.pmanager.common.ent.vo.CommonIdVO;
import cm.packagemanager.pmanager.common.ent.vo.CommonVO;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;


@Embeddable
public class UserIdVO extends CommonIdVO {

	private String userId;

	public UserIdVO() {
		super();
	}

	public UserIdVO(String userId) {
		this.userId=userId;
	}

	@Basic(optional = false)
	@Column(name = "USER_ID", nullable = false)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserIdVO that = (UserIdVO) o;

		if (!userId.equals(that.userId)) return false;
		return token.equals(that.token);
	}

	@Override
	public int hashCode() {
		int result = userId.hashCode();
		result = 31 * result + userId.hashCode();
		return result;
	}
}
