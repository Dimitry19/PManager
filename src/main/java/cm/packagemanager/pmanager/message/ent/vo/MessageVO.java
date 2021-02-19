package cm.packagemanager.pmanager.message.ent.vo;

import cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO;
import cm.packagemanager.pmanager.announce.ent.vo.AnnouncesVO;
import cm.packagemanager.pmanager.common.ent.vo.CommonVO;
import cm.packagemanager.pmanager.common.ent.vo.WSCommonResponseVO;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.user.ent.vo.UserIdVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.NaturalId;
import org.omg.IOP.TAG_ALTERNATE_IIOP_ADDRESS;

import javax.persistence.*;

/**
 *
 */

@Entity(name ="MessageVO")
@Table(name="MESSAGE", schema = "PUBLIC")
@Filters({
		@Filter(name = FilterConstants.CANCELLED)
})
public class MessageVO extends WSCommonResponseVO {

	public  final static String GET_ID_SQL="SELECT MAX(ID) FROM MESSAGE WHERE CANCELLED IS FALSE ";

	private static final long serialVersionUID = 1L;

	private UserVO user;

	private AnnounceVO announce;

	private String content;

	@Transient
	private String username;

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
	@JsonProperty
	private String getContent(){
		return content;
	}

	@Access(AccessType.PROPERTY)
	@ManyToOne
	@JoinColumn(name="R_ANNOUNCE", referencedColumnName = "ID",updatable = false)
	@JsonBackReference
	public AnnounceVO getAnnounce() {
		return announce;
	}


	@Access(AccessType.PROPERTY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="R_USER_ID", updatable = false)
	@JsonBackReference
	public UserVO getUser(){
		return user;
	}


	@Basic(optional = false)
	@Column(name="CANCELLED")
	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public void setUser(UserVO user) {
		this.user = user;
		setUsername(user.getUsername());
	}

	public void setAnnounce(AnnounceVO announce) {
		this.announce = announce;
	}

	public void setContent(String content) {
		this.content = content;
	}


	@JsonProperty
	public String getUsername() {
		return username;//.getUsername();
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
