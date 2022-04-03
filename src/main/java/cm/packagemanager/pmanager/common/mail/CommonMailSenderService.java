package cm.packagemanager.pmanager.common.mail;

import cm.packagemanager.pmanager.common.Constants;
import cm.packagemanager.pmanager.common.utils.StringUtils;
import cm.packagemanager.pmanager.common.utils.Utility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.ManagedBean;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@ManagedBean
public class CommonMailSenderService {


    @Value("${mail.email.from}")
    protected String travelPostPseudo;

    @Value("${mail.admin.username}")
    protected String defaultContactUs;



    protected String template(String sender,boolean isNotHtml){
        StringBuilder sb = new StringBuilder("\n");
        if(isNotHtml){
            sb.append("\n");
            sb.append("\n");
            sb.append("\n");
            sb.append("\n");
            sb.append("\n");
            sb.append("\n");

        }
        sb.append(!isNotHtml?"<h3>Veuillez repondre à cette adresse : ":"Veuillez repondre à cette adresse : ");
        sb.append(sender);
        sb.append(!isNotHtml?"</h3>":"");

        return sb.toString();
    }


    protected String buildTemplateMail(String templateName, String messageSubject, String message,Map<String, String> variableLabel) {
        org.springframework.core.io.Resource resource = new ClassPathResource("/templates/" + templateName);


        String emailTemplateString =null;
        try {
            InputStream emailTemplateStream  = resource.getInputStream();
            emailTemplateString = Utility.convertStreamToString(emailTemplateStream);


            if (variableLabel != null && StringUtils.isEmpty(message)) {
                variableLabel.put(Constants.SUBJECT, messageSubject);

                for (Map.Entry<String, String> entry : variableLabel.entrySet()) {
                    String value = entry.getValue().replaceAll("\\$", "\\\\\\$");
                    emailTemplateString = emailTemplateString.replaceAll("(?i)" + "%" + entry.getKey() + "%", value);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return emailTemplateString;

    }
}
