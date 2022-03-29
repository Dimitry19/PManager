package cm.packagemanager.pmanager.common.mail.mailjet;

import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.utils.SecRandom;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;


@Service
public class MailJetSender {

	private Logger logger = LoggerFactory.getLogger(this.getClass());


	@Value("${mail.email.from}")
	protected String travelPostPseudo;

	@Value("${mail.admin.username}")
	protected String defaultContactUs;

	private JavaMailSenderImpl jms;

	@Value("${mail.jet.api.key}")
	protected String mailJetApiKey;

	@Value("${mail.jet.api.secret}")
	protected String mailJetApiSecret;

	@Value("${mail.jet.api.version}")
	protected String mailJetApiVersion;

	@Value("${mail.jet.api.sender}")
	protected String mailJetApiSender;


	private MailjetClient mjClient;



	public String getDefaultContactUs() {
		return defaultContactUs;
	}

	private void setJms(JavaMailSenderImpl jms) {
		this.jms = jms;
	}

	public MailJetSender(){

	}

	public String getTravelPostPseudo() {
		return travelPostPseudo;
	}

	@PostConstruct
	public void init() {
		System.out.println("MailJet service starts....");
		mailJetProperties();
	}

	protected void mailJetProperties(){
		mjClient = new MailjetClient(mailJetApiKey, mailJetApiSecret,new ClientOptions(mailJetApiVersion));
	}


	public boolean send( String emailFrom,String nameFrom, String emailTo, String nameTo,
						 String emailCc, String nameCc, String emailBCc, String nameBCc,
					  String subject, String content,String template,boolean replyTo) throws MailjetSocketTimeoutException, MailjetException {

		MailjetRequest mjRequest= new MailjetRequest(Emailv31.resource);
		buildMjMail(mjRequest, emailFrom, nameFrom, emailTo,nameTo,emailCc,nameCc,emailBCc, nameBCc,subject,content,template,replyTo);
		return send(mjRequest);
	}

	public boolean send(String emailFrom, String nameFrom, Map emailsTo, Map emailsCc,Map emailsBcc,
						String subject, String content, String template, boolean replyTo) throws MailjetSocketTimeoutException, MailjetException {

		MailjetRequest mjRequest= new MailjetRequest(Emailv31.resource);
		buildMjMail(mjRequest, emailFrom, nameFrom, emailsTo,emailsCc,emailsBcc, subject,content,template,replyTo);
		return send(mjRequest);
	}


	private boolean send(MailjetRequest mjRequest) throws MailjetSocketTimeoutException, MailjetException {
		MailjetResponse mjResponse =  mjClient.post(mjRequest);
		return CustomMailJetResponse.managedResponse(mjResponse.getStatus());
	}

	private void buildMjMail(@NotNull MailjetRequest mjRequest, String emailFrom, String nameFrom, String emailTo, String nameTo,
							 String emailCc, String nameCc, String emailBCc, String nameBCc,
							 String subject, String content, String template,boolean replyTo){
		JSONArray jaMessage=new JSONArray();
		JSONObject joFrom= new JSONObject();
		JSONObject joTo= new JSONObject();
		JSONArray jaTos=new JSONArray();
		JSONObject joAll= new JSONObject();
		JSONObject joCc= new JSONObject();
		JSONObject joBcc= new JSONObject();
		JSONArray jaCcs=new JSONArray();
		JSONArray jaBccs=new JSONArray();

		joFrom.put(Emailv31.Message.EMAIL,mailJetApiSender)
				.put(Emailv31.Message.NAME,nameFrom);
		joTo.put(Emailv31.Message.EMAIL,emailTo)
				.put(Emailv31.Message.NAME,nameTo);
		jaTos.put(joTo);


		if(StringUtils.isNotEmpty(emailCc) && StringUtils.isNotEmpty(nameCc)){
			joCc.put(Emailv31.Message.EMAIL,emailCc)
					.put(Emailv31.Message.NAME,nameCc);
			jaCcs.put(joCc);
			joAll.put(Emailv31.Message.CC,jaTos);
		}
		if(StringUtils.isNotEmpty(emailBCc) && StringUtils.isNotEmpty(nameBCc)){
			joBcc.put(Emailv31.Message.EMAIL,emailBCc)
					.put(Emailv31.Message.NAME,nameBCc);
			jaBccs.put(joBcc);
			joAll.put(Emailv31.Message.BCC,jaTos);
		}

		if(replyTo){
			JSONObject joReplyTo= new JSONObject();
			joReplyTo.put(Emailv31.Message.REPLYTO,emailFrom);
			joAll.put(Emailv31.Message.HEADERS,joReplyTo);
		}

		joAll.put(Emailv31.Message.FROM,joFrom);
		joAll.put(Emailv31.Message.TO,jaTos);
		joAll.put(Emailv31.Message.SUBJECT, subject);
		checkAndFillAttributes(content, template, generateCustomerId(), joAll);

		jaMessage.put(joAll);
		mjRequest.property(Emailv31.MESSAGES, jaMessage);
	}

