package com.example.kuriakinzeng.popularmovies;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kuriakinzeng.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry;
import com.example.kuriakinzeng.popularmovies.details.DetailActivity;
import com.example.kuriakinzeng.popularmovies.models.Movie;
import com.example.kuriakinzeng.popularmovies.models.MovieContainer;
import com.example.kuriakinzeng.popularmovies.data.MovieDBService;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieAdapterOnClickHandler {
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private RecyclerView mMovieRecyclerView;
    private MovieListAdapter mMovieListAdapter;
    private String mSelectedSortBy;
    private static final String TAG = "Main";
    private static final int NUMBER_OF_COLUMNS = 2;
    private static final String BASE_URL = "https://api.themoviedb.org/3/"; 
    private static final String SORT_BY = "sort_by";
    private static final String SORT_BY_POPULAR = "popular";
    private static final String SORT_BY_RATING = "top_rated";
    private static final String SORT_BY_FAVORITES = "favorites";
    private static final int MOVIE_LIST_LOADER_ID = 0;
    private static final int FAVORITE_MOVIES_LOADER_ID = 1;
    public static final String INTENT_EXTRA_MOVIE_OBJECT = "MOVIE_OBJECT";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mMovieRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        mMovieRecyclerView.setLayoutManager(layoutManager);
        mMovieListAdapter = new MovieListAdapter(this);
        mMovieRecyclerView.setAdapter(mMovieListAdapter);

        mSelectedSortBy  = SORT_BY_POPULAR;
        if (savedInstanceState != null) {
            mSelectedSortBy = savedInstanceState.getString(SORT_BY);
        }
        if(mSelectedSortBy.equals(SORT_BY_FAVORITES)) {
            getSupportLoaderManager().initLoader(FAVORITE_MOVIES_LOADER_ID, null, cursorLoaderCallbacks);    
        } else {
            getSupportLoaderManager().initLoader(MOVIE_LIST_LOADER_ID, null, movieListLoaderCallbacks);    
        }
    }

    private LoaderCallbacks<Movie[]> movieListLoaderCallbacks = new LoaderCallbacks<Movie[]>() {
        public Loader<Movie[]> onCreateLoader(int id, final Bundle loaderArgs) {
            return new AsyncTaskLoader<Movie[]>(MainActivity.this) {
                Movie[] mMovieList = null;

                @Override
                protected void onStartLoading() {
                    if (mMovieList != null) {
                        deliverResult(mMovieList);
                    } else {
                        mLoadingIndicator.setVisibility(View.VISIBLE);
                        forceLoad();
                    }
                }

                @Override
                public Movie[] loadInBackground() {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL) 
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    MovieDBService service = retrofit.create(MovieDBService.class);
                    Call<MovieContainer> call = service.getMovies(mSelectedSortBy, BuildConfig.API_KEY);
                    
                    try {
                        Response<MovieContainer> response = call.execute();
                        MovieContainer movieContainer = response.body();
                        return movieContainer.getMovies();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                public void deliverResult(Movie[] data) {
                    mMovieList = data;
                    super.deliverResult(data);
                }
            };
        }

        public void onLoadFinished(Loader<Movie[]> loader, Movie[] movieList) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mMovieListAdapter.setMovieList(movieList);
            if (movieList != null) {
                showDataView();
            } else {
                showErrorMessage();
            }
        }

        public void onLoaderReset(Loader<Movie[]> loader) {
            // Not used yet
        }
    };

    private LoaderCallbacks<Cursor> cursorLoaderCallbacks = new LoaderCallbacks<Cursor>() {
        public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {
            return new AsyncTaskLoader<Cursor>(MainActivity.this) {
                Cursor mFavoriteMoviesCursor = null;

                @Override
                protected void onStartLoading() {
                    if (mFavoriteMoviesCursor != null) {
                        deliverResult(mFavoriteMoviesCursor);
                    } else {
                        mLoadingIndicator.setVisibility(View.VISIBLE);
                        forceLoad();
                    }
                }

                @Override
                public Cursor loadInBackground() {
                    try {
                        Cursor cursor = getContentResolver().query(FavoriteMovieEntry.CONTENT_URI, null, null, null, null);
                        return cursor;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                public void deliverResult(Cursor data) {
                    mFavoriteMoviesCursor = data;
                    super.deliverResult(data);
                }
            };
        }

        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mMovieListAdapter.setCursor(cursor);
            Movie[] movieList = mMovieListAdapter.getMovieList();
            if (movieList != null) {
                showDataView();
            } else {
                showErrorMessage();
            }
        }

        public void onLoaderReset(Loader<Cursor> loader) {
            mMovieListAdapter.setCursor(null);
        }
    };

    private void invalidateData() {
        mMovieListAdapter.setMovieList(null);
    }
    
    private void showErrorMessage() {
        mMovieRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mMovieRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie movieChosen) {
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(this, destinationClass);
        intentToStartDetailActivity.putExtra(INTENT_EXTRA_MOVIE_OBJECT, movieChosen);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sort_by_popular) {
            mSelectedSortBy = SORT_BY_POPULAR;
            invalidateData();
            Bundle bundleForLoader = new Bundle();
            bundleForLoader.putString(SORT_BY, mSelectedSortBy);
            getSupportLoaderManager().destroyLoader(FAVORITE_MOVIES_LOADER_ID);
            getSupportLoaderManager().restartLoader(MOVIE_LIST_LOADER_ID, bundleForLoader, movieListLoaderCallbacks);
            return true;
        } 
        
        if (id == R.id.sort_by_rating) {
            mSelectedSortBy = SORT_BY_RATING;
            invalidateData();
            Bundle bundleForLoader = new Bundle();
            bundleForLoader.putString(SORT_BY, mSelectedSortBy);
            getSupportLoaderManager().destroyLoader(FAVORITE_MOVIES_LOADER_ID);
            getSupportLoaderManager().restartLoader(MOVIE_LIST_LOADER_ID, bundleForLoader, movieListLoaderCallbacks);
            return true;
        }

        if (id == R.id.show_favorites) {
            mSelectedSortBy = SORT_BY_FAVORITES;
            invalidateData();
            mMovieListAdapter.setCursor(null);
            getSupportLoaderManager().destroyLoader(MOVIE_LIST_LOADER_ID);
            getSupportLoaderManager().restartLoader(FAVORITE_MOVIES_LOADER_ID, null, cursorLoaderCallbacks);
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(SORT_BY, mSelectedSortBy);
        super.onSaveInstanceState(outState);
    }
}
