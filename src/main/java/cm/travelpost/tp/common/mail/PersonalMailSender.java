package cm.travelpost.tp.common.mail;

import cm.travelpost.tp.common.mail.mailjet.MailJetSender;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;


@Component("personalMailSender")
public class PersonalMailSender extends CommonMailSenderService{

	private Logger logger = LoggerFactory.getLogger(PersonalMailSender.class);

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



	@Autowired
	private MailJetSender mailJetSender;

	private Session session;

	private JavaMailSenderImpl jms;




	public String getDefaultContactUs() {
		return defaultContactUs;
	}

	private void setJms(JavaMailSenderImpl jms) {
		this.jms = jms;
	}

	public PersonalMailSender(){

	}

	public String getTravelPostPseudo() {
		return travelPostPseudo;
	}

	@PostConstruct
	public void init() {
		System.out.println("Personal Mail service starts....");
		loadProperties();
	}

	protected void loadProperties() {
		//PersonalMailSender personalMailSender = new PersonalMailSender();

		JavaMailSenderImpl jms = new JavaMailSenderImpl();
		Properties  mailProp = jms.getJavaMailProperties();
		//session = Session.getDefaultInstance(mailProp, new MailUtils.SMTPAuthenticator(ADMIN_USERNAME,ADMIN_PASS));

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

	}

	public Session getSession() {
		return session;
	}

	public void send(SimpleMailMessage mail){
		jms.send(mail);
	}

	public void sendEmailWithAttachment() throws MessagingException {

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

	public boolean contactUs(String emailFrom,String nameFrom, String emailTo, String nameTo, String emailCc,String nameCc, String emailBCc, String nameBCc,
						String subject, String content,String template, boolean replyTo) throws MailjetSocketTimeoutException, MailjetException {

		return send(emailFrom, nameFrom,  emailTo,  nameTo, emailCc,  nameCc,  emailBCc,  nameBCc, subject,  content, template,replyTo);
	}

	/**
	 * Send mail
	 * @param emailFrom
	 * @param nameFrom
	 * @param emailTo
	 * @param nameTo
	 * @param emailCc
	 * @param nameCc
	 * @param emailBCc
	 * @param nameBCc
	 * @param subject
	 * @param content
	 * @param template
	 * @param replyTo
	 * @return
	 * @throws MailjetSocketTimeoutException
	 * @throws MailjetException
	 */
	public boolean send(String emailFrom,String nameFrom, String emailTo, String nameTo, String emailCc,String nameCc, String emailBCc, String nameBCc,
						  String subject, String content,String template, boolean replyTo) throws MailjetSocketTimeoutException, MailjetException {

		return mailJetSender.send(emailFrom, nameFrom,  emailTo,  nameTo, emailCc,  nameCc,  emailBCc,  nameBCc, subject,  content, template,replyTo);
	}

	/**
	 * Send Mail With attachment as last parameter
	 * @param templateName
	 * @param messageSubject
	 * @param variableLabel
	 * @param emailTo
	 * @param emailCc
	 * @param emailBCc
	 * @param from
	 * @param message
	 * @param replyTo
	 * @param attachment
	 * @return
	 * @throws MailjetSocketTimeoutException
	 * @throws MailjetException
	 * @throws IOException
	 */
	public boolean  send(String templateName, String messageSubject, Map<String, String> variableLabel, Map<String,String> emailTo, Map<String,String> emailCc, Map<String,String>emailBCc, String from, String message, boolean replyTo, Object attachment) throws MailjetSocketTimeoutException, MailjetException, IOException {


		String emailTemplateString = buildTemplateMail(templateName,messageSubject,message,variableLabel);
		return mailJetSender.send(from, null,  emailTo,   emailCc,  emailBCc, messageSubject,  null, emailTemplateString,replyTo,attachment);

	}

}
