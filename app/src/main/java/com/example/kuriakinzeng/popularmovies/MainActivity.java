package com.example.kuriakinzeng.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kuriakinzeng.popularmovies.utils.MovieListJsonUtils;
import com.example.kuriakinzeng.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;
    private RecyclerView mMovieRecyclerView;
    private MovieListAdapter mMovieListAdapter;
    private static final String TAG = "Main";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        
        mMovieRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mMovieRecyclerView.setLayoutManager(layoutManager);
        mMovieListAdapter = new MovieListAdapter();
        mMovieRecyclerView.setAdapter(mMovieListAdapter);
        
        new FetchMovieList().execute("popular"); 
    }

    public class FetchMovieList extends AsyncTask<String, Void, String[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {
            if(params.length == 0){
                return null;
            }
            URL movieListUrl = NetworkUtils.buildUrl(params[0]);
            
            try {
                String movieListJsonResponse = NetworkUtils.getResponseFromHttpUrl(movieListUrl);
                String[] movieList = MovieListJsonUtils.getMovieListFromJson(movieListJsonResponse);
//                Log.w(TAG, movieListData[0]);
                return movieList;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] movieList) {
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
}
