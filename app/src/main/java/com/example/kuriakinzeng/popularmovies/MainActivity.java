package com.example.kuriakinzeng.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieAdapterOnClickHandler {
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private RecyclerView mMovieRecyclerView;
    private MovieListAdapter mMovieListAdapter;
    private static final String TAG = "Main";
    private static final int NUMBER_OF_COLUMNS = 2;
    private static final String POPULAR_ENDPOINT = "popular";
    private static final String TOP_RATED_ENDPOINT = "top_rated";
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
        
        new FetchMovieList().execute(POPULAR_ENDPOINT); 
    }

    public class FetchMovieList extends AsyncTask<String, Void, Movie[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            if(params.length == 0){
                return null;
            }
            URL movieListUrl = NetworkUtils.buildUrl(params[0]);
            
            try {
                String movieListJsonResponse = NetworkUtils.getResponseFromHttpUrl(movieListUrl);
                Movie[] movieList = MovieListJsonUtils.getMovieListFromJson(movieListJsonResponse);
                return movieList;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movieList) {
            super.onPostExecute(movieList);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieList != null) {
                showDataView();
                mMovieListAdapter.setMovieList(movieList);
            } else {
                showErrorMessage();
            }
        }
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
            mMovieListAdapter.setMovieList(null);
            new FetchMovieList().execute(POPULAR_ENDPOINT);
            return true;
        } 
        
        if (id == R.id.sort_by_rating) {
            mMovieListAdapter.setMovieList(null);
            new FetchMovieList().execute(TOP_RATED_ENDPOINT);
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
}
