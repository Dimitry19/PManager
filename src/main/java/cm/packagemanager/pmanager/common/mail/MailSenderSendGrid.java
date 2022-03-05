package cm.packagemanager.pmanager.common.mail;


import cm.packagemanager.pmanager.common.Constants;
import cm.packagemanager.pmanager.common.mail.ent.vo.ContactUSVO;
import cm.packagemanager.pmanager.common.utils.FileUtils;
import cm.packagemanager.pmanager.common.utils.MailUtils;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.common.utils.Utility;
import com.sendgrid.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

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

    @Value("${sendgrid.mail.sender.user}")
    private String sender;

    @Value("${sendgrid.mail.sender.password}")
    private String adminPassword;

    @Value("${sendgrid.admin.email}")
    private String adminEmail;

    @Value("${sendgrid.mail.email.bcc}")
    private String emailbcc;

    @Value("${sendgrid.mail.email.cc}")
    private String emailcc;

    @Value("${mail.email.from}")
    private String travelPostPseudo;


    @Value("${sendgrid.api.key}")
    private String SENDGRID_API_KEY;


    String EmailSendAddresses;

    String EmailSendAddressesCC;

    String EmailSendAddressesBCC;


    public MailSenderSendGrid() {

    }


    public Response sendMailMessage(String templateName, String messageSubject, Map<String, String> variableLabel, List<String> emailSendTo, List<String> emailSendCC, List<String> emailSendBCC, String from, String username, String message, boolean replyToEnabled) {
        try {
            final Mail mail = buildMailToSend(templateName, messageSubject, variableLabel, emailSendTo, emailSendCC, emailSendBCC, from, username, message, replyToEnabled);
            final Response response = send(mail);
            return response;
        } catch (Exception e) {
        }

        return null;
    }

    public Response sendMailMessageAttachments(String templateName, String messageSubject, Map<String, String> variableLabel, List<String> emailSendTo, List<String> emailSendCC, List<String> emailSendBCC, String from, String username, String message,String filePath, boolean replyToEnabled) {
        try {
            final Mail mail = buildMailToSend(templateName, messageSubject, variableLabel, emailSendTo, emailSendCC, emailSendBCC, from, username, message, replyToEnabled);
            attachments(mail,filePath);
            final Response response = send(mail);
            return response;
        } catch (Exception e) {
        }

        return null;
    }


    public Response sendMailMessageAttachments(String messageSubject, List<String> emailSendTo, List<String> emailSendCC, List<String> emailSendBCC, String from, String username, String message, String filePath,boolean repyToEnabled) {
        try {
            final Mail mail = buildMailToSend(messageSubject, emailSendTo, emailSendCC, emailSendBCC, from, username, message, repyToEnabled);

            attachments(mail,filePath);

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


    public Mail buildMailToSend(String templateName, String messageSubject, Map<String, String> variableLabel, List<String> emailSendTo, List<String> emailSendCC, List<String> emailSendBCC, String from, String username, String message, boolean repyToEnabled) throws IOException {
        Mail mail = new Mail();
        try {

            Resource resource = new ClassPathResource("/templates/" + templateName);

            InputStream emailTemplateStream = resource.getInputStream();

            String emailTemplateString = Utility.convertStreamToString(emailTemplateStream);


            if (from == null) {
                from = sender;
            }

            genericPersonalizzation(messageSubject, from, travelPostPseudo, username, mail, emailSendTo, emailSendCC, emailSendBCC);
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
                replyTo.setName(travelPostPseudo);
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

            genericPersonalizzation(messageSubject, from, travelPostPseudo, username, mail, emailSendTo, emailSendCC, emailSendBCC);
            Content content = new Content();
            content.setType(TEXT_PLAIN);
            content.setValue(message);
            mail.addContent(content);

        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }

        return mail;
    }


    public Response  sendContactUs(ContactUSVO contactUS, boolean replyToEnabled) throws Exception {
        Mail mail = new Mail();
        try {

            contactUS.setReceiver(adminEmail);
            List<String> emailSendTo = new ArrayList<>();
            emailSendTo.add(contactUS.getReceiver());

            List<String> emailSendCC = new ArrayList<>();
            emailSendCC.add(contactUS.getSender());
            genericPersonalizzation(contactUS.getSubject(), sender, contactUS.getPseudo(),travelPostPseudo, mail, emailSendTo, emailSendCC, null);
            Content content = new Content();
            content.setType(TEXT_PLAIN);
            content.setValue(contactUS.getContent());
            mail.addContent(content);

            if (replyToEnabled) {
                Email replyTo = new Email();
                replyTo.setName(contactUS.getPseudo());
                replyTo.setEmail(contactUS.getSender());
                mail.setReplyTo(replyTo);
            }

        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }

        return send(mail);

    }

    private void genericPersonalizzation(String messageSubject, String from,String pseudo, String username, Mail mail, List<String> emailSendTo, List<String> emailSendCC, List<String> emailSendBCC) {

        EmailSendAddresses = MailUtils.formatEmails(emailSendTo);
        EmailSendAddressesCC = MailUtils.formatEmails(emailSendCC);
        EmailSendAddressesBCC = MailUtils.formatEmails(emailSendBCC);


        String[] emailSendAddressesArray = EmailSendAddresses.split(MailUtils.SEPARATOR);
        String[] emailSendAddressesCCArray = StringUtils.isNotEmpty(EmailSendAddressesCC) ? EmailSendAddressesCC.split(MailUtils.SEPARATOR) : null;
        String[] emailSendAddressesBCCArray = StringUtils.isNotEmpty(EmailSendAddressesBCC) ? EmailSendAddressesBCC.split(MailUtils.SEPARATOR) : null;

        Email fromEmail = new Email();
        fromEmail.setName(pseudo);
        fromEmail.setEmail(from);
        mail.setFrom(fromEmail);
        mail.setSubject(messageSubject);

        Personalization personalization = new Personalization();
        for (int i = 0; i < emailSendAddressesArray.length; i++) {
            Email to = new Email();
            to.setName(username);
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


    private void attachments(Mail mail, String filePath) throws IOException {



        /*
        *   Attachments attachments = new Attachments();
            Base64 x = new Base64();
            String encodedString = x.encodeAsString(loadPdfFromClasspath());
            attachments.setContent(encodedString);
            attachments.setDisposition("attachment");
            attachments.setFilename("xyx.pdf");
            attachments.setType("application/pdf");
            mail.addAttachments(attachments);
    * */

        if (mail == null) return;

        Path file = Paths.get(filePath);
        String type=Files.probeContentType(file);

        if (StringUtils.isEmpty(type)) return;

        byte[] attachmentContentBytes = Files.readAllBytes(file);
        String attachmentContent = Base64.getMimeEncoder().encodeToString(attachmentContentBytes);

        Attachments attachments = new Attachments();
        attachments.setFilename(file.getFileName().toString());
        attachments.setType(type);
        attachments.setDisposition("attachment");
        attachments.setContent(attachmentContent);
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

    protected Response send(final Mail mail) throws Exception {

        if (mail == null) return null;

        final SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        final Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        final Response response = sg.api(request);
        return response;

    }


    void testRemoveDomain() throws IOException {
        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        sg.addRequestHeader("on-behalf-of", "The subuser's username. This header generates the API call as if the subuser account was making the call.");

        Request request = new Request();
        request.setMethod(Method.DELETE);
        request.setEndpoint("/whitelabel/domains/ZGkrHSypTsudrGkmdpJJ");
        Response response = sg.api(request);
        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
        System.out.println(response.getHeaders());
    }

  /*  private Response testSend() throws IOException {

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

    }*/
}