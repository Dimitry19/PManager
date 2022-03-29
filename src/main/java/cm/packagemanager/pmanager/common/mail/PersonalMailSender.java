package cm.packagemanager.pmanager.common.mail;

import cm.packagemanager.pmanager.common.mail.mailjet.MailJetSender;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import javax.mail.MessagingException;


@Component("personalMailSender")
public class PersonalMailSender {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

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

	@Value("${mail.admin.username}")
	protected String defaultContactUs;

	private JavaMailSenderImpl jms;

	@Autowired
	private MailJetSender mailJetSender;




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

	public boolean send(String emailFrom,String nameFrom, String emailTo, String nameTo, String emailCc,String nameCc, String emailBCc, String nameBCc,
						  String subject, String content,String template, boolean replyTo) throws MailjetSocketTimeoutException, MailjetException {


		return mailJetSender.send(emailFrom, nameFrom,  emailTo,  nameTo, emailCc,  nameCc,  emailBCc,  nameBCc, subject,  content, template,replyTo);
	}

}
