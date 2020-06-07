package cm.packagemanager.pmanager.user.ent.vo;


import cm.packagemanager.pmanager.common.ent.vo.CommonIdVO;
import cm.packagemanager.pmanager.constant.FieldConstants;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;


@Embeddable
public class AuthUserIdVO extends CommonIdVO {

	private String username ;

	public AuthUserIdVO() {

	}

	public AuthUserIdVO(String username, String token) {

		this.username=username;
		this.token=token;
	}

	@Basic(optional = false)
	@Column(name = "USERNAME", nullable = false,length = FieldConstants.AUTH_USER_LEN)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
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
