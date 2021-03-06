package cm.travelpost.tp.review.ent.vo;

import cm.travelpost.tp.rating.enums.Rating;

import java.io.Serializable;

public class ReviewDetailsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Rating rating;

    private String title;

    private String details;

    public ReviewDetailsVO() {
    }

    public ReviewDetailsVO(Rating rating, String title, String details) {
        this.details = details;
        this.title = title;
        this.rating = rating;
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
