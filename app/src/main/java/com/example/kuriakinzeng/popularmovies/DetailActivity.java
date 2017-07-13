package com.example.kuriakinzeng.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuriakinzeng.popularmovies.data.FavoriteMovieContract;
import com.example.kuriakinzeng.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry;
import com.example.kuriakinzeng.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mSynopsis;
    private TextView mRating;
    private ImageView mThumbnail;
    private String mPosterPath;
    private Button mFavoriteBtn;
    private final static String TAG = "Detail";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mTitle = (TextView) findViewById(R.id.tv_detail_title);
        mReleaseDate = (TextView) findViewById(R.id.tv_detail_release_date);
        mSynopsis = (TextView) findViewById(R.id.tv_detail_synopsis);
        mRating = (TextView) findViewById(R.id.tv_detail_rating);
        mThumbnail = (ImageView) findViewById(R.id.iv_detail_thumbnail);
        // TODO: Change the button text to Remove from Favorites if the movie has been favorited
        mFavoriteBtn = (Button) findViewById(R.id.btn_favorite);
        mFavoriteBtn.setOnClickListener(this);
        
        Intent intentStarter = getIntent();
        if(intentStarter != null) {
            if(intentStarter.hasExtra(MainActivity.INTENT_EXTRA_MOVIE_OBJECT)){
                Movie movieChosen = (Movie) intentStarter
                        .getSerializableExtra(MainActivity.INTENT_EXTRA_MOVIE_OBJECT);
                mTitle.setText(movieChosen.getTitle());
                mRating.setText(movieChosen.getVoteAverage().toString());
                mSynopsis.setText(movieChosen.getOverview());
                mReleaseDate.setText(movieChosen.getReleaseYear());
                mPosterPath = movieChosen.getPosterPath();
                Picasso.with(this).load(movieChosen.getPosterPath()).into(mThumbnail);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_favorite:
                addToFavorites();
                break;
            default:
                break;
        }
    }

    public void addToFavorites () {
        ContentValues values = new ContentValues();
        values.put(FavoriteMovieEntry.COLUMN_TITLE, mTitle.getText().toString());
        values.put(FavoriteMovieEntry.COLUMN_OVERVIEW, mSynopsis.getText().toString());
        values.put(FavoriteMovieEntry.COLUMN_RELEASE_DATE, mReleaseDate.getText().toString());
        values.put(FavoriteMovieEntry.COLUMN_VOTE_AVERAGE, Double.parseDouble(mRating.getText().toString()));
        values.put(FavoriteMovieEntry.COLUMN_POSTER_PATH, mPosterPath);
        values.put(FavoriteMovieEntry.COLUMN_VIDEO, true); // TODO: pass the real value later on
        Uri uri = getContentResolver().insert(FavoriteMovieEntry.CONTENT_URI, values);
        if (uri != null) {
            Toast.makeText(DetailActivity.this, uri.toString(), Toast.LENGTH_LONG).show();
            // TODO: Change the button text to Remove from Favorites
        }
    }
}
