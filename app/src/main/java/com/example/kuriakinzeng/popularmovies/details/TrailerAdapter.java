package com.example.kuriakinzeng.popularmovies.details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.kuriakinzeng.popularmovies.R;
import com.example.kuriakinzeng.popularmovies.models.Trailer;
import com.squareup.picasso.Picasso;

/**
 * Created by kuriakinzeng on 7/15/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {
    private final static String TAG = "Adapter";

    private LayoutInflater mLayoutInflater;
    private Trailer[] mTrailerList;
    private final TrailerAdapterOnClickHandler mClickHandler;

    public interface TrailerAdapterOnClickHandler {
        void onClick(Trailer trailerChosen);
    }
    
    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
    }
    
    public void setTrailerList (Trailer[] trailers) {
        mTrailerList = trailers;
        notifyDataSetChanged();
    }

    public Trailer[] getTrailerList() {
        return mTrailerList;
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
        Trailer trailer = mTrailerList[position];
        Context context = holder.itemView.getContext();
        String thumbnailUrl = "http://img.youtube.com/vi/" + trailer.getKey() + "/0.jpg";
        Picasso.with(context).load(thumbnailUrl).into(holder.mTrailerThumbnail);
    }

    @Override
    public int getItemCount() {
        if (mTrailerList == null){
            return 0;
        }
        return mTrailerList.length;
    }
    
    public class TrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mTrailerThumbnail;
        
        public TrailerHolder (View view) {
            super(view);
            mTrailerThumbnail = (ImageView) view.findViewById(R.id.iv_trailer_thumbnail);
            mTrailerThumbnail.setOnClickListener(this);
        }
        
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Trailer trailer = mTrailerList[adapterPosition];
            mClickHandler.onClick(trailer);
        }
    }
}
