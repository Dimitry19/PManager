package cm.travelpost.tp.ws.requests.messages;

import javax.validation.constraints.NotNull;


public class UpdateMessageDTO {

    private Long id;

    @NotNull(message = "Le username / email de l'utilisateur doit etre valorisé")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
