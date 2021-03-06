package com.example.kuriakinzeng.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kuriakinzeng.popularmovies.data.FavoriteMovieContract;
import com.example.kuriakinzeng.popularmovies.data.FavoriteMovieContract.FavoriteMovieEntry;
import com.example.kuriakinzeng.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuriakinzeng on 6/30/17.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieHolder> {

    private final static String TAG = "Adapter";
    final String IMAGE_BASE_PATH = "http://image.tmdb.org/t/p/";
    final String IMAGE_SIZE = "w185";
    
    private LayoutInflater mLayoutInflater;
    private Movie[] mMovieList;
    private Cursor mCursor;
    private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movieChosen);
    }
    
    public MovieListAdapter (MovieAdapterOnClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
    }

    public Movie[] getMovieList() {
        return mMovieList;
    }

    public void setMovieList (Movie[] movieList) {
        mMovieList = movieList;
        notifyDataSetChanged();
    }

    public void setCursor(Cursor cursor) {
        if (cursor == mCursor) {
            return;
        }
        this.mCursor = cursor;
        
        List<Movie> newMovieList = new ArrayList<Movie>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Integer id = cursor.getInt(cursor.getColumnIndex(FavoriteMovieEntry._ID));
                String title = cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_TITLE));
                String posterPath = cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_POSTER_PATH));
                String overview = cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_OVERVIEW));
                Double voteAverage = cursor.getDouble(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_VOTE_AVERAGE));
                String releaseDate = cursor.getString(cursor.getColumnIndex(FavoriteMovieEntry.COLUMN_RELEASE_DATE));
                Movie movie = new Movie(id, voteAverage, title, posterPath, overview, releaseDate);
                newMovieList.add(movie);
//                Log.w(TAG, movie.toString());
            } while (cursor.moveToNext());
        }
        Movie[] movieList = new Movie[newMovieList.size()];
        newMovieList.toArray(movieList);
        this.setMovieList(movieList);
//        Log.w(TAG, String.valueOf(movieList.length));
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
        Picasso.with(context).load(IMAGE_BASE_PATH + IMAGE_SIZE + movie.getPosterPath()).into(holder.mMovieThumbnail);
//        Log.w(TAG, movie.getPosterPath());
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
        }
    }
}
