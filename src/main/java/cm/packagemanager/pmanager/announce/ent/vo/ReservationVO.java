package cm.packagemanager.pmanager.announce.ent.vo;

import cm.packagemanager.pmanager.common.ent.vo.CommonVO;
import cm.packagemanager.pmanager.common.enums.StatusEnum;
import cm.packagemanager.pmanager.common.enums.ValidateEnum;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.constant.FieldConstants;
import cm.packagemanager.pmanager.user.ent.vo.UserInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
@Table(name = "RESERVATION",schema = "PUBLIC")
@NamedQueries({
		@NamedQuery(name = ReservationVO.FINDBYANNOUNCE, query =" select r from  ReservationVO as r where r.announce.id =: announceId"),
		@NamedQuery(name = ReservationVO.FINDBYUSER, query =" select r from  ReservationVO as r where r.user.id =: userId"),
		@NamedQuery(name = ReservationVO.FIND_ANNOUNCE_USER, query =" select r from  ReservationVO as r inner join r.announce a where a.user.id =: userId"),
})
@Where(clause= FilterConstants.FILTER_WHERE_RESERVATION_CANC_COMPLETED)
public class ReservationVO  extends CommonVO {

	public static final String FINDBYANNOUNCE="cm.packagemanager.pmanager.announce.ent.vo.ReservationVO.findByAnnounce";
	public static final String FINDBYUSER="cm.packagemanager.pmanager.announce.ent.vo.ReservationVO.findByUser";
	public static final String FIND_ANNOUNCE_USER="cm.packagemanager.pmanager.announce.ent.vo.ReservationVO.findByAnnounceUser";



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	private Long id;

	//@JsonIgnore
	@Access(AccessType.PROPERTY)
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="R_USER_ID", updatable = false)
	@JsonBackReference
	private UserVO user;


	@Basic(optional = false)
	@Column(name = "WEIGTH", nullable = false)
	private BigDecimal weight;


	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.EAGER)
	@JoinTable(name = "RESERVATION_CATEGORY", joinColumns = @JoinColumn(name = "RESERVATION_ID"), inverseJoinColumns = @JoinColumn(name = "CATEGORIES_CODE"))
	@JsonProperty
	private Set<CategoryVO> categories=new HashSet<>();


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="R_ANNOUNCE_ID", updatable = true)
	@JsonBackReference
	private AnnounceVO announce;

	@Basic(optional = false)
	@Enumerated(EnumType.STRING)
	@Column(name="VALIDATE")
	private ValidateEnum validate;


	@Basic(optional = false)
	@Column(name="DESCRIPTION" , length = FieldConstants.DESC)
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS",length = 10)
	private StatusEnum status;

	public StatusEnum getStatus(){
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}


	@Transient
	@JsonProperty
	private UserInfo userInfo;

	@Transient
	@JsonProperty
	private AnnounceInfo announceInfo;

	public AnnounceInfo getAnnounceInfo() {
		return announceInfo;
	}

	public void setAnnounceInfo(AnnounceInfo announceInfo) {
		this.announceInfo = announceInfo;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public ReservationVO(){
		super();
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserVO getUser() {
		return user;
	}

	public void setUser(UserVO user) {
		this.user = user;
		userInfo= new UserInfo(user);
	}

	public AnnounceVO getAnnounce() {
		return announce;
	}

	public void setAnnounce(AnnounceVO announce) {
		this.announce = announce;
		announceInfo = new AnnounceInfo(announce);
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public Set<CategoryVO> getCategories() {
		return categories;
	}

	public void setCategories(Set<CategoryVO> categories) {
		this.categories = categories;
	}

	public ValidateEnum getValidate() {
		return validate;
	}

	public void setValidate(ValidateEnum validate) {
		this.validate = validate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void addCategory(CategoryVO category){
		categories.add(category);
	}

	public void removeCategory(CategoryVO category){

		if(categories.contains(category)){
			categories.remove(category);
		}
	}

	public void removeCategories() {
		Iterator<CategoryVO> iterator = this.categories.iterator();
		while (iterator.hasNext()) {
			iterator.remove();
		}
	}
}
