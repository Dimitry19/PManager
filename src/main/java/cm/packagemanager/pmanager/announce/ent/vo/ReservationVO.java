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



	private Long id;
	private UserVO user;
	private BigDecimal weight;
	private CategoryVO category;
	private AnnounceVO announce;
	private boolean validate;

	private boolean cancelled;
	private String description;

	public ReservationVO(){
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	@Access(AccessType.PROPERTY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="R_USER_ID", updatable = false)
	@JsonBackReference
	public UserVO getUser() {
		return user;
	}

	public void setUser(UserVO user) {
		this.user = user;
	}

	@Access(AccessType.PROPERTY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="R_ANNOUNCE_ID", updatable = true)
	@JsonBackReference
	public AnnounceVO getAnnounce() {
		return announce;
	}

	public void setAnnounce(AnnounceVO announce) {
		this.announce = announce;
	}

	@Basic(optional = false)
	@Column(name = "WEIGTH", nullable = false)
	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	@OneToOne
	@JoinColumn(name="CATEGORY_ID")
	public CategoryVO getCategory() {
		return category;
	}

	public void setCategory(CategoryVO category) {
		this.category = category;
	}

	@Basic(optional = false)
	@Column(name="VALIDATE")
	@JsonIgnore
	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	@Basic(optional = false)
	@Column(name="DESCRIPTION" , length = FieldConstants.DESC)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Basic(optional = false)
	@Column(name="CANCELLED")
	@JsonIgnore
	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
