package cm.packagemanager.pmanager.common.mail.ent.service;

import cm.packagemanager.pmanager.common.Constants;
import cm.packagemanager.pmanager.common.mail.ent.MailProperties;
import cm.packagemanager.pmanager.common.mail.ent.vo.ContactUSVO;
import cm.packagemanager.pmanager.common.mail.ent.wrapper.MailWrapper;
import cm.packagemanager.pmanager.common.utils.DateUtils;
import cm.packagemanager.pmanager.common.utils.MailUtils;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.common.utils.Utility;
import cm.packagemanager.pmanager.ws.requests.mail.MailDTO;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Personalization;
import com.sendgrid.Response;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class IGoogleMailSenderServiceImpl implements IGoogleMailSenderService {

	@Resource(name = "mailProperties")
	private MailProperties mailProperties ;

	@Override
	public void sendMail() {

	}

	@Override
	public void contactUs(ContactUSVO contactUS, boolean replyToEnabled) {

		contactUS.setReceiver(mailProperties.getDefaultContactUs());
		List<String> emailSendTo = new ArrayList<>();
		emailSendTo.add(contactUS.getReceiver());

		List<String> emailSendCC = new ArrayList<>();
		emailSendCC.add(contactUS.getSender());

		SimpleMailMessage mail = new SimpleMailMessage();
		common(mail,contactUS.getSubject(), contactUS.getSender(),contactUS.getContent(), emailSendTo, emailSendCC, null,true);
		mailProperties.send(mail);


	}

	@Override
	public void sendMail(String templateName, String messageSubject, Map<String, String> variableLabel, List<String> to, List<String> cc, List<String> bcc, String from, String username, String message, boolean replyToEnabled) throws IOException {

		SimpleMailMessage smm =mailToSend(templateName,messageSubject,variableLabel,to,cc,bcc,from,from,message,replyToEnabled);
		mailProperties.send(smm);
	}


	protected SimpleMailMessage  mailToSend(String templateName, String messageSubject, Map<String, String> variableLabel, List<String> emailSendTo, List<String> emailSendCC, List<String> emailSendBCC, String from, String username, String message, boolean replyToEnabled) throws IOException {

		org.springframework.core.io.Resource resource = new ClassPathResource("/templates/" + templateName);

		InputStream emailTemplateStream = resource.getInputStream();

		String emailTemplateString = Utility.convertStreamToString(emailTemplateStream);


		if (variableLabel != null && StringUtils.isEmpty(message)) {
			variableLabel.put(Constants.SUBJECT, messageSubject);

			for (Map.Entry<String, String> entry : variableLabel.entrySet()) {
				String value = entry.getValue().replaceAll("\\$", "\\\\\\$");
				emailTemplateString = emailTemplateString.replaceAll("(?i)" + "%" + entry.getKey() + "%", value);
			}

		}
		SimpleMailMessage mail = new SimpleMailMessage();

		common(mail,messageSubject,from, emailTemplateString, emailSendTo, emailSendCC,emailSendBCC,false);

		return mail;
	}

	private void common(SimpleMailMessage mailMessage, String messageSubject, String from, String message, List<String> emailSendTo, List<String> emailSendCC, List<String> emailSendBCC,boolean replyToEnabled) {


		MailWrapper mwr= new MailWrapper(emailSendTo,emailSendCC,emailSendBCC);

		mailMessage.setTo(mwr.getToArray());
		mailMessage.setCc(mwr.getCcArray());
		mailMessage.setBcc(mwr.getBccArray());
		mailMessage.setSubject(messageSubject);
		mailMessage.setFrom(from);
		mailMessage.setText(message);
		mailMessage.setSentDate(new Date());
		if(BooleanUtils.isTrue(replyToEnabled)){
			mailMessage.setReplyTo(from);
		}


	}
}
