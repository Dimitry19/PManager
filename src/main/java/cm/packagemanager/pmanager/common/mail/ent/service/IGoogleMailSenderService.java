package cm.packagemanager.pmanager.common.mail.ent.service;


import cm.packagemanager.pmanager.common.mail.ent.vo.ContactUSVO;
import cm.packagemanager.pmanager.ws.requests.mail.MailDTO;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.sendgrid.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IGoogleMailSenderService{

	public void sendMail();

	public void contactUs(ContactUSVO contactUS, boolean replyToEnabled) throws MailjetSocketTimeoutException, MailjetException;

	public boolean contactUs(ContactUSVO contactUS) throws MailjetSocketTimeoutException, MailjetException;

	public void sendMail(String templateName, String messageSubject, Map<String, String> variableLabel, List<String> emailSendTo, List<String> emailSendCC, List<String> emailSendBCC, String from, String username, String message, boolean replyToEnabled) throws IOException;

}
