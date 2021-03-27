package cm.packagemanager.pmanager.common.mail.ent.service;

import cm.packagemanager.pmanager.common.mail.ent.vo.ContactUSVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import com.sendgrid.Response;
import org.springframework.mail.MailException;

import java.io.IOException;

public interface MailService {
	public void sendEmail(UserVO user) throws MailException;

	public Response contactUS(ContactUSVO contactUS) throws MailException, IOException;

}
