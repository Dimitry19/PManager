package cm.packagemanager.pmanager.announce.ent.vo;

import cm.packagemanager.pmanager.airline.ent.vo.AirlineVO;
import cm.packagemanager.pmanager.common.ent.vo.CommonVO;
import cm.packagemanager.pmanager.common.enums.AnnounceType;
import cm.packagemanager.pmanager.common.enums.StatusEnum;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "ANNOUNCE", schema = "PUBLIC")
public class AnnounceVO extends CommonVO {



	private int id;

	private String departure;

	private String arrival;

	private Date startDate;

	private Date endDate;

	private AirlineVO airline;

	private BigDecimal price;

	private BigDecimal weigth;

	private AnnounceType announceType;

	private StatusEnum status;

	private boolean cancelled;

	private Set<MessageVO> messages=new HashSet<>();


	private UserVO user;


	private AnnounceIdVO announceId;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	public int getId() {
		return id;
	}

	public void setId(int id) {
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


	@OneToOne(cascade = CascadeType.ALL)
	public AirlineVO getAirline() {
		return airline;
	}

	public void setAirline(AirlineVO airline) {
		this.airline = airline;
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
	@Column(name = "PRICE", nullable = false)
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}



	@Enumerated(EnumType.STRING)
	@Column(length = 10)
	public AnnounceType getAnnounceType(){
		return announceType;
	}


	public void setAnnounceType(AnnounceType announceType) {
		this.announceType = announceType;
	}


	@Enumerated(EnumType.STRING)
	@Column(length = 10)
	public StatusEnum getStatus(){
		return status;
	}


	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public AnnounceIdVO getAnnounceId() {
		return announceId;
	}

	public void setAnnounceId(AnnounceIdVO announceId) {
		this.announceId = announceId;
	}


	@Access(AccessType.PROPERTY)
	@OneToMany(targetEntity=MessageVO.class, mappedBy="announce", fetch=FetchType.EAGER)
	public Set<MessageVO> getMessages() {
		return messages;
	}

	public void setMessages(Set<MessageVO> messages) {
		this.messages = messages;
	}


	@Access(AccessType.PROPERTY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name="R_USER_ID", referencedColumnName = "USER_ID",insertable=false ,updatable=false),
			@JoinColumn(name="TOKEN", referencedColumnName = "TOKEN",insertable=false ,updatable=false)
	})
	public UserVO getUser() {
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
