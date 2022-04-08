package cm.travelpost.tp.common.mail.ent.service;

import cm.travelpost.tp.common.mail.CommonMailSenderService;
import cm.travelpost.tp.common.mail.PersonalMailSender;
import cm.travelpost.tp.common.mail.ent.vo.ContactUSVO;
import cm.travelpost.tp.common.mail.ent.wrapper.MailWrapper;
import com.sun.mail.smtp.SMTPSendFailedException;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class IGoogleMailSenderServiceImpl extends CommonMailSenderService implements IGoogleMailSenderService {



	@Autowired
	private PersonalMailSender personalMailSender;

	@Override
	public void sendMail() {

	}

	@Override
	public void contactUs(ContactUSVO contactUS, boolean replyToEnabled) throws  Exception {
		contactUs(contactUS);
	}


	@Override
	public boolean contactUs(ContactUSVO contactUS) throws Exception {

		contactUS.setReceiver(personalMailSender.getDefaultContactUs());
		List<String> emailSendTo = new ArrayList<>();
		emailSendTo.add(contactUS.getReceiver());

		List<String> emailSendCC = new ArrayList<>();
		emailSendCC.add(contactUS.getSender());

		SimpleMailMessage mail = new SimpleMailMessage();
		common(mail,contactUS.getSubject(), contactUS.getSender(),contactUS.getContent(), emailSendTo, emailSendCC, null,true);

		personalMailSender.send(mail);
		return true;

	}

	@Override
	public void sendMail(String templateName, String messageSubject, Map<String, String> variableLabel, List<String> to, List<String> cc, List<String> bcc, String from, String username, String message, boolean replyToEnabled) throws IOException, MessagingException {


		String emailTemplateString = buildTemplateMail(templateName,messageSubject,message,variableLabel);


		//MimeMessage mimeMessage=createEmails(to,personalMailSender.getDefaultContactUs(),messageSubject,message, emailTemplateString);

		//sendMessage(new Gmail(), personalMailSender.getDefaultContactUs(),mimeMessage);
		SimpleMailMessage smm =mailToSend(emailTemplateString,messageSubject,to,cc,bcc,from,from,replyToEnabled);

		personalMailSender.send(smm);
	}

	@Override
	public void sendMail(String messageSubject, List<String> to, List<String> cc, List<String> bcc, String from, String username, String message, boolean replyToEnabled) throws IOException, MessagingException, SMTPSendFailedException {


		SimpleMailMessage smm =mailToSend(message,messageSubject,to,cc,bcc,from,from,replyToEnabled);

		personalMailSender.send(smm);
	}


	protected SimpleMailMessage  mailToSend(String  message , String messageSubject, List<String> emailSendTo, List<String> emailSendCC, List<String> emailSendBCC, String from, String username, boolean replyToEnabled) throws IOException {

		SimpleMailMessage mail = new SimpleMailMessage();

		common(mail,messageSubject,from, message, emailSendTo, emailSendCC,emailSendBCC,false);

		return mail;
	}

	private void common(SimpleMailMessage mailMessage, String messageSubject, String from, String message, List<String> emailSendTo, List<String> emailSendCC, List<String> emailSendBCC,boolean replyToEnabled) {


		MailWrapper mwr= new MailWrapper(emailSendTo,emailSendCC,emailSendBCC);

		mailMessage.setTo(mwr.getToArray());
		mailMessage.setCc(mwr.getCcArray());
		mailMessage.setBcc(mwr.getBccArray());
		mailMessage.setSubject(messageSubject);
		mailMessage.setFrom(from);

		StringBuilder sb= new StringBuilder();
		sb.append(message);
		if(replyToEnabled){
			sb.append(template(from,true));
		}

		mailMessage.setText(sb.toString());
		mailMessage.setSentDate(new Date());
		if(BooleanUtils.isTrue(replyToEnabled)){
			mailMessage.setReplyTo(from);
		}
	}






    /**
     * Create a MimeMessage using the parameters provided.
     *
     * @param to email address of the receiver
     * @param from email address of the sender, the mailbox account
     * @param subject subject of the email
     * @param bodyText body text of the email
     * @return the MimeMessage to be used to send email
     * @throws MessagingException
     */
    MimeMessage createEmail(String to, String from, String subject, String bodyText) throws MessagingException {

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }




    /**
     * Create HTML email
     * @param to
     * @param from
     * @param subject
     * @param text
     * @param html
     * @return MimeMessage email (binded)
     */
    MimeMessage createHTMLEmail(String to, String from, String subject, String text, String html) throws MessagingException {

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        Multipart multiPart = new MimeMultipart("alternative");
        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(text, "utf-8");

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(html, "text/html; charset=utf-8");

        multiPart.addBodyPart(textPart);
        multiPart.addBodyPart(htmlPart);
        email.setContent(multiPart);
        return email;
    }


    /**
     * Creates an email with inline Image - refer to gsp which has a single image with html
     * <img src="cid:myimage" />
     *
     * This myimage is then translated locally in this app to c:\temp\images.jpg and sent with
     * html content along side html tag above.
     * @param to
     * @param from
     * @param subject
     * @param text
     * @param html
     * @return
     */
    MimeMessage createInlineEmail(String to, String from, String subject, String text, String html) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        // This mail has 2 part, the BODY and the embedded image
        MimeMultipart multiPart = new MimeMultipart("related");
        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(text, "utf-8");

        BodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(html, "text/html; charset=utf-8");
        multiPart.addBodyPart(htmlPart);
        htmlPart = new MimeBodyPart();
        DataSource fds = new FileDataSource("c:\\\\temp\\\\images.jpg");

        htmlPart.setDataHandler(new DataHandler(fds));
        htmlPart.setHeader("Content-ID", "<myimage>");
        // add image to the multipart
        multiPart.addBodyPart(htmlPart);
        email.setContent(multiPart);
        return email;
    }


    /**
     * Create a MimeMessage using the parameters provided.
     *
     * @param to Email address of the receiver.
     * @param from Email address of the sender, the mailbox account.
     * @param subject Subject of the email.
     * @param bodyText Body text of the email.
     * @param file Path to the file to be attached.
     * @return MimeMessage to be used to send email.
     * @throws MessagingException
     */
    MimeMessage createEmailWithAttachment(String to, String from, String subject, String bodyText, File file)
            throws MessagingException, IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(bodyText, personalMailSender.TEXT_PLAIN);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        mimeBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(file);

        mimeBodyPart.setDataHandler(new DataHandler(source));
        mimeBodyPart.setFileName(file.getName());

        multipart.addBodyPart(mimeBodyPart);
        email.setContent(multipart);

        return email;
    }

    /**
     * Receives a grails uploaded multiPart file
     * runs convert process to convert to real file
     * and then passes to default createEmailWithAttachment
     * @param to
     * @param from
     * @param subject
     * @param bodyText
     * @param file
     * @return
     * @throws MessagingException
     * @throws IOException
     */

    MimeMessage createEmailWithAttachment(String to, String from, String subject, String bodyText, MultipartFile file)
            throws MessagingException, IOException {
        return createEmailWithAttachment(to, from, subject, bodyText, convert(file));
    }

    /**
     * Converts multiPart file to a real file
     * @param file
     * @return
     */
    private File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
        File convFile = new File( multipart.getOriginalFilename());
        multipart.transferTo(convFile);
        return convFile;
    }



}
