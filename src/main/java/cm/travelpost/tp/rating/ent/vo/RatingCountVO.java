package cm.travelpost.tp.rating.ent.vo;

import cm.travelpost.tp.rating.enums.Rating;

import java.io.Serializable;

public class RatingCountVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Rating rating;

    private final long count;

    public RatingCountVO(Rating rating, long count) {
        this.rating = rating;
        this.count = count;
    }

    public Rating getRating() {
        return this.rating;
    }

    public long getCount() {
        return this.count;
    }
}