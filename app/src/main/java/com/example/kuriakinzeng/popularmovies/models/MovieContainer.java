package com.example.kuriakinzeng.popularmovies.models;

import java.util.List;

/**
 * Created by kuriakinzeng on 7/5/17.
 */

public class MovieContainer {
    private Integer page;
    
    private Integer totalResults;
    
    private Integer totalPages;
    
    private List<Movie> movies = null;

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

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> results) {
        this.movies = results;
    }
}
