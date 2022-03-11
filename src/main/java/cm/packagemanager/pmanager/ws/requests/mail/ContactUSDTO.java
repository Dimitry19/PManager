package cm.packagemanager.pmanager.ws.requests.mail;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class ContactUSDTO {

    @ApiModelProperty(notes = "Email sender", required = true)
    @Email(message = "Email : format non valide")
    @NotNull(message = "Valoriser l'expediteur")
    private String sender;

    //@ApiModelProperty(notes = "Email receiver", required = true)
    //@Email(message = "Email : format non valide")
    //@NotNull(message = "Valoriser le destinataire")
    @Value("${mail.admin.email}")
    private String receiver;

    @ApiModelProperty(notes = "Email subject", required = true)
    @NotNull(message = "Valoriser le sujet")
    private String subject;

    @ApiModelProperty(notes = "Email body", required = true)
    @NotNull(message = "Valoriser le contenu")
    private String content;

    @NotNull(message = "Valoriser le pseudo")
    private String pseudo;

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

  /*  public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
*/
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
