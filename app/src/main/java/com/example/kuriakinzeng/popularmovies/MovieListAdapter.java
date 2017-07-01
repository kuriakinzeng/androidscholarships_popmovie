package com.example.kuriakinzeng.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by kuriakinzeng on 6/30/17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieHolder> {

    private final static String TAG = "Adapter";
    
    private LayoutInflater mLayoutInflater;
    private String[] mMovieData;
    
//    public MovieListAdapter (Context context) {
//        
//    }
    
    public void setMovieData (String[] movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    @Override
    public MovieListAdapter.MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_movie_view, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieListAdapter.MovieHolder holder, int position) {
        String movie = mMovieData[position];
        Log.w(TAG, movie);
//        holder.mMovieTitle.setText(movie);
    }

    @Override
    public int getItemCount() {
        if (mMovieData == null) 
            return 0;
        return mMovieData.length;
    }
    
    public class MovieHolder extends RecyclerView.ViewHolder {
        private TextView mMovieTitle;
        
        public MovieHolder (View view) {
            super(view);
            mMovieTitle = (TextView) view.findViewById(R.id.tv_movie_title);
        }
        
        public void bind () {
            
        }
    }
}
