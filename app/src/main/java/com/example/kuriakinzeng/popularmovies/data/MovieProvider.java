package com.example.kuriakinzeng.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.kuriakinzeng.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry;

import static com.example.kuriakinzeng.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry;

/**
 * Created by kuriakinzeng on 7/12/17.
 */

public class MovieProvider extends ContentProvider {

    public static final String TAG = "Movie Provider";
    public static final int CODE_FAVORITE_MOVIES = 100;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavoriteMovieDBHelper mFavoriteMovieDBHelper;
    
    @Override
    public boolean onCreate() {
        mFavoriteMovieDBHelper = new FavoriteMovieDBHelper(getContext());
        return true;
    }
    
    public static UriMatcher buildUriMatcher () {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoriteMovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, FavoriteMovieContract.PATH_FAVORITE_MOVIES, CODE_FAVORITE_MOVIES);
        return matcher;
    }
    

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, 
                        @Nullable String selection, @Nullable String[] selectionArgs, 
                        @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case CODE_FAVORITE_MOVIES: {
                cursor = mFavoriteMovieDBHelper.getReadableDatabase().query(
                        FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("We are not implementing getType yet.");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues cv, @Nullable String s, @Nullable String[] strings) {
        throw new RuntimeException("We are not implementing update yet");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mFavoriteMovieDBHelper.getWritableDatabase();
        
        int match = sUriMatcher.match(uri);
        Uri returnUri;

//        Log.w(TAG, values.toString());
        
        switch (match) {
            case CODE_FAVORITE_MOVIES:
                long id = db.insert(FavoriteMovieEntry.TABLE_NAME, null, values);
                if (id > 0){
                    returnUri = ContentUris.withAppendedId(FavoriteMovieEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        
        // Notify the resolver if the uri has been changed
        getContext().getContentResolver().notifyChange(uri, null);
        
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        throw new RuntimeException("We are not implementing delete yet");
    }
}
