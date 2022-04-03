package cm.packagemanager.pmanager.common.mail.mailjet;

import cm.packagemanager.pmanager.common.mail.CommonMailSenderService;
import cm.packagemanager.pmanager.common.utils.CollectionsUtils;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@Service
public class MailJetSender extends CommonMailSenderService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());


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
						String subject, String content, String template, boolean replyTo,Object attachment) throws MailjetSocketTimeoutException, MailjetException, IOException {

		MailjetRequest mjRequest= new MailjetRequest(Emailv31.resource);
		buildMjMail(mjRequest, emailFrom, nameFrom, emailsTo,emailsCc,emailsBcc, subject,content,template,replyTo,attachment);
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
			joAll.put(Emailv31.Message.CC,jaCcs);
		}
		if(StringUtils.isNotEmpty(emailBCc) && StringUtils.isNotEmpty(nameBCc)){
			joBcc.put(Emailv31.Message.EMAIL,emailBCc)
					.put(Emailv31.Message.NAME,nameBCc);
			jaBccs.put(joBcc);
			joAll.put(Emailv31.Message.BCC,jaBccs);
		}

		if(replyTo){
			//replyTo(emailFrom, nameFrom,joAll);
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
			joAll.put(Emailv31.Message.TEXTPART, content+"\n\n\n");
		}

		if(StringUtils.isNotEmpty(template)){
			joAll.put(Emailv31.Message.HTMLPART, template);
		}
		if(StringUtils.isNotEmpty(customerId)){
			joAll.put(Emailv31.Message.CUSTOMID, customerId);
		}
	}


	private void buildMjMail(MailjetRequest mjRequest,String emailFrom,String nameFrom,  Map<String,String> emailsTo, Map emailsCc,Map emailsBcc,
							 String subject, String content,String template,boolean replyTo,Object o) throws IOException {

		JSONArray jaMessage=new JSONArray();
		JSONObject joFrom= new JSONObject();
		JSONObject joAll= new JSONObject();

		joFrom.put(Emailv31.Message.EMAIL,mailJetApiSender)
				.put(Emailv31.Message.NAME,StringUtils.isEmpty(nameFrom)?travelPostPseudo:nameFrom);

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
			//replyTo(emailFrom, nameFrom,joAll);
		}


		if(o!=null  && o instanceof MultipartFile){
			MultipartFile attachment=(MultipartFile)o;

			JSONObject joAttachment= new JSONObject();
			joAttachment.put("ContentType",attachment.getContentType())
					.put("Filename",attachment.getOriginalFilename())
					.put("Base64Content",attachment.getBytes());

			/*joAttachment.put("ContentType","text/plain")
					.put("Filename","test.txt")
					.put("Base64Content", "VGhpcyBpcyB5b3VyIGF0dGFjaGVkIGZpbGUhISEK");*/
			JSONArray jaAttachment=new JSONArray();
			jaAttachment.put(joAttachment);

			joAll.put(Emailv31.Message.INLINEDATTACHMENTS,jaAttachment);

		}


		jaMessage.put(joAll);
		mjRequest.property(Emailv31.MESSAGES, jaMessage);

	}

	private void replyTo(String emailFrom, String nameFrom,JSONObject joAll){
		JSONObject joReplyTo= new JSONObject();

		joReplyTo.put(Emailv31.Message.EMAIL,emailFrom)
				.put(Emailv31.Message.NAME,nameFrom);
		joAll.put(Emailv31.Message.REPLYTO,joReplyTo);

	}

	private JSONArray fillDestinations(Map<String, String> emails){

		JSONArray jaTos = new JSONArray();
		if(emails!=null){
			emails.entrySet().forEach(e->{
				JSONObject joTo= new JSONObject();
				joTo.put(Emailv31.Message.EMAIL,e.getValue())
						.put(Emailv31.Message.NAME,e.getKey());
				jaTos.put(joTo);

			});
		}
		return jaTos;
	}

	private JSONArray fillDestinations(List<String> emails){

		JSONArray jaTos = new JSONArray();
		if(CollectionsUtils.isNotEmpty(emails)){
			emails.stream().forEach(e->{
				JSONObject joTo= new JSONObject();
				joTo.put(Emailv31.Message.EMAIL,e);
				jaTos.put(joTo);

			});
		}
		return jaTos;
	}

	private String generateCustomerId(){
		return SecRandom.randomString(8);
	}
}
