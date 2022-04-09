package cm.travelpost.tp.review.ent.bo;


import cm.travelpost.tp.rating.enums.Rating;

public interface ReviewsSummaryBO {

    long getNumberOfReviewsWithRating(Rating rating);

}