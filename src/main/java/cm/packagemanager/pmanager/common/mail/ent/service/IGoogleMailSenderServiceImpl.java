package cm.packagemanager.pmanager.common.mail.ent.service;

import cm.packagemanager.pmanager.common.mail.CommonMailSenderService;
import cm.packagemanager.pmanager.common.mail.PersonalMailSender;
import cm.packagemanager.pmanager.common.mail.ent.vo.ContactUSVO;
import cm.packagemanager.pmanager.common.mail.ent.wrapper.MailWrapper;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
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
	public void sendMail(String templateName, String messageSubject, Map<String, String> variableLabel, List<String> to, List<String> cc, List<String> bcc, String from, String username, String message, boolean replyToEnabled) throws IOException {

		SimpleMailMessage smm =mailToSend(templateName,messageSubject,variableLabel,to,cc,bcc,from,from,message,replyToEnabled);
		personalMailSender.send(smm);
	}


	protected SimpleMailMessage  mailToSend(String templateName, String messageSubject, Map<String, String> variableLabel, List<String> emailSendTo, List<String> emailSendCC, List<String> emailSendBCC, String from, String username, String message, boolean replyToEnabled) throws IOException {


		String emailTemplateString = buildTemplateMail(templateName,messageSubject,message,variableLabel);

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
		mailMessage.setText(message + template(from,true));
		mailMessage.setSentDate(new Date());
		if(BooleanUtils.isTrue(replyToEnabled)){
			mailMessage.setReplyTo(from);
		}


	}
}
