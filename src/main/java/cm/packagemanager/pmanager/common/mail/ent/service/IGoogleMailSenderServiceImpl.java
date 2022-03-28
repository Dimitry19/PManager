package cm.packagemanager.pmanager.common.mail.ent.service;

import cm.packagemanager.pmanager.common.mail.ent.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class IGoogleMailSenderServiceImpl implements IGoogleMailSenderService{

	@Resource(name = "mailProperties")
	private MailProperties mailProperties ;

	@Override
	public void sendMail() {


		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo("dimipasc@yahoo.fr");
		mail.setSubject("Testing Mail API");
		mail.setText("Hurray ! You have done that dude...");
		//mailProperties.send(mail);

		mailProperties.sendSimpleMail();
	}
}
