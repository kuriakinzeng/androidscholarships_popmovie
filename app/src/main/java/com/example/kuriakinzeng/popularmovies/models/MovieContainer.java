package com.example.kuriakinzeng.popularmovies.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kuriakinzeng on 7/5/17.
 */

public class MovieContainer {
    @SerializedName("results")
    private Movie[] movies = null;

    public Movie[] getMovies() {
        return movies;
    }
}
