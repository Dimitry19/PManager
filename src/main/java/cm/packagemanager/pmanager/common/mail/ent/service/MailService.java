package cm.packagemanager.pmanager.common.mail.ent.service;

import cm.packagemanager.pmanager.common.mail.ent.vo.ContactUSVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.mail.ContactUSDTO;
import com.sendgrid.Response;
import org.springframework.mail.MailException;

import java.io.IOException;

public interface MailService {

    void sendEmail(UserVO user) throws MailException;

    Response contactUS(ContactUSDTO contactUS) throws MailException, IOException;


}
