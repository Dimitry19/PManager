package cm.packagemanager.pmanager.message.ent.vo;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.common.ent.vo.CommonVO;
import cm.packagemanager.pmanager.user.ent.vo.UserIdVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

/**
 *
 */
@Entity(name ="MessageVO")
@Table(name="MESSAGE", schema = "PUBLIC")
public class MessageVO extends CommonVO {

	private static final long serialVersionUID = 1L;

	//private UserVO user;

	//private AnnounceVO announce;

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


	/*@MapsId
	@ManyToOne
	public AnnounceVO getAnnounce() {
		return announce;
	}

	public void setAnnounce(AnnounceVO announce) {
		this.announce = announce;
	}*/

	/*@ManyToOne
	@JoinColumn(name="USER_ID")
	public UserVO getUser(){
		return user;
	}



	public void setUser(UserVO user) {
		this.user = user;
	}*/

	@Basic(optional = false)
	@Column(name="CANCELLED")
	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
