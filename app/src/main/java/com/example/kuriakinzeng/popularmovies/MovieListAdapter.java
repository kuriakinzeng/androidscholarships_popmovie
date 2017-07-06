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
import android.widget.Toast;

import com.example.kuriakinzeng.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by kuriakinzeng on 6/30/17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieHolder> {

    private final static String TAG = "Adapter";
    
    private LayoutInflater mLayoutInflater;
    private Movie[] mMovieList;
    private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movieChosen);
    }
    
    public MovieListAdapter (MovieAdapterOnClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
    }
    
    public void setMovieList (Movie[] movieList) {
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
        Movie movie = mMovieList[position];
        Context context = holder.itemView.getContext();
        Picasso.with(context).load(movie.getPosterPath()).into(holder.mMovieThumbnail);
    }

    @Override
    public int getItemCount() {
        if (mMovieList == null) 
            return 0;
        return mMovieList.length;
    }
    
    public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        private TextView mMovieTitle;
        private ImageView mMovieThumbnail;
        
        public MovieHolder (View view) {
            super(view);
            mMovieThumbnail = (ImageView) view.findViewById(R.id.iv_movie_thumbnail);
            mMovieThumbnail.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovieList[adapterPosition];
            mClickHandler.onClick(movie);
//            Toast.makeText(context, movie.getPosterPath() + " is clicked!", Toast.LENGTH_SHORT).show();
        }
    }
}
