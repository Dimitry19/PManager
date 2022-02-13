package cm.packagemanager.pmanager.common.mail;


import cm.packagemanager.pmanager.common.Constants;
import cm.packagemanager.pmanager.common.mail.ent.vo.ContactUSVO;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.common.utils.Utility;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import com.sendgrid.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import javax.mail.PasswordAuthentication;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


/*https://github.com/sendgrid/sendgrid-java/*/

@Service
public class MailSenderSendGrid {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(MailSenderSendGrid.class);


    private static final String TEXT_PLAIN = "text/plain";
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

    String EmailSendAddresses;

    String EmailSendAddressesCC;

    String EmailSendAddressesBCC;

    private final static String SEPARATOR = ";";

    public MailSenderSendGrid() {

    }


    private void loadProperties() {

        properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", 587);
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");

    }

    private String formatEmails(List<String> emails) {

        if (emails == null)
            return null;

        StringBuilder sb = new StringBuilder();

        for (String email : emails) {
            sb.append(email);
            sb.append(SEPARATOR);
        }
        return sb.toString();

    }


    public static Map<String, String> replace(UserVO user, List<String> labels, String body, String password) {

        Map<String, String> variableLabel = new HashMap<String, String>();

        for (String label : labels) {

            if (user != null && !label.equals(MailType.BODY_KEY)) {

                if (label.equals(MailType.USERNAME_KEY)) {
                    variableLabel.put(label, user.getFirstName());
                }


                if (label.equals(MailType.PASSWORD_KEY)) {
                    variableLabel.put(label, password);
                }

            } else {
                variableLabel.put(label, body);
            }

        }

        return variableLabel;
    }

    class SMTPAuthenticator extends javax.mail.Authenticator {
        String username;
        String password;

        public SMTPAuthenticator(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public PasswordAuthentication getPasswordAuthentication() {

            return new PasswordAuthentication(username, password);
        }
    }


    public Response sendMailMessage(String templateName, String messageSubject, Map<String, String> variableLabel, List<String> emailSendTo, List<String> emailSendCC, List<String> emailSendBCC, String from, String username, String message, boolean repyToEnabled) {
        try {
            final Mail mail = buildMailToSend(templateName, messageSubject, variableLabel, emailSendTo, emailSendCC, emailSendBCC, from, username, message, repyToEnabled);
            final Response response = send(mail);
            return response;
        } catch (Exception e) {
        }

        return null;
    }

    public Response sendMailMessage(String messageSubject, List<String> emailSendTo, List<String> emailSendCC, List<String> emailSendBCC, String from, String username, String message, boolean repyToEnabled) {
        try {
            final Mail mail = buildMailToSend(messageSubject, emailSendTo, emailSendCC, emailSendBCC, from, username, message, repyToEnabled);
            final Response response = send(mail);
            return response;
        } catch (Exception e) {
        }

        return null;
    }

    public Response send(final Mail mail) throws IOException {
        final SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        //sg.addRequestHeader("X-Mock", "true");

        final Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        final Response response = sg.api(request);
        return response;

    }

