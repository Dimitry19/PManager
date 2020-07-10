package cm.packagemanager.pmanager.user.ent.vo;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.common.ent.vo.CommonVO;
import cm.packagemanager.pmanager.common.enums.Gender;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.constant.FieldConstants;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.NaturalId;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity(name = "UserVO")
@Table(name="USER", schema = "PUBLIC")
@NamedQueries({
		@NamedQuery(name = UserVO.Q_AC_ITEM, query = "select u from UserVO u where (upper(lastName) like :searchFilter) or(upper(firstName) like :" +
				"searchFilter ) or(username like :searchFilter) or( email like :searchFilter)  order by firstName"),
		@NamedQuery(name = UserVO.ALL, query = "select u from UserVO u   order by firstName"),
		@NamedQuery(name = UserVO.FINDBYID, query = "select u from UserVO u where id  =:id"),
		@NamedQuery(name = UserVO.USERNAME, query = "select u from UserVO u where username like :username "),
		@NamedQuery(name = UserVO.EMAIL, query = "select u from UserVO u where  u.email =:email "),
		@NamedQuery(name = UserVO.CONF_TOKEN, query = "select u from UserVO u where  u.confirmationToken =:ctoken "),
		@NamedQuery(name = UserVO.FACEBOOK, query = "select u from UserVO u where  u.facebookId =:facebookId "),
		@NamedQuery(name = UserVO.GOOGLE, query = "select u from UserVO u where  u.googleId =:googleId "),
})
@Filters({
		@Filter(name = FilterConstants.CANCELLED),
		@Filter(name=FilterConstants.ACTIVE_MBR)
})

public class UserVO extends CommonVO  {


	public static final String FINDBYID="cm.packagemanager.pmanager.user.ent.vo.UserVO.findById";
	public static final String Q_AC_ITEM = "cm.packagemanager.pmanager.user.ent.vo.UserVO.QAutocompleteItem";
	public static final String ALL = "cm.packagemanager.pmanager.user.ent.vo.UserVO.All";
	public static final String USERNAME="cm.packagemanager.pmanager.user.ent.vo.UserVO.findLikeId";
	public static final String CONF_TOKEN="cm.packagemanager.pmanager.user.ent.vo.UserVO.findCToken";
	public static final String EMAIL="cm.packagemanager.pmanager.user.ent.vo.UserVO.findByEmail";
	public static final String FACEBOOK="cm.packagemanager.pmanager.user.ent.vo.UserVO.findByFacebookId";
	public static final String GOOGLE="cm.packagemanager.pmanager.user.ent.vo.UserVO.findByGoogleId";


	private Long id;

	private String firstName;

	private String username;

	private String lastName;

	private String email;

	private String phone;

	private Gender gender;

	private String facebookId;

	private String googleId;

	private String password;

	private Integer active;

	private boolean cancelled;

	private Set<MessageVO> messages=new HashSet<>();

	private Set<AnnounceVO> announces=new HashSet<>();

	private Set<RoleVO> roles= new HashSet<>();


	private String confirmationToken;

	private String error;



	public UserVO() {
		super();
	}



	public UserVO(Long id, String username, String password, Integer active) {
		this.id= id;
		this.username=username;
		this.password = password;
		this.active = active;

	}

	public UserVO(String username, String password) {
		this.username=username;
		this.password = password;
	}


	public UserVO(String username, String password, Integer active) {
		this.username=username;
		this.password = password;
		this.active = active;
	}

	@NaturalId
	@Basic(optional = false)
	@Column(name = "USERNAME", unique=true, insertable=true, updatable=true, nullable=false,length = FieldConstants.AUTH_USER_LEN)
	public String getUsername() {
		return username;
	}


	@Basic(optional = false)
	@Column(name="PASSWORD", nullable=false)
	public String getPassword() {
		return password;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	public Long getId() {
		return id;

	}

	@Basic(optional = false)
	@Column(name="FIRST_NAME")
	public String getFirstName() {

		return firstName;
	}



	@Basic(optional = false)
	@Column(name="LAST_NAME")
	public String getLastName() {
		return lastName;
	}

	@Basic(optional = true)
	@Column(name="PHONE",nullable = false,length = FieldConstants.PHONE_LEN)
	public String getPhone() {
		return phone;
	}

	@Basic(optional = false)
	@Column(name="EMAIL", nullable=false,unique = true)
	public String getEmail() {
		return email;
	}


	@Enumerated(EnumType.STRING)
	@Column(length = 10)
	public Gender getGender(){
		return gender;
	}


	@Basic(optional = false)
	@Column(name="CANCELLED")
	public boolean isCancelled() {
		return cancelled;
	}


	@Basic(optional = true)
	@Column(name="FACEBOOK_ID")
	public String getFacebookId() {
		return facebookId;
	}

	@Basic(optional = true)
	@Column(name="GOOGLE_ID")
	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	@Column(name = "ACTIVE", insertable=true, updatable = true, nullable=false)
	public Integer getActive() {
		return active;
	}


	@ManyToMany(cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
	@JoinTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "R_USER"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
	public Set<RoleVO> getRoles() {
		return roles;
	}


	@OneToMany(cascade = CascadeType.ALL,targetEntity=MessageVO.class, mappedBy="user", fetch=FetchType.EAGER)
	public Set<MessageVO> getMessages() {
		return messages;
	}

	@OneToMany(cascade = CascadeType.ALL,targetEntity=AnnounceVO.class, mappedBy="user", fetch=FetchType.EAGER)
	public Set<AnnounceVO> getAnnounces() {
		return announces;
	}

	@Basic(optional = true)
	@Column(name="CONFIRM_TOKEN")
	public String getConfirmationToken() {
		return confirmationToken;
	}

	@Transient
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void setActive(Integer active) {
		this.active = active;
	}


	public void setUsername(String username) {
		this.username = username;
	}

	public void setMessages(Set<MessageVO> messages) {
		this.messages = messages;
	}

	public void setRoles(Set<RoleVO> roles) {
		this.roles = roles;
	}

	public void setAnnounces(Set<AnnounceVO> announces) {
		this.announces = announces;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public void setId(Long id) {this.id = id;}

	public void setPassword(String password) {this.password = password;	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}





	@Override
	public String toString() {
		return "User [phone=" + phone + ", firstName=" + firstName +
				", lastName=" + lastName + ", email=" + email   + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((username== null) ? 0 : username.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserVO other = (UserVO) obj;
		if (active == null) {
			if (other.active != null)
				return false;
		} else if (!active.equals(other.active))
			return false;

		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		return true;
	}

}
