package com.example.kuriakinzeng.popularmovies;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kuriakinzeng.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mSynopsis;
    private TextView mRating;
    private ImageView mThumbnail;
    private final static String intentName = "movieObject";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mTitle = (TextView) findViewById(R.id.tv_detail_title);
        mReleaseDate = (TextView) findViewById(R.id.tv_detail_release_date);
        mSynopsis = (TextView) findViewById(R.id.tv_detail_synopsis);
        mRating = (TextView) findViewById(R.id.tv_detail_rating);
        mThumbnail = (ImageView) findViewById(R.id.iv_detail_thumbnail);
        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity != null) {
            if(intentThatStartedThisActivity.hasExtra(intentName)){
                Movie movieChosen = (Movie) intentThatStartedThisActivity.getSerializableExtra(intentName);
                mTitle.setText(movieChosen.getTitle());
                mRating.setText(movieChosen.getVoteAverage().toString());
                mSynopsis.setText(movieChosen.getOverview());
                mReleaseDate.setText(movieChosen.getReleaseDate());
                Picasso.with(this).load(movieChosen.getPosterPath()).into(mThumbnail);
            }
        }
    }
}
