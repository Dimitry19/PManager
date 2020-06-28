package cm.packagemanager.pmanager.common.mail;

import org.springframework.stereotype.Component;

//@Component
public class MailManager {


	private String emailAddress;

	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
}
