package cm.packagemanager.pmanager.message.ent.vo;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.common.ent.vo.CommonVO;
import cm.packagemanager.pmanager.user.ent.vo.UserIdVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.NaturalId;
import org.omg.IOP.TAG_ALTERNATE_IIOP_ADDRESS;

import javax.persistence.*;

/**
 *
 */
@Entity(name ="MessageVO")
@Table(name="MESSAGE", schema = "PUBLIC")
public class MessageVO extends CommonVO {

	private static final long serialVersionUID = 1L;

	private UserVO user;

	private AnnounceVO announce;

	private String content;

	private boolean cancelled;

	@EmbeddedId
	MessageIdVO id;


	public MessageIdVO getId() {
		return id;
	}

	public void setId(MessageIdVO id) {
		this.id = id;
	}

	@Basic(optional = false)
	@Column(name = "CONTENT")
	private String getContent(){
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Access(AccessType.PROPERTY)
	@ManyToOne
	@JoinColumn(name="R_ANNOUNCE_ID", referencedColumnName = "ID")
	public AnnounceVO getAnnounce() {
		return announce;
	}

	public void setAnnounce(AnnounceVO announce) {
		this.announce = announce;
	}

	@Access(AccessType.PROPERTY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="R_USER_ID", referencedColumnName = "USER_ID",insertable=false ,updatable=false),
			@JoinColumn(name="TOKEN", referencedColumnName = "TOKEN",insertable=false ,updatable=false)
	})
	public UserVO getUser(){
		return user;
	}



	public void setUser(UserVO user) {
		this.user = user;
	}

	@Basic(optional = false)
	@Column(name="CANCELLED")
	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
