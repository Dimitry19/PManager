package cm.packagemanager.pmanager.common.mail;


import cm.packagemanager.pmanager.common.Constants;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.common.utils.Utility;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import com.sendgrid.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.sendgrid.SendGridAutoConfiguration;
import org.springframework.boot.autoconfigure.sendgrid.SendGridProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


/*https://github.com/sendgrid/sendgrid-java/*/

@Service
public class MailSender {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(MailSender.class);


	@Value("${mail.smtp.port}")
	private String PORT;

	@Value("${mail.smtp.host}")
	private String HOST;

	@Value("${mail.smtp.starttls.enable}")
	private boolean TLS;

	@Value("${mail.smtp.auth}")
	private boolean AUTH;

	@Value("${mail.admin.username}")
	private String adminUsername;

	@Value("${mail.admin.password}")
	private String adminPassword;

	@Value("${mail.admin.email}")
	private String adminEmail;

	@Value("${mail.email.bcc}")
	private String emailbcc;

	@Value("${mail.email.cc}")
	private String emailcc;

	@Value("${mail.email.from}")
	private String fromName;
	
	
	@Value("${sendgrid.api.key}")
	private String SENDGRID_API_KEY;


	Properties properties;


	//@Autowired
	ResourceLoader resourceLoader;

	String EmailSendAddresses  ;

	String EmailSendAddressesCC ;

	String EmailSendAddressesBCC ;

	private final static String SEPARATOR =";";

	public  MailSender(){

	}

	/*
	public boolean sendMailMessage(String templateName, String messageSubject, Map<String, String> variableLabel, List<String> emailSendTo, List<String> emailSendCC,List<String> emailSendBCC,String from,String username) {

		loadProperties();
		Authenticator authenticator = new SMTPAuthenticator("packagemanager2020@gmail.com", "pmanager2020");

		Session mailSession;
		try	{
			mailSession = Session.getDefaultInstance(properties, authenticator);
			logger.debug(String.format("Sending a message To:%s CC:%s BCC:%s", emailSendTo, emailcc,emailbcc));

			MimeMessage message = new MimeMessage(mailSession);
			if(from==null){
				from="noreply@travel.com";
				username ="No reply";
			}

			message.setFrom(new InternetAddress(from, username));
			Resource resource = new ClassPathResource("/templates/"+templateName);
			InputStream emailTemplateStream = resource.getInputStream();
			String emailTemplateString =Utility.convertStreamToString(emailTemplateStream);
			message.setSubject(messageSubject);
			String subj=messageSubject.toString();

			try{
				variableLabel.put("SUBJECT",subj );
				for (Map.Entry<String, String> entry : variableLabel.entrySet()){
					String value = entry.getValue().replaceAll("\\$", "\\\\\\$");
					// Sostituzione case-insensitive mediante espressione
					// regolare , ignora la differenza tra maiuscole e minuscoli
					emailTemplateString = emailTemplateString.replaceAll("(?i)" + "%" + entry.getKey() + "%", value);
				}
			}
			catch (Exception e){
			}

			message.setContent(emailTemplateString, "text/html");
			EmailSendAddresses=formatEmails(emailSendTo);
			EmailSendAddressesCC=formatEmails(emailSendCC);
			EmailSendAddressesBCC=formatEmails(emailSendBCC);
			String[] EmailSendAddressesArray = EmailSendAddresses.split(SEPARATOR);
			String[] EmailSendAddressesCCArray=null;
			String[] EmailSendAddressesBCCArray=null;

			if(EmailSendAddressesCC!=null){
			    EmailSendAddressesCCArray = EmailSendAddressesCC.split(SEPARATOR);
			}

			if(EmailSendAddressesBCC!=null){
				EmailSendAddressesBCCArray = EmailSendAddressesBCC.split(SEPARATOR);
			}


			for (int i = 0; i < EmailSendAddressesArray.length; i++)
			{
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(EmailSendAddressesArray[i]));
			}

			if(EmailSendAddressesCC!=null){
				for (int i = 0; i < EmailSendAddressesCCArray.length; i++) {
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(EmailSendAddressesCCArray[i]));
				}
			}

			if(EmailSendAddressesBCC!=null){
				for (int i = 0; i < EmailSendAddressesBCCArray.length; i++) {
					message.addRecipient(Message.RecipientType.BCC, new InternetAddress(EmailSendAddressesBCCArray[i]));
				}
			}

			Transport.send(message);

			logger.info(String.format("Email send completed with templateName[%s].", templateName));
			return true;
		}
		catch (Exception e)
		{
			logger.error(String.format("Error sending email with templateName[%s], error[%s].", templateName, e.getMessage()));
			e.printStackTrace();
		}
		return false;
	}*/

