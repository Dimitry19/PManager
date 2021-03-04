package cm.packagemanager.pmanager.review.ent.vo;

import java.io.Serializable;
import javax.persistence.*;

import cm.packagemanager.pmanager.common.ent.vo.CommonVO;
import cm.packagemanager.pmanager.rating.enums.Rating;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import org.springframework.util.Assert;

/*
https://github.com/cloudControl/spring-boot-example-app/tree/master/src/main
*/

@Entity(name = "ReviewVO")
@Table(name="REVIEW", schema = "PUBLIC")
public class ReviewVO extends CommonVO {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(optional = false)
	private UserVO user;

	@Column(nullable = false, name = "idx")
	private int index;

	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private Rating rating;


	@Column(nullable = false)
	private String title;

	@Column(nullable = false, length = 5000)
	private String details;

	public ReviewVO() {
		super();
	}


	public  ReviewVO(UserVO  user, int index, ReviewDetailsVO details) {
		Assert.notNull(user, "User must not be null");
		Assert.notNull(details, "Details must not be null");
		this.user = user;
		this.index = index;
		this.rating = details.getRating();
		this.title = details.getTitle();
		this.details = details.getDetails();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserVO getUser() {
		return this.user;
	}

	public int getIndex() {
		return this.index;
	}

	public Rating getRating() {
		return this.rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetails() {
		return this.details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
}