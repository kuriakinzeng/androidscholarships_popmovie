package com.example.kuriakinzeng.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kuriakinzeng.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry;

/**
 * Created by kuriakinzeng on 7/12/17.
 */

public class FavoriteMovieDBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 3;
    private static final String DATABASE_NAME = "favorite_movie.db";

    public FavoriteMovieDBHelper (Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " + FavoriteMovieEntry.TABLE_NAME + " (" + 
                FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY," +
                FavoriteMovieEntry.COLUMN_POSTER_PATH + " STRING NOT NULL," +
                FavoriteMovieEntry.COLUMN_TITLE + " STRING NOT NULL," + 
                FavoriteMovieEntry.COLUMN_RELEASE_DATE + " STRING NOT NULL," +
                FavoriteMovieEntry.COLUMN_VOTE_AVERAGE + " DOUBLE NOT NULL," +
                FavoriteMovieEntry.COLUMN_OVERVIEW + " STRING NOT NULL);";
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
