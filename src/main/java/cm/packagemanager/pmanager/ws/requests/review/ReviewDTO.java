package cm.packagemanager.pmanager.ws.requests.review;

import cm.packagemanager.pmanager.rating.enums.Rating;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

public class ReviewDTO {


    private Long userId;

    private Long ratingUserId;

    @NotNull(message = "La note doit etre valorisée")
    @Enumerated(EnumType.ORDINAL)
    private Rating rating;

    @NotNull(message = "Le titre doit etre valorisé")
    private String title;

    @NotNull(message = "Les details de l'avis  doivent etre valorisés")
    private String details;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public Long getRatingUserId() {
        return ratingUserId;
    }

    public void setRatingUserId(Long ratingUserId) {
        this.ratingUserId = ratingUserId;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
