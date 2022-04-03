package cm.packagemanager.pmanager.common.mail.ent.wrapper;


import cm.packagemanager.pmanager.common.utils.MailUtils;
import cm.packagemanager.pmanager.common.utils.StringUtils;

import java.util.List;

public class MailWrapper {


    private String to;
    private String cc;
    private String bcc;
    private String[] toArray;
    private String[] ccArray;
    private String[] bccArray;

    public MailWrapper(List to,List cc, List bcc){
        this.to = MailUtils.formatEmails(to);
        this.cc = MailUtils.formatEmails(cc);
        this.bcc = MailUtils.formatEmails(bcc);

        this.toArray = this.to.split(MailUtils.SEPARATOR);
        this.ccArray = StringUtils.isNotEmpty(this.cc) ? this.cc.split(MailUtils.SEPARATOR) : null;
        this.bccArray = StringUtils.isNotEmpty(this.bcc) ? this.bcc.split(MailUtils.SEPARATOR) : null;

    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String[] getToArray() {
        return toArray;
    }

    public void setToArray(String[] toArray) {
        this.toArray = toArray;
    }

    public String[] getCcArray() {
        return ccArray;
    }

    public void setCcArray(String[] ccArray) {
        this.ccArray = ccArray;
    }

    public String[] getBccArray() {
        return bccArray;
    }

    public void setBccArray(String[] bccArray) {
        this.bccArray = bccArray;
    }
}
