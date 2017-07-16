package com.example.kuriakinzeng.popularmovies.utils;

import com.example.kuriakinzeng.popularmovies.models.MovieContainer;
import com.example.kuriakinzeng.popularmovies.models.ReviewContainer;
import com.example.kuriakinzeng.popularmovies.models.TrailerContainer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by kuriakinzeng on 7/15/17.
 */

public interface MovieDBService {
    @GET("movie/{sort}")
    Call<MovieContainer> getMovies(@Path("sort") String sort, @Query("api_key") String apiKey);
    
    @GET("movie/{id}/reviews")
    Call<ReviewContainer> getReviewsById(@Path("id") Integer movieId, @Query("api_key") String apiKey);
    
    @GET("movie/{id}/videos")
    Call<TrailerContainer> getTrailersById(@Path("id") Integer movieId, @Query("api_key") String apiKey);
}
