package com.example.kuriakinzeng.popularmovies.utils;

import android.util.Log;

import com.example.kuriakinzeng.popularmovies.models.Movie;
import com.example.kuriakinzeng.popularmovies.models.MovieContainer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuriakinzeng on 6/29/17.
 */

public final class MovieListJsonUtils {
    private static final String TAG = "JSON_UTILS";
    
    public static Movie[] getMovieListFromJson(String json) throws JSONException {
        Gson gson = new Gson();
        MovieContainer movieContainer = gson.fromJson(json, MovieContainer.class);
//        Log.w(TAG, movieContainer.toString());
        Movie[] movieList = movieContainer.getMovies();
        for (Movie movie : movieList) {
            Log.w(TAG, movie.getPosterPath());
        }
        return movieList;
    }
}