	private void checkAndFillAttributes(String content, String template, String customerId, JSONObject joAll) {
		if(StringUtils.isNotEmpty(content)){
			joAll.put(Emailv31.Message.TEXTPART, content);
		}

		if(StringUtils.isNotEmpty(template)){
			joAll.put(Emailv31.Message.HTMLPART, template);
		}
		if(StringUtils.isNotEmpty(customerId)){
			joAll.put(Emailv31.Message.CUSTOMID, customerId);
		}
	}


	private void buildMjMail(MailjetRequest mjRequest,String emailFrom,String nameFrom,  Map<String,String> emailsTo, Map emailsCc,Map emailsBcc,
							 String subject, String content,String template,boolean replyTo){

		JSONArray jaMessage=new JSONArray();
		JSONObject joFrom= new JSONObject();
		JSONObject joAll= new JSONObject();

		joFrom.put(Emailv31.Message.EMAIL,mailJetApiSender)
				.put(Emailv31.Message.NAME,nameFrom);

		JSONArray jaTos =fillDestinations(emailsTo);
		JSONArray jaCcs=fillDestinations(emailsCc);
		JSONArray jaBCcs=fillDestinations(emailsBcc);

		joAll.put(Emailv31.Message.SUBJECT, subject);
		joAll.put(Emailv31.Message.FROM,joFrom);
		joAll.put(Emailv31.Message.TO,jaTos);
		joAll.put(Emailv31.Message.CC,jaCcs);
		joAll.put(Emailv31.Message.BCC,jaBCcs);



		checkAndFillAttributes(content, template, generateCustomerId(), joAll);
		if(replyTo){
			JSONObject joReplyTo= new JSONObject();
			joReplyTo.put(Emailv31.Message.REPLYTO,emailFrom);
			joAll.put(Emailv31.Message.HEADERS,joReplyTo);
		}


		JSONObject joAttachment= new JSONObject();
		joAttachment.put("ContentType","text/plain")
						.put("Filename","test.txt")
								.put("Base64Content", "VGhpcyBpcyB5b3VyIGF0dGFjaGVkIGZpbGUhISEK");
		JSONArray jaAttachment=new JSONArray();
		jaAttachment.put(joAttachment);
		joAll.put(Emailv31.Message.ATTACHMENTS,jaAttachment);


		jaMessage.put(joAll);
		mjRequest.property(Emailv31.MESSAGES, jaMessage);

	}

	private void buildMjMailWithAttachment(MailjetRequest mjRequest,String emailFrom,String nameFrom,  Map<String,String> emailsTo, Map emailsCc,Map emailsBcc,
							 String subject, String content,String template,boolean replyTo, Object o){


		if(o instanceof File){
			File attachment=(File)o;

		}
		buildMjMail(mjRequest, emailFrom, nameFrom, emailsTo,emailsCc,emailsBcc, subject,content,template,replyTo);

	}


	private JSONArray fillDestinations(Map<String, String> emails){

		JSONArray jaTos = new JSONArray();
		if(emails!=null){
			emails.entrySet().forEach(e->{
				JSONObject joTo= new JSONObject();
				joTo.put(Emailv31.Message.EMAIL,e.getKey())
						.put(Emailv31.Message.NAME,e.getValue());
				jaTos.put(joTo);

			});
		}
		return jaTos;
	}
	private String generateCustomerId(){
		return SecRandom.randomString(8);
	}
}
