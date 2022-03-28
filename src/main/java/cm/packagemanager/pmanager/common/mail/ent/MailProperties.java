package cm.packagemanager.pmanager.common.mail.ent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;





// [START simple_includes]
import java.io.IOException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
// [END simple_includes]

// [START multipart_includes]
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import javax.activation.DataHandler;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
// [END multipart_includes]



@Configuration("mailProperties")
public class MailProperties {


	public static final String TEXT_PLAIN = "text/plain";


	@Value("${mail.smtp.host}")
	protected String HOST;

	@Value("${mail.smtp.port}")
	protected int PORT;

	@Value("${mail.transport.protocol}")
	protected String PROTOCOL;

	@Value("${mail.debug}")
	protected boolean DEBUG;

	@Value("${mail.smtp.auth}")
	protected boolean AUTH;

	@Value("${mail.smtp.starttls.enable}")
	protected boolean TLS;

	@Value("${mail.smtp.starttls.required}")
	protected boolean REQUIRED_TLS;

	@Value("${mail.smtp.ssl.enable}")
	protected boolean SSL_ENABLED;

	@Value("${mail.admin.username}")
	protected String ADMIN_USERNAME;

	@Value("${mail.admin.password}")
	protected String ADMIN_PASS;

	@Value("${mail.email.from}")
	protected String travelPostPseudo;

	private JavaMailSenderImpl jms;


	private void setJms(JavaMailSenderImpl jms) {
		this.jms = jms;
	}

	public MailProperties(){

	}

	public String getTravelPostPseudo() {
		return travelPostPseudo;
	}

	@PostConstruct
	public void init() {

		System.out.println("Mail service starts....");
		mailProperties();
	}

	public MailProperties mailProperties() {
		MailProperties mailProperties = new MailProperties();

		JavaMailSenderImpl jms = new JavaMailSenderImpl();
		Properties  mailProp = jms.getJavaMailProperties();

		jms.setHost(HOST);
		jms.setPort(PORT);
		jms.setUsername(ADMIN_USERNAME);
		jms.setPassword(ADMIN_PASS);

		mailProp.put("mail.transport.protocol",  PROTOCOL);
		mailProp.put("mail.smtp.auth", AUTH);
		mailProp.put("mail.smtp.starttls.enable", TLS);
		mailProp.put("mail.smtp.starttls.required", REQUIRED_TLS);
		mailProp.put("mail.debug", DEBUG);
		mailProp.put("mail.smtp.ssl.enable",SSL_ENABLED);
		jms.setJavaMailProperties(mailProp);

		setJms(jms);
		return mailProperties;
	}

	public void send(SimpleMailMessage mail){

		jms.send(mail);
	}

	public void sendEmailWithAttachment() throws MessagingException, IOException {

		MimeMessage msg = jms.createMimeMessage();

		// true = multipart message
		MimeMessageHelper helper = new MimeMessageHelper(msg, true);

		helper.setTo("to_@email");

		helper.setSubject("Testing from Spring Boot");

		// default = text/plain
		//helper.setText("Check attachment for image!");

		// true = text/html
		helper.setText("<h1>Check attachment for image!</h1>", true);

		// hard coded a file path
		//FileSystemResource file = new FileSystemResource(new File("path/android.png"));

		helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));

		jms.send(msg);

	}

	public void sendSimpleMail() {
		// [START simple_example]
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("admin@example.com", "Example.com Admin"));
			msg.addRecipient(Message.RecipientType.TO,	new InternetAddress("dimipasc@yahoo.fr", "Mr. User"));
			msg.addRecipient(Message.RecipientType.CC,	new InternetAddress("user@example.com", "Mr. User"));
			msg.setSubject("Your Example.com account has been activated");
			msg.setText("This is a test");
			Transport.send(msg);
		} catch (AddressException e) {

		} catch (MessagingException e) {

		} catch (UnsupportedEncodingException e) {

		}
		// [END simple_example]
	}


	private void sendMultipartMail() {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String msgBody = "...";

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("admin@example.com", "Example.com Admin"));
			msg.addRecipient(Message.RecipientType.TO,
					new InternetAddress("user@example.com", "Mr. User"));
			msg.setSubject("Your Example.com account has been activated");
			msg.setText(msgBody);

			// [START multipart_example]
			String htmlBody = "";          // ...
			byte[] attachmentData = null;  // ...
			Multipart mp = new MimeMultipart();

			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlBody, "text/html");
			mp.addBodyPart(htmlPart);

			MimeBodyPart attachment = new MimeBodyPart();
			InputStream attachmentDataStream = new ByteArrayInputStream(attachmentData);
			attachment.setFileName("manual.pdf");
			attachment.setContent(attachmentDataStream, "application/pdf");
			mp.addBodyPart(attachment);

			msg.setContent(mp);
			// [END multipart_example]

			Transport.send(msg);

		} catch (AddressException e) {
			// ...
		} catch (MessagingException e) {
			// ...
		} catch (UnsupportedEncodingException e) {
			// ...
		}
	}


}
