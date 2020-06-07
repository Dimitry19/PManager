package cm.packagemanager.pmanager.user.ent.vo;


import cm.packagemanager.pmanager.common.ent.vo.CommonIdVO;
import cm.packagemanager.pmanager.constant.FieldConstants;
import javax.persistence.*;


@Embeddable
@Access(AccessType.PROPERTY)
public class UserIdVO extends CommonIdVO {

	private String userId;

	public UserIdVO() {

	}

	public UserIdVO(String userId, String token) {
		this.userId=userId;
		this.token=token;
	}

	@Basic(optional = false)
	@Column(name = "USER_ID", nullable = false, length = FieldConstants.USER_ID)
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
