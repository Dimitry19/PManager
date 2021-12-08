package cm.packagemanager.pmanager.common.mail.ent.service;

import cm.packagemanager.pmanager.common.mail.MailSender;
import cm.packagemanager.pmanager.common.mail.ent.dao.ContactUSDAO;
import cm.packagemanager.pmanager.common.mail.ent.vo.ContactUSVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import com.sendgrid.Mail;
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
import java.io.File;
import java.io.IOException;

@Service
public class MailServiceImpl implements MailService {


    private JavaMailSender javaMailSender;
    @Autowired
    MailSender mailSender;

    @Autowired
    ContactUSDAO contactUSDAO;


    @Override
    public Response contactUS(ContactUSVO contactUS) throws MailException, IOException {
        contactUSDAO.save(contactUS);
        final Mail mail = mailSender.buildMail(contactUS, true);
        if (mail == null) return null;
        final Response response = mailSender.send(mail);
        return response;
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

}
