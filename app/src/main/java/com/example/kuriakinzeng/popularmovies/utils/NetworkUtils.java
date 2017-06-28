package com.example.kuriakinzeng.popularmovies.utils;

import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import com.example.kuriakinzeng.popularmovies.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
/**
 * These utilities will be used to communicate with the weather servers.
 */
public final class NetworkUtils {
    
    private static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie";
    private static final String API_KEY_PARAM = "api_key";
    private static final String API_KEY = Resources.getSystem().getString(R.string.api_key);

    /**
     * Builds the URL used to talk to the weather server using a location. This location is based
     * on the query capabilities of the weather provider that we are using.
     *
     * @param endpoint The location that will be queried for.
     * @return The URL to use to query the weather server.
     */
    public static URL buildUrl(String endpoint) {
        Log.w("A",API_KEY);
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(endpoint)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        
        return url;
    }

    /**
     * Builds the URL used to talk to the weather server using latitude and longitude of a
     * location.
     *
     * @param lat The latitude of the location
     * @param lon The longitude of the location
     * @return The Url to use to query the weather server.
     */
    public static URL buildUrl(Double lat, Double lon) {
        /** This will be implemented in a future lesson **/
        return null;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}


