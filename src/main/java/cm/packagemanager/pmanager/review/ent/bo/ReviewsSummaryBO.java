package cm.packagemanager.pmanager.review.ent.bo;


import cm.packagemanager.pmanager.rating.enums.Rating;

public interface ReviewsSummaryBO {

    long getNumberOfReviewsWithRating(Rating rating);

}