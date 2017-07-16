package com.example.kuriakinzeng.popularmovies.details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kuriakinzeng.popularmovies.R;
import com.example.kuriakinzeng.popularmovies.models.Trailer;

/**
 * Created by kuriakinzeng on 7/15/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.TrailerHolder> {
    private final static String TAG = "Adapter";

    private LayoutInflater mLayoutInflater;
    private Trailer[] mTrailerList;
    private final TrailerAdapterOnClickHandler mClickHandler;

    public interface TrailerAdapterOnClickHandler {
        void onClick(Trailer trailerChosen);
    }
    
    public ReviewAdapter(TrailerAdapterOnClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
    }
    
    public void setTrailerList (Trailer[] trailers) {
        mTrailerList = trailers;
        notifyDataSetChanged();
    }
    
    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_trailer_view, parent, false);
        return new TrailerHolder(view);
    }
    
    @Override
    public void onBindViewHolder(TrailerHolder holder, int position) {
        
    }

    @Override
    public int getItemCount() {
        return 0;
    }
    
    public class TrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TrailerHolder (View view) {
            super(view);
            
        }
        
        @Override
        public void onClick(View view) {
            
        }
    }
}
