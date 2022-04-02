package cm.packagemanager.pmanager.common.mail.ent.service;


import cm.packagemanager.pmanager.common.mail.ent.vo.ContactUSVO;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IGoogleMailSenderService{

	public void sendMail();

	public void contactUs(ContactUSVO contactUS, boolean replyToEnabled) throws Exception;

	public boolean contactUs(ContactUSVO contactUS) throws Exception;

	public void sendMail(String templateName, String messageSubject, Map<String, String> variableLabel, List<String> emailSendTo, List<String> emailSendCC, List<String> emailSendBCC, String from, String username, String message, boolean replyToEnabled) throws IOException, MessagingException;

    void sendMail(String messageSubject, List<String> to, List<String> cc, List<String> bcc, String from, String username, String message, boolean replyToEnabled) throws IOException, MessagingException;
}
