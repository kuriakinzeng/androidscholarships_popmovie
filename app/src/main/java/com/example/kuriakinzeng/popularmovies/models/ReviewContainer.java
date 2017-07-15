package com.example.kuriakinzeng.popularmovies.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kuriakinzeng on 7/15/17.
 */

public class ReviewContainer {
    @SerializedName("results")
    private Review[] reviews = null;

    public Review[] getReviews() {
        return reviews;
    }

    public void setReviews(Review[] reviews) {
        this.reviews = reviews;
    }
}
