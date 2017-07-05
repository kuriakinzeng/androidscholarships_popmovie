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
        final String STATUS_CODE = "status_code";
        final String IMAGE_BASE_PATH = "http://image.tmdb.org/t/p/";
        final String IMAGE_SIZE = "w185";
               
        Gson gson = new Gson();
//        Type type = new TypeToken<Movie[]>() {}.getType();
        Movie[] movieList = gson.fromJson(json, Movie[].class);
        for (Movie task : movieList) {
            System.out.println(task);
            Log.w(TAG,task.toString());
        }
        return movieList;

//        JSONObject movieListJson = new JSONObject(movieListJsonString);
//        if (movieListJson.has(STATUS_CODE)) {
//            return null;
//        }
//        
//        JSONArray resultsArray = movieListJson.getJSONArray("results");
//        MovieContainer movies = gson.fromJson(resultsArray, Movie[].class);
//        for(int i = 0; i < resultsArray.length(); i++){
//            JSONObject movieData = resultsArray.getJSONObject(i);
//            String posterPath = movieData.getString("poster_path");
//            movies[i] = IMAGE_BASE_PATH + IMAGE_SIZE + posterPath;
////            movies[i] = resultsArray.getString(i);
//        }
//        return movies;
    }
}
