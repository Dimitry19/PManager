package cm.packagemanager.pmanager.announce.ent.vo;


import cm.packagemanager.pmanager.common.ent.vo.ImageVO;
import cm.packagemanager.pmanager.common.ent.vo.WSCommonResponseVO;
import cm.packagemanager.pmanager.common.enums.AnnounceType;
import cm.packagemanager.pmanager.common.enums.StatusEnum;
import cm.packagemanager.pmanager.common.enums.TransportEnum;
import cm.packagemanager.pmanager.common.utils.DateUtils;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.message.ent.vo.MessageVO;
import cm.packagemanager.pmanager.user.ent.vo.UserInfo;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.*;
import org.hibernate.annotations.OrderBy;

import javax.persistence.*;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


@Entity
@Table(name = "ANNOUNCE", schema = "PUBLIC")
@NamedQueries({
		@NamedQuery(name = AnnounceVO.FINDBYUSER,  query="select a from AnnounceVO a where a.user.id =:userId order by a.startDate desc"),
		@NamedQuery(name = AnnounceVO.FINDBYTYPE,  query="select a from AnnounceVO a where a.announceType =:type order by a.startDate desc"),
})
@Filters({
		@Filter(name = FilterConstants.CANCELLED)
})
@Where(clause= FilterConstants.FILTER_WHERE_ANNOUNCE_CANCELLED)
public class AnnounceVO extends WSCommonResponseVO {

	public static final String FINDBYUSER="cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO.findByUser";
	public static final String FINDBYTYPE="cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO.findByType";
	public static final String SQL_FIND_BY_USER=" FROM AnnounceVO a where a.user.id =:userId order by a.startDate desc";

	private Long id;

	private String departure;

	private String arrival;

	private Date startDate;

	private Date endDate;

	private ImageVO image;

	private TransportEnum transport;

	private BigDecimal price;

	private BigDecimal goldPrice;

	private BigDecimal preniumPrice;

	private BigDecimal weight;

	private BigDecimal remainWeight;

	private AnnounceType announceType;

	private UserVO user;

	private StatusEnum status;

	private boolean cancelled;
	
	private String description;
	
	private String descriptionTransport;

	private CategoryVO category;

	private Set<MessageVO> messages=new HashSet<>();

	private AnnounceIdVO announceId;

	private UserInfo userInfo;

	public AnnounceVO(){ super();}


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
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = DateUtils.STD_PATTERN)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Basic(optional = false)
	@Column(name = "END_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = DateUtils.STD_PATTERN)
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
		setDescriptionTransport(transport.toValue());
		this.transport = transport;
	}

	@Basic(optional = false)
	@Column(name = "WEIGHT", nullable = false)
	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	@Basic(optional = false)
	@Column(name = "REMAIN_WEIGHT", updatable = true)
	public BigDecimal getRemainWeight() {
		return remainWeight;
	}

	public void setRemainWeight(BigDecimal remainWeight) {
		this.remainWeight = remainWeight;
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

	@JsonIgnore
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS",length = 10)
	public StatusEnum getStatus(){
		return status;
	}


	public void setStatus(StatusEnum status) {
		this.status = status;
	}


	@Access(AccessType.PROPERTY)
	@OneToMany(cascade = {CascadeType.REMOVE,CascadeType.MERGE}, mappedBy="announce",fetch=FetchType.LAZY)
	@JsonManagedReference
	//@Fetch(value = SELECT)
	@OrderBy(clause = "id.id ASC")
	public Set<MessageVO> getMessages() {
		return messages;
	}

	public void setMessages(Set<MessageVO> messages) {
		this.messages = messages;
	}


	@Access(AccessType.PROPERTY)
	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.DETACH)
	@JoinColumn(name="R_USER_ID", updatable = false)
	@JsonBackReference
	@JsonProperty
	public UserVO getUser() {
		return user;
	}

	public void setUser(UserVO user) {
		this.user = user;
		userInfo= new UserInfo(user);
	}

	@JsonIgnore
	@Basic(optional = false)
	@Column(name="CANCELLED")
	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	
	@Basic(optional = false)
	@Column(name = "DESCRIPTION", nullable = true)
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	@Access(AccessType.PROPERTY)
	@ManyToOne(optional = false,fetch = FetchType.LAZY,cascade = CascadeType.DETACH)
	@JoinColumn(name="R_CATEGORY",referencedColumnName = "CODE")
	@JsonProperty
	public CategoryVO getCategory() {
		return category;
	}

	public void setCategory(CategoryVO category) {
		this.category = category;
	}

	@OneToOne
	public ImageVO getImage() {
		return image;
	}

	public void setImage(ImageVO image) {
		this.image = image;
	}

	//@NaturalId
	public AnnounceIdVO getAnnounceId() {
		return announceId;
	}

	public void setAnnounceId(AnnounceIdVO announceId) {
		this.announceId = announceId;
	}

	@Transient
	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}


	
	@Transient
	@JsonProperty
	public String getDescriptionTransport() {
	
		return descriptionTransport;
	}

	public void setDescriptionTransport(String descriptionTransport){
		this.descriptionTransport=descriptionTransport;
	}	
	
	public void addMessage(MessageVO message){
		messages.add(message);
		message.setAnnounce(this);
	}

	public void removeMessage(MessageVO message){

		if(messages.contains(message)){
			messages.remove(message);
			message.setAnnounce(null);
		}
	}


	public void updateDeleteChildrens() {

		Iterator<MessageVO> itermessage = this.messages.iterator();
		while (itermessage.hasNext()) {
			MessageVO message = itermessage.next();
			message.setCancelled(true);
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
		AnnounceVO other = (AnnounceVO) obj;
		if (id != other.id)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AnnounceVO{" + "id=" + id + ", departure='" + departure + '\'' + ", arrival='" + arrival + '\'' + ", startDate=" + startDate + ", endDate=" + endDate + ", transport=" + transport + ", price=" + price + ", goldPrice=" + goldPrice + ", preniumPrice=" + preniumPrice + ", weight=" + weight + ", remainWeight=" + remainWeight + ", announceType=" + announceType + ", user=" + user + ", status=" + status + ", cancelled=" + cancelled + ", description='" + description + '\'' + ", descriptionTransport='" + descriptionTransport + '\'' + ", category=" + category + ", messages=" + messages + ", announceId=" + announceId  + '\'' + '}';
	}
}
