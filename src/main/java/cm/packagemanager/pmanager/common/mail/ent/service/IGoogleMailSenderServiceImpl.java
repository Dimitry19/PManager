package cm.packagemanager.pmanager.common.mail.ent.service;

import cm.packagemanager.pmanager.common.Constants;
import cm.packagemanager.pmanager.common.mail.PersonalMailSender;
import cm.packagemanager.pmanager.common.mail.ent.vo.ContactUSVO;
import cm.packagemanager.pmanager.common.mail.ent.wrapper.MailWrapper;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.common.utils.Utility;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class IGoogleMailSenderServiceImpl implements IGoogleMailSenderService {

	@Autowired
	private PersonalMailSender personalMailSender;

	@Override
	public void sendMail() {

	}

	@Override
	public void contactUs(ContactUSVO contactUS, boolean replyToEnabled) throws MailjetSocketTimeoutException, MailjetException {
		contactUs(contactUS);
	}


	@Override
	public boolean contactUs(ContactUSVO contactUS) throws MailjetSocketTimeoutException, MailjetException {

		contactUS.setReceiver(personalMailSender.getDefaultContactUs());
		List<String> emailSendTo = new ArrayList<>();
		emailSendTo.add(contactUS.getReceiver());

		List<String> emailSendCC = new ArrayList<>();
		emailSendCC.add(contactUS.getSender());

		SimpleMailMessage mail = new SimpleMailMessage();
		common(mail,contactUS.getSubject(), contactUS.getSender(),contactUS.getContent(), emailSendTo, emailSendCC, null,true);
		//personalMailSender.send(mail);

		return personalMailSender.send(contactUS.getSender(), contactUS.getPseudo(),contactUS.getReceiver(), personalMailSender.getTravelPostPseudo(),null, null,
				null,null,contactUS.getSubject(),contactUS.getContent(), null,true);

	}

	@Override
	public void sendMail(String templateName, String messageSubject, Map<String, String> variableLabel, List<String> to, List<String> cc, List<String> bcc, String from, String username, String message, boolean replyToEnabled) throws IOException {

		SimpleMailMessage smm =mailToSend(templateName,messageSubject,variableLabel,to,cc,bcc,from,from,message,replyToEnabled);
		personalMailSender.send(smm);
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
