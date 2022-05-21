package cm.travelpost.tp.authentication.ent.vo;

import cm.framework.ds.common.ent.vo.CommonVO;
import cm.travelpost.tp.user.ent.vo.UserVO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity(name = "AuthenticationVO")
@Table(name = "tp_authentication")
public class AuthenticationVO extends CommonVO {

	private Long id;
	private UserVO user;
	private int attempt;
	private boolean desactivate;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	public Long getId() { return id;}

	@JsonIgnore
	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	public UserVO getUser() {	return user;	}

	@Basic(optional = false)
	@Column(name = "ATTEMPT" , nullable = false)
	public int getAttempt() { 	return attempt;	}

	@Basic(optional = false)
	@Column(name = "DESACTIVATE", nullable = false )
	public boolean isDesactivate() {return desactivate;}


	public void setId(Long id) { this.id = id;	}
	public void setUser(UserVO user) {	this.user = user;	}
	public void setAttempt(int attempt) {	this.attempt = attempt;	}
	public void setDesactivate(boolean desactivate) {this.desactivate = desactivate;}
}
