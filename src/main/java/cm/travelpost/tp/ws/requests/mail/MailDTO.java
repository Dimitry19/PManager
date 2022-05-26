package cm.travelpost.tp.ws.requests.mail;

import javax.validation.constraints.NotNull;
import java.util.List;

public class MailDTO {

    private String body;

    @NotNull(message = "Valoriser le destinataire")
    private List<String> to;

    private List<String> cc;

    private List<String> bcc;

    @NotNull(message = "Valoriser l'expediteur")
    private String from;

    @NotNull(message = "Valoriser l'objet du mail")
    private String subject;

    private List<String> attachements;


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getAttachements() {
        return attachements;
    }

    public void setAttachements(List<String> attachements) {
        this.attachements = attachements;
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public void setBcc(List<String> bcc) {
        this.bcc = bcc;
    }
}