    public Mail buildMailToSend(String templateName, String messageSubject, Map<String, String> variableLabel, List<String> emailSendTo, List<String> emailSendCC, List<String> emailSendBCC, String from, String username, String message, boolean repyToEnabled) throws IOException {
        Mail mail = new Mail();
        try {

            Resource resource = new ClassPathResource("/templates/" + templateName);

            InputStream emailTemplateStream = resource.getInputStream();

            String emailTemplateString = Utility.convertStreamToString(emailTemplateStream);

            if (from == null) {
                from = adminEmail;
            }

            genericPersonalizzation(messageSubject, from, username, mail, emailSendTo, emailSendCC, emailSendBCC);
            Content content = new Content();

            if (variableLabel != null && StringUtils.isEmpty(message)) {
                variableLabel.put(Constants.SUBJECT, messageSubject);

                for (Map.Entry<String, String> entry : variableLabel.entrySet()) {
                    String value = entry.getValue().replaceAll("\\$", "\\\\\\$");
                    emailTemplateString = emailTemplateString.replaceAll("(?i)" + "%" + entry.getKey() + "%", value);
                }
                content.setType("text/html");
                content.setValue(emailTemplateString);
            } else {
                content.setType(TEXT_PLAIN);
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

        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }

        return mail;
    }

    public Mail buildMailToSend(String messageSubject, List<String> emailSendTo, List<String> emailSendCC, List<String> emailSendBCC, String from, String username, String message, boolean repyToEnabled) throws IOException {
        Mail mail = new Mail();
        try {

            genericPersonalizzation(messageSubject, from, username, mail, emailSendTo, emailSendCC, emailSendBCC);
            Content content = new Content();
            content.setType(TEXT_PLAIN);
            content.setValue(message);
            mail.addContent(content);

			/*if (repyToEnabled) {
				Email replyTo = new Email();
				replyTo.setName(fromName);
				replyTo.setEmail(adminEmail);
				mail.setReplyTo(replyTo);
			}*/

        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }

        return mail;
    }


    public Mail buildMail(ContactUSVO contactUS, boolean repyToEnabled) throws MailException {
        Mail mail = new Mail();
        try {

            List<String> emailSendTo = new ArrayList<>();
            emailSendTo.add(contactUS.getReceiver());

            List<String> emailSendCC = new ArrayList<>();
            emailSendCC.add(contactUS.getSender());
            genericPersonalizzation(contactUS.getSubject(), contactUS.getSender(), contactUS.getPseudo(), mail, emailSendTo, emailSendCC, null);
            Content content = new Content();
            content.setType(TEXT_PLAIN);
            content.setValue(contactUS.getContent());
            mail.addContent(content);

            if (repyToEnabled) {
                Email replyTo = new Email();
                replyTo.setName(contactUS.getPseudo());
                replyTo.setEmail(contactUS.getSender());
                mail.setReplyTo(replyTo);
            }

        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return mail;
    }

    private void genericPersonalizzation(String messageSubject, String from, String username, Mail mail, List<String> emailSendTo, List<String> emailSendCC, List<String> emailSendBCC) {

        EmailSendAddresses = formatEmails(emailSendTo);
        EmailSendAddressesCC = formatEmails(emailSendCC);
        EmailSendAddressesBCC = formatEmails(emailSendBCC);


        String[] emailSendAddressesArray = EmailSendAddresses.split(SEPARATOR);
        String[] emailSendAddressesCCArray = StringUtils.isNotEmpty(EmailSendAddressesCC) ? EmailSendAddressesCC.split(SEPARATOR) : null;
        String[] emailSendAddressesBCCArray = StringUtils.isNotEmpty(EmailSendAddressesBCC) ? EmailSendAddressesBCC.split(SEPARATOR) : null;

        Email fromEmail = new Email();
        fromEmail.setName(username);
        fromEmail.setEmail(adminEmail);
        mail.setFrom(fromEmail);
        mail.setSubject(messageSubject);

        Personalization personalization = new Personalization();
        for (int i = 0; i < emailSendAddressesArray.length; i++) {
            Email to = new Email();
            to.setName(adminUsername);
            to.setEmail(emailSendAddressesArray[i]);
            personalization.addTo(to);
        }

        if (StringUtils.isNotEmpty(EmailSendAddressesBCC)) {
            for (int i = 0; i < emailSendAddressesCCArray.length; i++) {
                Email cc = new Email();
                cc.setName(emailSendAddressesCCArray[i]);
                cc.setEmail(emailSendAddressesCCArray[i]);
                personalization.addCc(cc);
            }
        }
        if (StringUtils.isNotEmpty(EmailSendAddressesBCC)) {
            for (int i = 0; i < emailSendAddressesBCCArray.length; i++) {
                Email bcc = new Email();
                bcc.setName(emailSendAddressesBCCArray[i]);
                bcc.setEmail(emailSendAddressesBCCArray[i]);
                personalization.addBcc(bcc);
            }
        }

        mail.addPersonalization(personalization);
    }


    private void hanbleAttachments(Mail mail, String type, String disposition, String filePath, String content) throws IOException {

        if (mail == null) return;
        if (content == null) return;
        if (StringUtils.isEmpty(type)) return;
        if (StringUtils.isEmpty(disposition)) return;

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


    public boolean manageResponse(final Response response) {

        if (response == null) {
            return false;
        }
        switch (response.getStatusCode()) {
            case 200:
            case 201:
            case 202:
            case 203:
                return true;
            default:
                return false;
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