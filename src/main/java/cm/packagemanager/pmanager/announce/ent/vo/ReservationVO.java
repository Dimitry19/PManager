package cm.packagemanager.pmanager.announce.ent.vo;

import cm.packagemanager.pmanager.common.ent.vo.WSCommonResponseVO;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.constant.FieldConstants;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "RESERVATION",schema = "PUBLIC")
@NamedQueries({
		@NamedQuery(name = ReservationVO.FINDBYANNOUNCE, query =" select r from  ReservationVO as r where r.announce.id =: announceId"),
		@NamedQuery(name = ReservationVO.FINDBYUSER, query =" select r from  ReservationVO as r where r.user.id =: userId"),
})
@Where(clause= FilterConstants.FILTER_WHERE_RESERVATION_CANCELLED)
public class ReservationVO  extends WSCommonResponseVO {

	public static final String FINDBYANNOUNCE="cm.packagemanager.pmanager.announce.ent.vo.ReservationVO.findByAnnounce";
	public static final String FINDBYUSER="cm.packagemanager.pmanager.announce.ent.vo.ReservationVO.findByUser";



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	private Long id;

	@Access(AccessType.PROPERTY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="R_USER_ID", updatable = false)
	@JsonBackReference
	private UserVO user;


	@Basic(optional = false)
	@Column(name = "WEIGTH", nullable = false)
	private BigDecimal weight;

	@OneToOne
	@JoinColumn(name="CATEGORY_ID")
	private CategoryVO category;

	@Access(AccessType.PROPERTY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="R_ANNOUNCE_ID", updatable = true)
	@JsonBackReference
	private AnnounceVO announce;

	@Basic(optional = false)
	@Column(name="VALIDATE")
	@JsonIgnore
	private boolean validate;

	@Basic(optional = false)
	@Column(name="CANCELLED")
	@JsonIgnore
	private boolean cancelled;

	@Basic(optional = false)
	@Column(name="DESCRIPTION" , length = FieldConstants.DESC)
	private String description;

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
	}

	public AnnounceVO getAnnounce() {
		return announce;
	}

	public void setAnnounce(AnnounceVO announce) {
		this.announce = announce;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public CategoryVO getCategory() {
		return category;
	}

	public void setCategory(CategoryVO category) {
		this.category = category;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
