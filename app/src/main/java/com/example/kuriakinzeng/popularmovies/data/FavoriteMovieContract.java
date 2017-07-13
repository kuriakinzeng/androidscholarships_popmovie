package com.example.kuriakinzeng.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.util.List;

/**
 * Created by kuriakinzeng on 7/12/17.
 */

public class FavoriteMovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.kuriakinzeng.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAVORITE_MOVIES = "favorite_movies";
    
    public static final class FavoriteMovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE_MOVIES)
                .build();
        public static final String TABLE_NAME = "favorite_movies";
        public static final String COLUMN_VIDEO = "video";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
    }
}
