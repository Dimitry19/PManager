package cm.travelpost.tp.announce.ent.vo;


import cm.framework.ds.common.ent.vo.CommonVO;
import cm.travelpost.tp.common.enums.AnnounceType;
import cm.travelpost.tp.common.enums.StatusEnum;
import cm.travelpost.tp.common.enums.TransportEnum;
import cm.travelpost.tp.common.utils.DateUtils;
import cm.travelpost.tp.common.utils.StringUtils;
import cm.travelpost.tp.configuration.filters.FilterConstants;
import cm.travelpost.tp.constant.FieldConstants;
import cm.travelpost.tp.image.ent.vo.ImageVO;
import cm.travelpost.tp.message.ent.vo.MessageVO;
import cm.travelpost.tp.user.ent.vo.UserInfo;
import cm.travelpost.tp.user.ent.vo.UserVO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.*;

import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "announce")
@NamedQueries(value = {
		@NamedQuery(name = AnnounceMasterVO.FINDBYUSER, query = "select a from AnnounceMasterVO a where a.user.id =:userId order by a.startDate desc"),
		@NamedQuery(name = AnnounceMasterVO.FINDBYTYPE, query = "select a from AnnounceMasterVO a where a.announceType =:announceType order by a.startDate desc"),
		@NamedQuery(name = AnnounceMasterVO.FINDBYTRANSPORT, query = "select a from AnnounceMasterVO a where a.transport =:transport order by a.startDate desc"),
})
@Filters({
		@Filter(name = FilterConstants.CANCELLED)
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "status")
@DiscriminatorOptions(insert = false,force=false)
@Where(clause = FilterConstants.FILTER_ANNOUNCE_CANC)
public class AnnounceMasterVO extends CommonVO {

	private static final long serialVersionUID = -6128390864869421614L;


	public static final String FINDBYUSER = "cm.travelpost.tp.announce.ent.vo.AnnounceMasterVO.findByUser";
	public static final String FINDBYTYPE = "cm.travelpost.tp.announce.ent.vo.AnnounceMasterVO.findByType";
	public static final String FINDBYTRANSPORT = "cm.travelpost.tp.announce.ent.vo.AnnounceMasterVO.findByTransport";
	public static final String SQL_FIND_BY_USER = " FROM AnnounceMasterVO a where a.user.id =:userId order by a.startDate desc";
	public static final String ANNOUNCE_SEARCH = "select  distinct  a from AnnounceVO  as a join a.categories as c ";


	protected Long id;

	protected String departure;

	protected String arrival;

	protected Date startDate;

	protected Date endDate;

	protected ImageVO image;

	protected TransportEnum transport;

	protected BigDecimal price;

	protected BigDecimal goldPrice;

	protected BigDecimal preniumPrice;

	protected BigDecimal weight;

	protected BigDecimal remainWeight;

	protected AnnounceType announceType;

	protected UserVO user;

	protected StatusEnum status;

	protected String description;

	protected Set<CategoryVO> categories = new HashSet<>();

	protected Set<MessageVO> messages = new HashSet<>();

	protected String warning;

	@Embedded
	protected AnnounceIdVO announceId;

	protected String descriptionTransport;

	@Transient
	protected UserInfo userInfo;

	@Transient
	protected Integer countReservation;

	public AnnounceMasterVO() {
		super();
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false, unique = true)
	public Long getId() {
		return id;
	}


	@Basic(optional = false)
	@Column(name = "START_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = DateUtils.STD_PATTERN)
	public Date getStartDate() {
		return startDate;
	}


	@Basic(optional = false)
	@Column(name = "END_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = DateUtils.STD_PATTERN)
	public Date getEndDate() {
		return endDate;
	}


	@Basic(optional = false)
	@Column(name = "DEPARTURE", nullable = false)
	public String getDeparture() {
		return departure;
	}

	@Basic(optional = false)
	@Column(name = "ARRIVAL", nullable = false)
	public String getArrival() {
		return arrival;
	}


	@Enumerated(EnumType.STRING)
	@Column(name = "TRANSPORT", nullable = false)
	public TransportEnum getTransport() {

		setDescriptionTransport(transport.toValue());
		return transport;
	}

	@Basic(optional = false)
	@Column(name = "WEIGHT", nullable = false)
	public BigDecimal getWeight() {
		return weight;
	}


	@Basic(optional = false)
	@Column(name = "REMAIN_WEIGHT")
	public BigDecimal getRemainWeight() {
		return remainWeight;
	}


	@Basic(optional = false)
	@Column(name = "GOLD_PRICE", nullable = false)
	public BigDecimal getGoldPrice() {
		return goldPrice;
	}

	@Basic(optional = false)
	@Column(name = "PRENIUM_PRICE", nullable = false)
	public BigDecimal getPreniumPrice() {
		return preniumPrice;
	}


	@Basic(optional = false)
	@Column(name = "PRICE", nullable = false)
	public BigDecimal getPrice() {
		return price;
	}


	@Enumerated(EnumType.STRING)
	@Column(name = "ANNOUNCE_TYPE", length = FieldConstants.ENUM_LEN)
	public AnnounceType getAnnounceType() {
		return announceType;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", length = FieldConstants.ENUM_LEN)
	public StatusEnum getStatus() {
		return status;
	}

	// @Basic(optional = false)
	@Access(AccessType.PROPERTY)
	@OneToMany(cascade = {CascadeType.REMOVE, CascadeType.MERGE}, mappedBy = "announce", fetch = FetchType.LAZY)
	@JsonManagedReference
	@org.hibernate.annotations.OrderBy(clause = "id.id ASC")
	@Where(clause = "cancelled=false")
	//@JsonIgnore
	public Set<MessageVO> getMessages() {
		return messages;
	}


	@Access(AccessType.PROPERTY)
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "R_USER_ID", updatable = false)
	@JsonBackReference
	@JsonProperty
	public UserVO getUser() {
		return user;
	}


	@Basic(optional = false)
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	@Access(AccessType.PROPERTY)
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
	@JoinTable(name = "announce_category", joinColumns = @JoinColumn(name = "ANNOUNCE_ID"), inverseJoinColumns = @JoinColumn(name = "CATEGORIES_CODE"))
	@JsonProperty
	public Set<CategoryVO> getCategories() {
		return categories;
	}

	@Access(AccessType.PROPERTY)
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	public ImageVO getImage() {
		return image;
	}

	public AnnounceIdVO getAnnounceId() {
		return announceId;
	}

	@Formula(value = "(select coalesce(count(r.r_announce_id),0)  from reservation r where  r.r_announce_id = id and r.cancelled ='0')")
	public Integer getCountReservation() {
		return countReservation;
	}


	@Transient
	public UserInfo getUserInfo() {   return userInfo;  }

	@Transient
	public String getDescriptionTransport() {   return descriptionTransport;  }

	@Transient
	@JsonProperty
	public String getWarning(){
		return warning;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public void setArrival(String arrival) {
		this.arrival = arrival;
	}

	public void setTransport(TransportEnum transport) {
		this.transport = transport;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public void setRemainWeight(BigDecimal remainWeight) {
		this.remainWeight = remainWeight;
	}

	public void setGoldPrice(BigDecimal goldPrice) {
		this.goldPrice = goldPrice;
	}

	public void setPreniumPrice(BigDecimal preniumPrice) {
		this.preniumPrice = preniumPrice;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setAnnounceType(AnnounceType announceType) {
		this.announceType = announceType;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public void setMessages(Set<MessageVO> messages) {
		this.messages = messages;
	}

	public void setUser(UserVO user) {
		this.user = user;
		if (user != null) {
			userInfo = new UserInfo(user);
		}
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCategories(Set<CategoryVO> categories) {
		this.categories = categories;
	}


	public void setImage(ImageVO image) {
		this.image = image;
	}


	public void setAnnounceId(AnnounceIdVO announceId) {
		this.announceId = announceId;
	}


	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public void setDescriptionTransport(String descriptionTransport) {

		this.descriptionTransport = descriptionTransport;
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}

	public void setCountReservation(Integer countReservation) {
		this.countReservation = countReservation;
	}

	public void addCategory(CategoryVO category) {
		categories.add(category);
	}

	public void removeCategory(CategoryVO category) {

		categories.remove(category);
	}

	public void addMessage(MessageVO message) {
		messages.add(message);
		//message.setAnnounce(this);
	}

	public void removeMessage(MessageVO message) {

		if (messages.contains(message)) {
			messages.remove(message);
			message.setAnnounce(null);
		}
	}

	public void updateDeleteChildrens() {

		this.messages.forEach(message -> {
			message.cancel();
		});
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
		if (id.equals(other.id))
			return false;
		if (user == null) {
			return other.user == null;
		} else return user.equals(other.user);
	}

	private String toUpperCase(String value){
		if(StringUtils.isNotEmpty(value)){
			return value.toUpperCase();
		}
		return value;

	}

}
