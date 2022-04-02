package cm.packagemanager.pmanager.common.mail.ent.service;

import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.mail.ContactUSDTO;
import cm.packagemanager.pmanager.ws.requests.mail.MailDTO;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.sendgrid.Response;
import org.springframework.mail.MailException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface MailService {

    void sendEmail(UserVO user) throws MailException;

    boolean contactUS(ContactUSDTO contactUS) throws Exception;

    boolean buildAndSendMail(HttpServletRequest request, UserVO user) throws UserException, IOException, MailjetSocketTimeoutException, MailjetException, MessagingException;

    Response sendMail(MailDTO mr, boolean active) throws Exception;


}
