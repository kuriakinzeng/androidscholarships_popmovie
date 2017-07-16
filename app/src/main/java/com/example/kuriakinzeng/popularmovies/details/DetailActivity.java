package com.example.kuriakinzeng.popularmovies.details;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import com.example.kuriakinzeng.popularmovies.models.MovieContainer;
import com.example.kuriakinzeng.popularmovies.models.Trailer;
import com.example.kuriakinzeng.popularmovies.models.TrailerContainer;
import com.example.kuriakinzeng.popularmovies.utils.MovieDBService;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private final static String TAG = "Detail";
    private static final int DETAIL_ACTIVITY_LOADER_ID = 100;
    
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
                getSupportLoaderManager().initLoader(DETAIL_ACTIVITY_LOADER_ID, null, trailerListLoaderCallbacks);
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
        Log.w(TAG, trailerChosen.getName().toString());
        Toast.makeText(this, trailerChosen.getName().toString(), Toast.LENGTH_LONG);
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(trailerChosen.getUrl())));
    }

    public void addToFavorites () {
        ContentValues values = new ContentValues();
        values.put(FavoriteMovieEntry.COLUMN_TITLE, mTitle.getText().toString());
        values.put(FavoriteMovieEntry.COLUMN_OVERVIEW, mSynopsis.getText().toString());
        values.put(FavoriteMovieEntry.COLUMN_RELEASE_DATE, mReleaseDate.getText().toString());
        values.put(FavoriteMovieEntry.COLUMN_VOTE_AVERAGE, Double.parseDouble(mRating.getText().toString()));
        values.put(FavoriteMovieEntry.COLUMN_POSTER_PATH, mPosterPath);
        values.put(FavoriteMovieEntry.COLUMN_VIDEO, true); // TODO: not required
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

        private void showErrorMessage() {
            mTrailerRecyclerView.setVisibility(View.INVISIBLE);
            mErrorMessageDisplay.setVisibility(View.VISIBLE);
        }

        private void showDataView() {
            mErrorMessageDisplay.setVisibility(View.INVISIBLE);
            mTrailerRecyclerView.setVisibility(View.VISIBLE);
        }
    };
}
