package com.example.kuriakinzeng.popularmovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kuriakinzeng on 6/29/17.
 */

public final class MovieListJsonUtils {
    public static String[] getMovieListFromJson(String movieListJsonString) throws JSONException {
        final String STATUS_CODE = "status_code";
        final String IMAGE_BASE_PATH = "http://image.tmdb.org/t/p/";
        final String IMAGE_SIZE = "w185";
                
        String[] parsedMovieListData = null;
        JSONObject movieListJson = new JSONObject(movieListJsonString);

        if (movieListJson.has(STATUS_CODE)) {
            return null;
        }

        JSONArray resultsArray = movieListJson.getJSONArray("results");
        parsedMovieListData = new String[resultsArray.length()];
        for(int i = 0; i < resultsArray.length(); i++){
            JSONObject movieData = resultsArray.getJSONObject(i);
            String posterPath = movieData.getString("poster_path");
            parsedMovieListData[i] = IMAGE_BASE_PATH + IMAGE_SIZE + posterPath;
//            parsedMovieListData[i] = resultsArray.getString(i);
        }
        
        return parsedMovieListData;
    }
}
