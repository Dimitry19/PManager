package cm.packagemanager.pmanager.announce.ent.vo;

import cm.packagemanager.pmanager.common.ent.vo.CommonVO;
import cm.packagemanager.pmanager.constant.FieldConstants;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "RESERVATION",schema = "PUBLIC")
public class ReservationVO  extends CommonVO {

	private Long id;
	private UserVO user;
	private BigDecimal weight;
	private ProductCategoryVO category;
	private AnnounceVO announce;
	private boolean validate;
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
	public ProductCategoryVO getCategory() {
		return category;
	}

	public void setCategory(ProductCategoryVO category) {
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
}
