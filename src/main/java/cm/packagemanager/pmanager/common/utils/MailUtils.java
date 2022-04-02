package cm.packagemanager.pmanager.common.utils;

import cm.packagemanager.pmanager.common.mail.MailType;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;

import javax.mail.PasswordAuthentication;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MailUtils {


    public final static String SEPARATOR = ";";


    public static String formatEmails(List<String> emails) {

        if (emails == null)
            return null;

        StringBuilder sb = new StringBuilder();

        for (String email : emails) {
            sb.append(email);
            sb.append(SEPARATOR);
        }
        return sb.toString();

    }

    public static class SMTPAuthenticator extends javax.mail.Authenticator {
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
}
