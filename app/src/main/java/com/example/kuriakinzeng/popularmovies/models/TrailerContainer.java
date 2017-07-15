package com.example.kuriakinzeng.popularmovies.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kuriakinzeng on 7/15/17.
 */

public class TrailerContainer {
    @SerializedName("results")
    private Trailer[] trailers = null;

    public Trailer[] getTrailers() {
        return trailers;
    }

    public void setTrailers(Trailer[] trailers) {
        this.trailers = trailers;
    }
}
