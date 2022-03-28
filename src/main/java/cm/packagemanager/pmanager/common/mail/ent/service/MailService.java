package cm.packagemanager.pmanager.common.mail.ent.service;

import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.mail.ContactUSDTO;
import com.sendgrid.Response;
import org.springframework.mail.MailException;

public interface MailService {

    void sendEmail(UserVO user) throws MailException;

    Response contactUS(ContactUSDTO contactUS) throws Exception;


}
