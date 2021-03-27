package cm.packagemanager.pmanager.review.ent.vo;

import javax.persistence.*;
import cm.packagemanager.pmanager.common.ent.vo.WSCommonResponseVO;
import cm.packagemanager.pmanager.configuration.filters.FilterConstants;
import cm.packagemanager.pmanager.rating.enums.Rating;
import cm.packagemanager.pmanager.user.ent.vo.UserVO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.Filters;
import org.springframework.util.Assert;

/*
https://github.com/cloudControl/spring-boot-example-app/tree/master/src/main
*/

@Entity
@Table(name="REVIEW", schema = "PUBLIC")
@NamedQueries({
		@NamedQuery(name = ReviewVO.RATING,  query="select new cm.packagemanager.pmanager.rating.ent.vo.RatingCountVO(r.rating, count(r)) from ReviewVO r where r.user =:userId group by r.rating order by r.rating DESC"),
})
@Filters({
		@Filter(name = FilterConstants.CANCELLED)
})
public class ReviewVO extends WSCommonResponseVO {

	private static final long serialVersionUID = 1L;

	public static final String RATING="cm.packagemanager.pmanager.review.ent.vo.ReviewVO.rating";


	private Long id;

	private UserVO user;

	private UserVO ratingUser;

	private int index;

	private Rating rating;

	private String title;

	private String details;

	private boolean cancelled;

	private ReviewIdVO reviewId;

	public ReviewIdVO getReviewId() {
		return  reviewId;
	}

	public void setReviewId(ReviewIdVO reviewId) {
		this.reviewId = reviewId;
	}

	@Id
	@GeneratedValue
	@Column(name="ID")
	public Long getId() {
		return id;
	}

	@Access(AccessType.PROPERTY)
	@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.DETACH)
	@JoinColumn(name="R_USER_ID", updatable = false)
	@JsonBackReference
	@JsonProperty
	public UserVO getUser() {
		return this.user;
	}

	/*@Access(AccessType.PROPERTY)
	@ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.DETACH)
	@JoinColumn(name="RATING_USER_ID", updatable = false)
	@JsonBackReference
	@JsonProperty
	public UserVO getRatingUser() {
		return ratingUser;
	}*/

	@Column(name = "INDEXES",nullable = false)
	public int getIndex() {
		return this.index;
	}

	@Column(name = "RATING",nullable = false)
	@Enumerated(EnumType.ORDINAL)
	public Rating getRating() {
		return this.rating;
	}

	@Column(name ="TITLE", nullable = false)
	public String getTitle() {
		return this.title;
	}

	@Column(name="DETAILS",nullable = false, length = 5000)
	public String getDetails() {
		return this.details;
	}

	@Basic(optional = false)
	@Column(name="CANCELLED")
	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	public void setId(Long id) {this.id = id;}

	public void setUser(UserVO user) {
		this.user = user;
	}


/*
	public void setRatingUser(UserVO ratingUser) {
		this.ratingUser = ratingUser;
	}
*/

	public void setIndex(int index) {
		this.index = index;
	}

	public ReviewVO() {
		super();
	}


	public  ReviewVO(UserVO  user,UserVO ratingUser, int index, ReviewDetailsVO details) {
		new ReviewVO(user,  index,  details);
		this.ratingUser=ratingUser;
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
		ReviewVO other = (ReviewVO) obj;
		if (id != other.id)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
}