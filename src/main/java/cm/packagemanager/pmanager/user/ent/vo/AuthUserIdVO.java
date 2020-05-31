package cm.packagemanager.pmanager.user.ent.vo;


import cm.packagemanager.pmanager.common.ent.vo.CommonIdVO;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;


@Embeddable
public class AuthUserIdVO extends CommonIdVO {

	private String username ;

	public AuthUserIdVO() {

	}

	public AuthUserIdVO(String userId) {
		this.username=username;
	}

	@Basic(optional = false)
	@Column(name = "USER_ID", nullable = false)
	public String getUsername() {
		return username;
	}

	public void setUsername(String userId) {
		this.username = username;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AuthUserIdVO that = (AuthUserIdVO) o;

		if (!username.equals(that.username)) return false;
		return token.equals(that.token);
	}

	@Override
	public int hashCode() {
		int result = username.hashCode();
		result = 31 * result + token.hashCode();
		return result;
	}
}
