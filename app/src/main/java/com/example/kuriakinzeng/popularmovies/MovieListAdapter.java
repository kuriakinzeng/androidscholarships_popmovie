package com.example.kuriakinzeng.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by kuriakinzeng on 6/30/17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieHolder> {

    private final static String TAG = "Adapter";
    
    private LayoutInflater mLayoutInflater;
    private String[] mMovieList;
    
//    public MovieListAdapter (Context context) {
//    
//    }
    
    public void setMovieList (String[] movieList) {
        mMovieList = movieList;
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
        String movieThumbnailPath = mMovieList[position];
//        Context context = 
        Context context = holder.itemView.getContext();
        Picasso.with(context).load(movieThumbnailPath).into(holder.mMovieThumbnail);
//        Log.w(TAG, movie);
//        holder.mMovieTitle.setText(movie);
    }

    @Override
    public int getItemCount() {
        if (mMovieList == null) 
            return 0;
        return mMovieList.length;
    }
    
    public class MovieHolder extends RecyclerView.ViewHolder {
//        private TextView mMovieTitle;
        private ImageView mMovieThumbnail;
        
        public MovieHolder (View view) {
            super(view);
            mMovieThumbnail = (ImageView) view.findViewById(R.id.iv_movie_thumbnail);
        }
        
        public void bind () {
            
        }
    }
}
