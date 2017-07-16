package com.example.kuriakinzeng.popularmovies.details;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuriakinzeng.popularmovies.BuildConfig;
import com.example.kuriakinzeng.popularmovies.MainActivity;
import com.example.kuriakinzeng.popularmovies.R;
import com.example.kuriakinzeng.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry;
import com.example.kuriakinzeng.popularmovies.details.TrailerAdapter.TrailerAdapterOnClickHandler;
import com.example.kuriakinzeng.popularmovies.models.Movie;
import com.example.kuriakinzeng.popularmovies.models.Review;
import com.example.kuriakinzeng.popularmovies.models.ReviewContainer;
import com.example.kuriakinzeng.popularmovies.models.Trailer;
import com.example.kuriakinzeng.popularmovies.models.TrailerContainer;
import com.example.kuriakinzeng.popularmovies.utils.MovieDBService;
import com.squareup.picasso.Picasso;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.nio.BufferUnderflowException;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.attr.button;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, TrailerAdapterOnClickHandler {

    private Integer mId;
    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mSynopsis;
    private TextView mRating;
    private ImageView mThumbnail;
    private String mPosterPath;
    private Button mFavoriteBtn;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private RecyclerView mTrailerRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private RecyclerView mReviewRecyclerView;
    private ReviewAdapter mReviewAdapter;

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private final static String TAG = "Detail";
    private final static int TRAILER_LOADER_ID = 100;
    private final static int REVIEW_LOADER_ID = 101;
    public final static String TRAILERS_OBJECT = "TRAILERS_OBJECT";
    public final static String REVIEWS_OBJECT = "REVIEWS_OBJECT";
    
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
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.rv_trailer_list);
        LinearLayoutManager trailerLM = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTrailerRecyclerView.setLayoutManager(trailerLM);
        mTrailerAdapter = new TrailerAdapter(this);
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        mReviewRecyclerView = (RecyclerView) findViewById(R.id.rv_review_list);
        LinearLayoutManager reviewLM = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewRecyclerView.setLayoutManager(reviewLM);
        mReviewAdapter = new ReviewAdapter();
        mReviewRecyclerView.setAdapter(mReviewAdapter);
        
        Intent intentStarter = getIntent();
        if(intentStarter != null) {
            if(intentStarter.hasExtra(MainActivity.INTENT_EXTRA_MOVIE_OBJECT)){
                Movie movieChosen = (Movie) intentStarter
                        .getParcelableExtra(MainActivity.INTENT_EXTRA_MOVIE_OBJECT);
                mId = movieChosen.getId();
                mTitle.setText(movieChosen.getTitle());
                mRating.setText(movieChosen.getVoteAverage().toString());
                mSynopsis.setText(movieChosen.getOverview());
                mReleaseDate.setText(movieChosen.getReleaseYear());
                mPosterPath = movieChosen.getPosterPath();
                Picasso.with(this).load(movieChosen.getPosterPath()).into(mThumbnail);
                // Try if we can recover trailers from preference
                if(!loadTrailersFromCache(mId)) {
                    getSupportLoaderManager().initLoader(TRAILER_LOADER_ID, null, trailerListLoaderCallbacks);
                }
                if(!loadReviewsFromCache(mId)) {
                    getSupportLoaderManager().initLoader(REVIEW_LOADER_ID, null, reviewListLoaderCallbacks);
                }
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

    @Override
    public void onClick(Trailer trailerChosen) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailerChosen.getUrl())));
    }

    public void addToFavorites () {
        ContentValues values = new ContentValues();
        values.put(FavoriteMovieEntry.COLUMN_TITLE, mTitle.getText().toString());
        values.put(FavoriteMovieEntry.COLUMN_OVERVIEW, mSynopsis.getText().toString());
        values.put(FavoriteMovieEntry.COLUMN_RELEASE_DATE, mReleaseDate.getText().toString());
        values.put(FavoriteMovieEntry.COLUMN_VOTE_AVERAGE, Double.parseDouble(mRating.getText().toString()));
        values.put(FavoriteMovieEntry.COLUMN_POSTER_PATH, mPosterPath);
        values.put(FavoriteMovieEntry.COLUMN_VIDEO, true); // TODO: Not required
        Uri uri = getContentResolver().insert(FavoriteMovieEntry.CONTENT_URI, values);
        if (uri != null) {
            Toast.makeText(DetailActivity.this, uri.toString(), Toast.LENGTH_LONG).show();
            // TODO: Change the button text to Remove from Favorites
        }
    }

    private LoaderManager.LoaderCallbacks<Trailer[]> trailerListLoaderCallbacks = new LoaderManager.LoaderCallbacks<Trailer[]>() {
        public Loader<Trailer[]> onCreateLoader(int id, final Bundle loaderArgs) {
            return new AsyncTaskLoader<Trailer[]>(DetailActivity.this) {
                Trailer[] mTrailerList = null;

                @Override
                protected void onStartLoading() {
                    if (mTrailerList != null) {
                        deliverResult(mTrailerList);
                    } else {
                        mLoadingIndicator.setVisibility(View.VISIBLE);
                        forceLoad();
                    }
                }

                @Override
                public Trailer[] loadInBackground() {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    MovieDBService service = retrofit.create(MovieDBService.class);
                    Call<TrailerContainer> call = service.getTrailersById(mId, BuildConfig.API_KEY);

                    try {
                        Response<TrailerContainer> response = call.execute();
                        TrailerContainer trailerContainer = response.body();
                        return trailerContainer.getTrailers();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                public void deliverResult(Trailer[] data) {
                    mTrailerList = data;
                    super.deliverResult(data);
                }
            };
        }

        public void onLoadFinished(Loader<Trailer[]> loader, Trailer[] trailerList) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mTrailerAdapter.setTrailerList(trailerList);
            if (trailerList != null) {
                showDataView();
            } else {
                showErrorMessage();
            }
        }
        
        public void onLoaderReset(Loader<Trailer[]> loader) {
            // Not used yet
        }
    };

    private LoaderManager.LoaderCallbacks<Review[]> reviewListLoaderCallbacks = new LoaderManager.LoaderCallbacks<Review[]>() {
        public Loader<Review[]> onCreateLoader(int id, final Bundle loaderArgs) {
            return new AsyncTaskLoader<Review[]>(DetailActivity.this) {
                Review[] mReviewList = null;

                @Override
                protected void onStartLoading() {
                    if (mReviewList != null) {
                        deliverResult(mReviewList);
                    } else {
                        mLoadingIndicator.setVisibility(View.VISIBLE);
                        forceLoad();
                    }
                }

                @Override
                public Review[] loadInBackground() {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    MovieDBService service = retrofit.create(MovieDBService.class);
                    Call<ReviewContainer> call = service.getReviewsById(mId, BuildConfig.API_KEY);

                    try {
                        Response<ReviewContainer> response = call.execute();
                        ReviewContainer reviewContainer = response.body();
                        return reviewContainer.getReviews();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                public void deliverResult(Review[] data) {
                    mReviewList = data;
                    super.deliverResult(data);
                }
            };
        }

        public void onLoadFinished(Loader<Review[]> loader, Review[] reviews) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mReviewAdapter.setReviewList(reviews);
            if (reviews != null) {
                showDataView();
            } else {
                showErrorMessage();
            }
        }

        public void onLoaderReset(Loader<Review[]> loader) {
            // Not used yet
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        cacheTrailers();
        cacheReviews();
    }

    @Override
    public void onBackPressed() {
        cacheTrailers();
        cacheReviews();
        super.onBackPressed();
    }

    private void cacheTrailers(){
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Trailer[] trailers = mTrailerAdapter.getTrailerList();
        String trailerJson = new Gson().toJson(trailers);
        editor.putString(TRAILERS_OBJECT+String.valueOf(mId), trailerJson);
        editor.commit();
    }
    
    private void cacheReviews(){
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Review[] reviews = mReviewAdapter.getReviewList();
        String reviewJson = new Gson().toJson(reviews);
        editor.putString(REVIEWS_OBJECT+String.valueOf(mId), reviewJson);
        editor.commit();
    }

    private boolean loadTrailersFromCache(Integer movieId){
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String trailerJson = prefs.getString(TRAILERS_OBJECT + String.valueOf(movieId), null);
        Trailer[] trailers = new Gson().fromJson(trailerJson, Trailer[].class);
        mTrailerAdapter.setTrailerList(trailers);
        return trailers != null && trailers.length > 0;
    }

    private boolean loadReviewsFromCache(Integer movieId){
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        String reviewJson = prefs.getString(REVIEWS_OBJECT + String.valueOf(movieId), null);
        Review[] reviews = new Gson().fromJson(reviewJson, Review[].class);
        mReviewAdapter.setReviewList(reviews);
        return reviews != null && reviews.length > 0;
    }

    private void showErrorMessage() {
        mTrailerRecyclerView.setVisibility(View.INVISIBLE);
        mReviewRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mTrailerRecyclerView.setVisibility(View.VISIBLE);
        mReviewRecyclerView.setVisibility(View.VISIBLE);
    }
}
