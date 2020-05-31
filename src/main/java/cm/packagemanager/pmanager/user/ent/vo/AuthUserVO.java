package cm.packagemanager.pmanager.user.ent.vo;
import cm.packagemanager.pmanager.common.ent.vo.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;


@Entity
@Table(name = "AUTH_USER", schema = "PUBLIC")
public class AuthUserVO extends CommonVO{

	private static final long serialVersionUID = 1L;

	private int id;

	private String password;

	private UserVO user;

	@NaturalId
	private AuthUserIdVO authUserId;

	@Transient
	private boolean rememberMe;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	public int getId() {
		return id;

	}


	@Basic(optional = false)
	@Column(name="PASSWORD", nullable=false)
	public String getPassword() {
		return password;
	}



	@OneToOne(cascade = CascadeType.ALL)
	public UserVO getUser() {
		return user;
	}


	public void setId(int id) {
		this.id = id;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUser(UserVO user) {
		this.user= user;
	}

	public boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}


	public AuthUserIdVO getAuthUserId() {
		return authUserId;
	}

	public void setAuthUserId(AuthUserIdVO authUserId) {
		this.authUserId = authUserId;
	}
}
