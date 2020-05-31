package cm.packagemanager.pmanager.user.ent.vo;

import cm.packagemanager.pmanager.common.ent.vo.CommonVO;
import cm.packagemanager.pmanager.common.enums.Gender;
import cm.packagemanager.pmanager.constant.FieldConstants;
import javax.persistence.*;


@Entity
@Table(name="USER", schema = "PUBLIC")
public class UserVO extends CommonVO  {


	private String firstName;

	private String lastName;

	private String email;

	private String phone;

	private Gender gender;


	@EmbeddedId
	private UserIdVO id;

	public UserIdVO getId() {
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

	@Basic(optional = false)
	@Column(name="PHONE",nullable = false,length = FieldConstants.PHONE_LEN)
	public String getPhone() {
		return phone;
	}

	@Basic(optional = false)
	@Column(name="EMAIL", nullable=false)
	public String getEmail() {
		return email;
	}




	@Enumerated(EnumType.STRING)
	@Column(length = 10)
	public Gender getGender(){
		return gender;
	}


	public void setId(UserIdVO id) {
		this.id = id;
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



	@Override
	public String toString() {
		return "User [phone=" + phone + ", firstName=" + firstName +
				", lastName=" + lastName + ", email=" + email   + "]";
	}
}
