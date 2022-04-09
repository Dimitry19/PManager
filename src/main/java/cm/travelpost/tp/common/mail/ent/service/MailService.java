package cm.travelpost.tp.common.mail.ent.service;

import cm.travelpost.tp.common.exception.UserException;
import cm.travelpost.tp.user.ent.vo.UserVO;
import cm.travelpost.tp.ws.requests.mail.ContactUSDTO;
import cm.travelpost.tp.ws.requests.mail.MailDTO;
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
