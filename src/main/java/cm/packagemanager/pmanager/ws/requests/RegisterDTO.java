package cm.packagemanager.pmanager.ws.requests;

import cm.packagemanager.pmanager.common.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO extends LoginDTO{




	@NotNull(message = "First name should not be empty")
	@Size(min = 1, max = 60, message = "First name should be between 1 and 60 characters")
	private String firstName;

	@NotNull(message = "Last name should not be empty")
	@Size(min = 1, max = 60, message = "Last name should be between 1 and 60 characters")
	private String lastName;

	@Enumerated(EnumType.STRING)
	private RoleEnum role;



	public RoleEnum getRole() {
		return role;
	}

	public void setRole(RoleEnum role) {
		this.role = role;
	}


	private String phone;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
