package cm.packagemanager.pmanager.review.ent.vo;

import cm.packagemanager.pmanager.rating.enums.Rating;

import java.io.Serializable;
import java.util.Date;

public class ReviewDetailsVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Rating rating;

	private String title;

	private String details;

	public ReviewDetailsVO() {
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
