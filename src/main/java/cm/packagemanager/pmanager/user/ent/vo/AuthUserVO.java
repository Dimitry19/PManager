package cm.packagemanager.pmanager.user.ent.vo;
import cm.packagemanager.pmanager.common.ent.vo.*;
import cm.packagemanager.pmanager.constant.FieldConstants;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;


@Entity
@Table(name = "AUTH_USER")
public class AuthUserVO extends CommonVO{

	private static final long serialVersionUID = 1L;

	private int id;

	//private String username;

	private String password;

	//private UserVO user;

	@EmbeddedId
	private AuthUserIdVO authUserId;

	@Transient
	private boolean rememberMe;


	//@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	public int getId() {
		return id;

	}



	/*@NaturalId
	@Basic(optional = false)
	@Column(name="USERNAME", nullable=false,length=FieldConstants.AUTH_USER_LEN)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	} */

	@Basic(optional = false)
	@Column(name="PASSWORD", nullable=false)
	public String getPassword() {
		return password;
	}


	/*@OneToOne(fetch = FetchType.LAZY,cascade =  CascadeType.ALL,mappedBy = "authUsr")
	public UserVO getUser() {
		return user;
	}*/


	public void setId(int id) {
		this.id = id;
	}

	public void setPassword(String password) {
		this.password = password;
	}
/*
	public void setUser(UserVO user) {
		this.user= user;
	}
*/
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
