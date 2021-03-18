package cm.packagemanager.pmanager.user.ent.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfo {

	private Long id;

	private String firstName;

	private String username;

	private String lastName;

	private String email;

	private String phone;


	public UserInfo(Long id, String firstName, String username, String lastName, String email, String phone) {
		this.id = id;
		this.firstName = firstName;
		this.username = username;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
	}

	public UserInfo(UserVO user) {
		this.id = user.getId();
		this.firstName = user.getFirstName();
		this.username = user.getUsername();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.phone = user.getPhone();
	}

	@JsonProperty
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	@JsonProperty
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@JsonProperty
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JsonProperty
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@JsonProperty
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
