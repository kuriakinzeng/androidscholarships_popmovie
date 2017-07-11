package com.example.kuriakinzeng.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.PersistableBundle;
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
import android.widget.Toast;

import com.example.kuriakinzeng.popularmovies.models.Movie;
import com.example.kuriakinzeng.popularmovies.utils.MovieListJsonUtils;
import com.example.kuriakinzeng.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieAdapterOnClickHandler, 
        LoaderCallbacks<Movie []> {
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private RecyclerView mMovieRecyclerView;
    private MovieListAdapter mMovieListAdapter;
    private String mSelectedEndpoint;
    private static final String TAG = "Main";
    private static final int NUMBER_OF_COLUMNS = 2;
    private static final String ENDPOINT = "endpoint";
    private static final String POPULAR_ENDPOINT = "popular";
    private static final String TOP_RATED_ENDPOINT = "top_rated";
    private static final int MOVIE_LIST_LOADER_ID = 0;
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
        
        mSelectedEndpoint  = POPULAR_ENDPOINT;
        LoaderCallbacks<Movie[]> callback = MainActivity.this;
        Bundle bundleForLoader = new Bundle();
        bundleForLoader.putString(ENDPOINT, mSelectedEndpoint);
        getSupportLoaderManager().initLoader(MOVIE_LIST_LOADER_ID, bundleForLoader, callback);
    }

    @Override
    public Loader<Movie[]> onCreateLoader(int id, final Bundle loaderArgs) {
        return new AsyncTaskLoader<Movie[]>(this) {
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
                String endpoint = loaderArgs.getString(ENDPOINT);
                URL movieListUrl = NetworkUtils.buildUrl(endpoint);

                try {
                    String movieListJsonResponse = NetworkUtils.getResponseFromHttpUrl(movieListUrl);
                    Movie[] movieList = MovieListJsonUtils.getMovieListFromJson(movieListJsonResponse);
                    return movieList;
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

    @Override
    public void onLoadFinished(Loader<Movie[]> loader, Movie[] movieList) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mMovieListAdapter.setMovieList(movieList);
        if (movieList != null) {
            showDataView();
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<Movie[]> loader) {
        // Not used yet
    }

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
            mSelectedEndpoint = POPULAR_ENDPOINT;
            invalidateData();
            Bundle bundleForLoader = new Bundle();
            bundleForLoader.putString(ENDPOINT, mSelectedEndpoint);
            getSupportLoaderManager().restartLoader(MOVIE_LIST_LOADER_ID, bundleForLoader, this);
            return true;
        } 
        
        if (id == R.id.sort_by_rating) {
            mSelectedEndpoint = TOP_RATED_ENDPOINT;
            invalidateData();
            Bundle bundleForLoader = new Bundle();
            bundleForLoader.putString(ENDPOINT, mSelectedEndpoint);
            getSupportLoaderManager().restartLoader(MOVIE_LIST_LOADER_ID, bundleForLoader, this);
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ENDPOINT, mSelectedEndpoint);
    }
}
