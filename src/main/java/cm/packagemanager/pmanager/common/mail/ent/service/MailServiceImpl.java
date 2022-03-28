package cm.packagemanager.pmanager.common.mail.ent.service;

import cm.packagemanager.pmanager.common.exception.UserException;
import cm.packagemanager.pmanager.common.mail.MailSenderSendGrid;
import cm.packagemanager.pmanager.common.mail.MailType;
import cm.packagemanager.pmanager.common.mail.ent.dao.ContactUSDAO;
import cm.packagemanager.pmanager.common.mail.ent.vo.ContactUSVO;
import cm.packagemanager.pmanager.common.utils.HTMLEntities;
import cm.packagemanager.pmanager.common.utils.MailUtils;
import cm.packagemanager.pmanager.user.ent.service.UserService;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import cm.packagemanager.pmanager.ws.requests.mail.ContactUSDTO;
import cm.packagemanager.pmanager.ws.requests.mail.MailDTO;
import com.sendgrid.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class MailServiceImpl implements MailService {

    public static final String USER_WS = "/ws/user";

    private JavaMailSender javaMailSender;

    @Autowired
    MailSenderSendGrid mailSenderSendGrid;

    @Autowired
    IGoogleMailSenderService googleMailSenderService;

    @Autowired
    ContactUSDAO contactUSDAO;

    @Autowired
    UserService userService;


    @Override
    public void contactUS(ContactUSDTO contactus) throws Exception {

        ContactUSVO contactUS = new ContactUSVO();
        contactUS.setPseudo(contactus.getPseudo());
        contactUS.setSender(contactus.getSender());
        contactUS.setReceiver(contactus.getReceiver());
        contactUS.setContent(contactus.getContent());
        contactUS.setSubject(contactus.getSubject());

        googleMailSenderService.contactUs(contactUS,true);

       /* final Response response  = mailSenderSendGrid.sendContactUs(contactUS, true);

        if (mailSenderSendGrid.manageResponse(response)) {
            contactUSDAO.saves(contactUS);
        }*/
       // return response;
    }


    /**
     * This function is used to send mail that contains a attachment.
     *
     * @param user
     * @throws MailException
     * @throws MessagingException
     */
    public void sendEmailWithAttachment(UserVO user) throws MailException, MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(user.getEmail());
        helper.setSubject("Testing Mail API with Attachment");
        helper.setText("Please find the attached document below.");

        FileSystemResource file1 = new FileSystemResource(new File(""));
        helper.addAttachment("Txt file", file1);
        ClassPathResource classPathResource = new ClassPathResource("Attachment.pdf");

        helper.addAttachment(classPathResource.getFilename(), classPathResource);
        helper.addAttachment("File name", file1);

        javaMailSender.send(mimeMessage);
    }


    /**
     * This function is used to send mail without attachment.
     *
     * @param user
     * @throws MailException
     */

    public void sendEmail(UserVO user) throws MailException {

        /*
         * This JavaMailSender Interface is used to send Mail in Spring Boot. This
         * JavaMailSender extends the MailSender Interface which contains send()
         * function. SimpleMailMessage Object is required because send() function uses
         * object of SimpleMailMessage as a Parameter
         */

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setSubject("Testing Mail API");
        mail.setText("Hurray ! You have done that dude...");

        /*
         * This send() contains an Object of SimpleMailMessage as an Parameter
         */
        javaMailSender.send(mail);
    }


    public void buildAndSendMail(HttpServletRequest request, UserVO user) throws UserException, IOException {

        String appUrl = HTMLEntities.buildUrl(request, USER_WS);
        StringBuilder sblink = new StringBuilder("<a href=");
        sblink.append(appUrl);
        sblink.append("/confirm?token=");
        sblink.append(user.getConfirmationToken());
        sblink.append(">Confirmation</a>");

        String body = MailType.CONFIRM_TEMPLATE_BODY + sblink.toString();
        List<String> labels = new ArrayList<String>();
        List<String> emails = new ArrayList<String>();

        labels.add(MailType.BODY_KEY);

        emails.add(user.getEmail());


       googleMailSenderService.sendMail(MailType.CONFIRM_TEMPLATE, MailType.CONFIRM_TEMPLATE_TITLE, MailUtils.replace(user, labels, body, null),
                emails, null, null, null, user.getUsername(), null, false);

       /* Response sent = mailSenderSendGrid.sendMailMessage(MailType.CONFIRM_TEMPLATE, MailType.CONFIRM_TEMPLATE_TITLE, MailUtils.replace(user, labels, body, null),
                emails, null, null, null, user.getUsername(), null, false);
*/

    }

    public Response sendMail(MailDTO mr, boolean active) throws Exception {

        UserVO user = userService.findByEmail(mr.getFrom());

        if (user != null) {

            List<String> labels = new ArrayList<String>();

            labels.add(MailType.BODY_KEY);

            return mailSenderSendGrid.sendMailMessage(MailType.SEND_MAIL_TEMPLATE, mr.getSubject(), MailUtils.replace(user, labels, mr.getBody(), null), mr.getTo(), mr.getCc(), mr.getBcc(), mr.getFrom(), user.getUsername(), null, true);
        }
        return null;
    }


}
