package cm.packagemanager.pmanager.ws.requests.messages;

import javax.validation.constraints.NotNull;

public class MessageDTO {

	@NotNull(message = "L'id de l'annonce doit etre valorisé")
	private Long announceId;

	@NotNull(message = "Le username / email de l'utilisateur doit etre valorisé")
	private String username;

	private String content;

	public Long getAnnounceId() {
		return announceId;
	}

	public void setAnnounceId(Long announceId) {
		this.announceId = announceId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
