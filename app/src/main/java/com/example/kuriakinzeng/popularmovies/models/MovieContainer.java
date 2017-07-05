package com.example.kuriakinzeng.popularmovies.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kuriakinzeng on 7/5/17.
 */

public class MovieContainer {
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("results")
    @Expose
    private Movie[] movies = null;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Movie[] getMovies() {
        return movies;
    }

    public void setMovies(Movie[] movies) {
        this.movies = movies;
    }

    @Override
    public String toString() {
        return "MovieContainer{" +
                "page=" + page +
                ", totalResults=" + totalResults +
                ", totalPages=" + totalPages +
                ", movies=" + Arrays.toString(movies) +
                '}';
    }
}
