package cm.packagemanager.pmanager.announce.ent.vo;


import cm.packagemanager.pmanager.common.enums.AnnounceType;
import cm.packagemanager.pmanager.common.enums.StatusEnum;
import cm.packagemanager.pmanager.common.enums.TransportEnum;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.hibernate.annotations.FetchMode.SELECT;

@Entity(name ="AnnouncesVO")

@Table(name = "V_ANNOUNCES", schema = "VIEWS")
@Filters({
		@Filter(name = FilterConstants.CANCELLED)
})
@Immutable

public class AnnouncesVO {
	public static final String FINDBYID="cm.packagemanager.pmanager.announce.ent.vo.AnnouncesVO.findById";


	private Long id;

	private String departure;

	private String arrival;

	private Date startDate;

	private Date endDate;

	private TransportEnum transport;

	private BigDecimal price;
	private BigDecimal goldPrice;
	private BigDecimal preniumPrice;

	private BigDecimal weigth;

	private AnnounceType announceType;

	private String user;

	private StatusEnum status;

	private boolean cancelled;

	private Set<MessageVO> messages=new HashSet<>();

	public AnnouncesVO(){ super();}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic(optional = false)
	@Column(name = "START_DATE", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Basic(optional = false)
	@Column(name = "END_DATE", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Basic(optional = false)
	@Column(name = "DEPARTURE", nullable = false)
	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	@Basic(optional = false)
	@Column(name = "ARRIVAL", nullable = false)
	public String getArrival() {
		return arrival;
	}

	public void setArrival(String arrival) {
		this.arrival = arrival;
	}


	@Enumerated(EnumType.STRING)
	@Column(name="TRANSPORT", updatable = true, nullable = false)
	public TransportEnum  getTransport() {
		return transport;
	}

	public void setTransport(TransportEnum transport) {
		this.transport = transport;
	}

	@Basic(optional = false)
	@Column(name = "WEIGHT", nullable = false)
	public BigDecimal getWeigth() {
		return weigth;
	}

	public void setWeigth(BigDecimal weigth) {
		this.weigth = weigth;
	}

	@Basic(optional = false)
	@Column(name = "GOLD_PRICE", nullable = false)
	public BigDecimal getGoldPrice() {
		return goldPrice;
	}

	public void setGoldPrice(BigDecimal goldPrice) {
		this.goldPrice = goldPrice;
	}

	@Basic(optional = false)
	@Column(name = "PRENIUM_PRICE", nullable = false)
	public BigDecimal getPreniumPrice() {
		return preniumPrice;
	}

	public void setPreniumPrice(BigDecimal preniumPrice) {
		this.preniumPrice = preniumPrice;
	}

	@Basic(optional = false)
	@Column(name = "PRICE", nullable = false)
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}



	@Enumerated(EnumType.STRING)
	@Column(name = "ANNOUNCE_TYPE",length = 10)
	public AnnounceType getAnnounceType(){
		return announceType;
	}


	public void setAnnounceType(AnnounceType announceType) {
		this.announceType = announceType;
	}


	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS",length = 10)
	public StatusEnum getStatus(){
		return status;
	}


	public void setStatus(StatusEnum status) {
		this.status = status;
	}


	@Access(AccessType.PROPERTY)
	@OneToMany(cascade = {CascadeType.ALL},targetEntity=MessageVO.class, mappedBy="announce", fetch=FetchType.EAGER)
	@JsonManagedReference
	@Fetch(value = SELECT)
	public Set<MessageVO> getMessages() {
		return messages;
	}

	public void setMessages(Set<MessageVO> messages) {
		this.messages = messages;
	}


	@Access(AccessType.PROPERTY)
	@Column(name = "USERNAME")
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
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


	public void addMessages(MessageVO message){
		messages.add(message);
	}

	public void removeMessages(MessageVO message){

		if(messages.contains(message)){
			messages.remove(message);
		}
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id.intValue();
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnnouncesVO other = (AnnouncesVO) obj;
		if (id != other.id)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
}
