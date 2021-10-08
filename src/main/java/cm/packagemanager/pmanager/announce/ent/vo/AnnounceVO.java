package cm.packagemanager.pmanager.announce.ent.vo;


import cm.packagemanager.pmanager.common.ent.vo.CommonVO;
import cm.packagemanager.pmanager.common.ent.vo.ImageVO;
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
@Where(clause= FilterConstants.FILTER_ANNOUNCE_CANC_COMPLETED)
public class AnnounceVO extends CommonVO {

	public static final String FINDBYUSER="cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO.findByUser";
	public static final String FINDBYTYPE="cm.packagemanager.pmanager.announce.ent.vo.AnnounceVO.findByType";
	public static final String SQL_FIND_BY_USER=" FROM AnnounceVO a where a.user.id =:userId order by a.startDate desc";
	public static final String ANNOUNCE_STATUS=" FROM AnnounceVO a where a.user.id =:userId order by a.startDate desc";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID",nullable = false,unique = true)
	private Long id;

	@Basic(optional = false)
	@Column(name = "DEPARTURE", nullable = false)
	private String departure;

	@Basic(optional = false)
	@Column(name = "ARRIVAL", nullable = false)
	private String arrival;

	@Basic(optional = false)
	@Column(name = "START_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = DateUtils.STD_PATTERN)
	private Date startDate;

	@Basic(optional = false)
	@Column(name = "END_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = DateUtils.STD_PATTERN)
	private Date endDate;

	@OneToOne(cascade =CascadeType.ALL,fetch = FetchType.EAGER)
	private ImageVO image;

	@Enumerated(EnumType.STRING)
	@Column(name="TRANSPORT", updatable = true, nullable = false)
	private TransportEnum transport;

	@Basic(optional = false)
	@Column(name = "PRICE", nullable = false)
	private BigDecimal price;

	@Basic(optional = false)
	@Column(name = "GOLD_PRICE", nullable = false)
	private BigDecimal goldPrice;

	@Basic(optional = false)
	@Column(name = "PRENIUM_PRICE", nullable = false)
	private BigDecimal preniumPrice;

	@Basic(optional = false)
	@Column(name = "WEIGHT", nullable = false)
	private BigDecimal weight;

	@Basic(optional = false)
	@Column(name = "REMAIN_WEIGHT", updatable = true)
	private BigDecimal remainWeight;

	@Enumerated(EnumType.STRING)
	@Column(name = "ANNOUNCE_TYPE",length = 10)
	private AnnounceType announceType;

	@Access(AccessType.PROPERTY)
	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.DETACH)
	@JoinColumn(name="R_USER_ID", updatable = false)
	@JsonBackReference
	@JsonProperty
	private UserVO user;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS",length = 10)
	private StatusEnum status;

	@Basic(optional = false)
	@Column(name = "DESCRIPTION", nullable = true)
	private String description;

	@Transient
	private String descriptionTransport;

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.EAGER)
	@JoinTable(name = "ANNOUNCE_CATEGORY", joinColumns = @JoinColumn(name = "ANNOUNCE_ID"), inverseJoinColumns = @JoinColumn(name = "CATEGORIES_CODE"))
	@JsonProperty
	private Set<CategoryVO> categories=new HashSet<>();

	@Access(AccessType.PROPERTY)
	@OneToMany(cascade = {CascadeType.REMOVE,CascadeType.MERGE}, mappedBy="announce",fetch=FetchType.LAZY)
	@JsonManagedReference
	@OrderBy(clause = "id.id ASC")
	@Where(clause = "cancelled=false")
	private Set<MessageVO> messages=new HashSet<>();

	private AnnounceIdVO announceId;

	@Transient
	private UserInfo userInfo;

	@Formula(value = "select count(id) from RESERVATION r where  r.r_announce_id = id and r.cancelled is false")
	private Integer countReservation;

	public AnnounceVO(){
		super();
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public String getArrival() {
		return arrival;
	}

	public void setArrival(String arrival) {
		this.arrival = arrival;
	}

	public TransportEnum  getTransport() {
		//setDescriptionTransport(this.transport.toValue());
		setDescriptionTransport("");

		return transport;
	}

	public void setTransport(TransportEnum transport) {
		this.transport = transport;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getRemainWeight() {
		return remainWeight;
	}

	public void setRemainWeight(BigDecimal remainWeight) {
		this.remainWeight = remainWeight;
	}

	public BigDecimal getGoldPrice() {
		return goldPrice;
	}

	public void setGoldPrice(BigDecimal goldPrice) {
		this.goldPrice = goldPrice;
	}

	public BigDecimal getPreniumPrice() {
		return preniumPrice;
	}

	public void setPreniumPrice(BigDecimal preniumPrice) {
		this.preniumPrice = preniumPrice;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public AnnounceType getAnnounceType(){
		return announceType;
	}

	public void setAnnounceType(AnnounceType announceType) {
		this.announceType = announceType;
	}

	public StatusEnum getStatus(){
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public Set<MessageVO> getMessages() {
		return messages;
	}

	public void setMessages(Set<MessageVO> messages) {
		this.messages = messages;
	}

	public UserVO getUser() {
		return user;
	}

	public void setUser(UserVO user) {
		this.user = user;
		userInfo= new UserInfo(user);
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public Set<CategoryVO> getCategories() {
		return categories;
	}

	public void setCategories(Set<CategoryVO> categories) {
		this.categories = categories;
	}

	public ImageVO getImage() {
		return image;
	}

	public void setImage(ImageVO image) {
		this.image = image;
	}

	public AnnounceIdVO getAnnounceId() {
		return announceId;
	}

	public void setAnnounceId(AnnounceIdVO announceId) {
		this.announceId = announceId;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}


	public String getDescriptionTransport() {
	
		return descriptionTransport;
	}

	public void setDescriptionTransport(String descriptionTransport){

		this.descriptionTransport=this.transport.toValue();
	}

	@Transient
	public Integer getCountReservation() {
		return countReservation;
	}

	public void setCountReservation(Integer countReservation) {
		this.countReservation = countReservation;
	}

	public void addCategory(CategoryVO category){
		categories.add(category);
	}

	public void removeCategory(CategoryVO category){

		if(categories.contains(category)){
			categories.remove(category);
		}
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

		this.messages.stream().forEach(message ->{
			message.setCancelled(true);
		} );
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

}