	private  void  loadProperties(){

		properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", 587);
		properties.put("mail.transport.protocol", "smtp");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.debug", "true");

	}

	private  String  formatEmails(List<String> emails){

		if (emails==null)
			return null;

		StringBuilder sb=new StringBuilder();

		for(String email:emails) {
			sb.append(email);
			sb.append(SEPARATOR);
		}
		return sb.toString();

	}


	public static Map<String, String> replace(UserVO user, List<String> labels,String body,String password){

		Map<String, String> variableLabel = new HashMap<String, String>();

		for(String label : labels){

			if(user!=null && !label.equals(MailType.BODY_KEY)){

				if(label.equals(MailType.USERNAME_KEY)){
					variableLabel.put(label, user.getFirstName());
				}


				if(label.equals(MailType.PASSWORD_KEY)){
					variableLabel.put(label, password);
				}

			}else{
				variableLabel.put(label, body);
			}

		}

		return variableLabel;
	}

	class SMTPAuthenticator extends javax.mail.Authenticator{
		String username;
		String password;

		public SMTPAuthenticator(String username, String password){
			this.username = username;
			this.password = password;
		}
		public PasswordAuthentication getPasswordAuthentication(){

			return new PasswordAuthentication(username, password);
		}
	}
	

	public Response sendMailMessage(String templateName, String messageSubject, Map<String, String> variableLabel, List<String> emailSendTo, List<String> emailSendCC,List<String> emailSendBCC,String from,String username,String message, boolean repyToEnabled){
		try {
			final Mail mail =buildMailToSend(templateName, messageSubject, variableLabel,  emailSendTo, emailSendCC,emailSendBCC, from, username, message, repyToEnabled);
				final Response response= send(mail);
				return response;
		}catch (Exception e){}

		return null;
	}

