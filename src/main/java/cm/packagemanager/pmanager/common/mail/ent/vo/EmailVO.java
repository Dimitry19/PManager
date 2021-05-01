package cm.packagemanager.pmanager.common.mail.ent.vo;

import cm.packagemanager.pmanager.common.Constants;
import cm.packagemanager.pmanager.common.ent.vo.CommonVO;
import cm.packagemanager.pmanager.common.ent.vo.WSCommonResponseVO;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;


@MappedSuperclass
public class EmailVO extends WSCommonResponseVO {

	@Email(message = "Email : format non valide")
	@Basic(optional = false)
	@Column(name="SENDER", nullable=false)
	private String sender;

	@Email(message = "Email : format non valide")
	@Basic(optional = false)
	@Column(name="RECEIVER", nullable=false)
	private String receiver;

	@Basic(optional = false)
	@Column(name="SUBJECT", nullable=false)
	private String subject;


	@Basic(optional = false)
	@Column(name="CONTENT", nullable=false, length = Constants.CONTENT_LENGTH)
	private String content;

	public EmailVO() {
	}

	public EmailVO(@Email(message = "Email : format non valide") String sender, @Email(message = "Email : format non valide") String receiver, String subject, String content) {
		this.sender = sender;
		this.receiver = receiver;
		this.subject = subject;
		this.content = content;
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

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

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
