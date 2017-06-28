package com.example.kuriakinzeng.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.kuriakinzeng.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private ProgressBar mLoadingIndicator;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
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
                Log.w("Main: JSON", "JSON Response" + movieListJsonResponse);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
        }
    } 
}