	private  Response send(final Mail mail) throws IOException {
		final SendGrid sg = new SendGrid(SENDGRID_API_KEY);
		//sg.addRequestHeader("X-Mock", "true");

		final Request request = new Request();
		request.setMethod(Method.POST);
		request.setEndpoint("mail/send");
		request.setBody(mail.build());

		final Response response = sg.api(request);
		return response;

	}
	public  Mail buildMailToSend(String templateName, String messageSubject, Map<String, String> variableLabel, List<String> emailSendTo, List<String> emailSendCC,List<String> emailSendBCC,String from,String username, String message,boolean repyToEnabled) throws IOException {
		Mail mail = new Mail();
		try{
			EmailSendAddresses = formatEmails(emailSendTo);
			EmailSendAddressesCC = formatEmails(emailSendCC);
			EmailSendAddressesBCC = formatEmails(emailSendBCC);


			String[] EmailSendAddressesArray = EmailSendAddresses.split(SEPARATOR);
			String[] EmailSendAddressesCCArray = StringUtils.isNotEmpty(EmailSendAddressesCC) ? EmailSendAddressesCC.split(SEPARATOR) : null;
			String[] EmailSendAddressesBCCArray = StringUtils.isNotEmpty(EmailSendAddressesBCC) ? EmailSendAddressesBCC.split(SEPARATOR) : null;

			Resource resource = new ClassPathResource("/templates/" + templateName);

			InputStream emailTemplateStream = resource.getInputStream();

			String emailTemplateString = Utility.convertStreamToString(emailTemplateStream);

			if (from == null) {
				from = adminEmail;
			}

			Email fromEmail = new Email();
			fromEmail.setName(fromName);
			fromEmail.setEmail(from);
			mail.setFrom(fromEmail);
			mail.setSubject(messageSubject);

			Personalization personalization = new Personalization();
			for (int i = 0; i < EmailSendAddressesArray.length; i++) {
				Email to = new Email();
				to.setName(username);
				to.setEmail(EmailSendAddressesArray[i]);
				personalization.addTo(to);
			}

			if (StringUtils.isNotEmpty(EmailSendAddressesBCC)) {
				for (int i = 0; i < EmailSendAddressesCCArray.length; i++) {
					Email cc = new Email();
					cc.setName(EmailSendAddressesCCArray[i]);
					cc.setEmail(EmailSendAddressesCCArray[i]);
					personalization.addCc(cc);
				}
			}
			if (StringUtils.isNotEmpty(EmailSendAddressesBCC)) {
				for (int i = 0; i < EmailSendAddressesBCCArray.length; i++) {
					Email bcc = new Email();
					bcc.setName(EmailSendAddressesBCCArray[i]);
					bcc.setEmail(EmailSendAddressesBCCArray[i]);
					personalization.addBcc(bcc);
				}
			}

			mail.addPersonalization(personalization);
			Content content = new Content();


			if(variableLabel!=null && StringUtils.isEmpty(message)){
				variableLabel.put(Constants.SUBJECT, messageSubject.toString());

				for (Map.Entry<String, String> entry : variableLabel.entrySet()) {
					String value = entry.getValue().replaceAll("\\$", "\\\\\\$");
					emailTemplateString = emailTemplateString.replaceAll("(?i)" + "%" + entry.getKey() + "%", value);
				}
				content.setType("text/html");
				content.setValue(emailTemplateString);
			}else {
				content.setType("text/plain");
				content.setValue(message);
			}
			mail.addContent(content);

			repyToEnabled = true;
			if (repyToEnabled) {
				Email replyTo = new Email();
				replyTo.setName(fromName);
				replyTo.setEmail(adminEmail);
				mail.setReplyTo(replyTo);
			}

		}catch (Exception e){
			logger.info(e.getMessage());
			e.printStackTrace();
		}

		return mail;
	  }
	
	
	private void hanbleAttachments(Mail mail, String type, String disposition,  String filePath,String content) throws IOException {
		
		if(mail==null) return;
		if(content==null) return;
		if(StringUtils.isEmpty(type)) return;
		if(StringUtils.isEmpty(disposition)) return;

		Path file = Paths.get(filePath);
		Attachments attachments = new Attachments();
		attachments.setFilename(file.getFileName().toString());

		byte[] attachmentContentBytes = Files.readAllBytes(file);
		String attachmentContent = Base64.getMimeEncoder().encodeToString(attachmentContentBytes);
		attachments.setContent(attachmentContent);
		mail.addAttachments(attachments);
	    attachments.setContent(content);
	    attachments.setType(type);
	    attachments.setDisposition(disposition);
	    mail.addAttachments(attachments);
		
	}


	public boolean manageResponse(final Response response){

		if(response==null){
			return false;
		}
		switch (response.getStatusCode()){
			case 200:
			case 201:
			case 202:
			case 203:
				return true;
			default:return false;
		}
	}

	private Response testSend() throws IOException {
		
		    Email from = new Email("packagemanager2020@gmail.com");
		    String subject = "Sending with Twilio SendGrid is Fun";
		    Email to = new Email("dimipasc@yahoo.fr");
		    Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
		    Mail mail = new Mail(from, subject, to, content);

		    SendGrid sg = new SendGrid(SENDGRID_API_KEY);
		    Request request = new Request();
		    try {
		      request.setMethod(Method.POST);
		      request.setEndpoint("mail/send");
		      request.setBody(mail.build());
		    return sg.api(request);

		    } catch (IOException ex) {
		      throw ex;
		    }


		       /*
		    personalization.addHeader("X-Test", "test");
		    personalization.addHeader("X-Mock", "true");
		    personalization.addSubstitution("%name%", "Example User");
		    personalization.addSubstitution("%city%", "Riverside");
		    personalization.addCustomArg("user_id", "343");
		    personalization.addCustomArg("type", "marketing");
		    personalization.setSendAt(1443636843);
			*/
	}
}