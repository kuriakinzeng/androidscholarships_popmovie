package com.example.kuriakinzeng.popularmovies.details;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kuriakinzeng.popularmovies.R;
import com.example.kuriakinzeng.popularmovies.models.Review;
import com.example.kuriakinzeng.popularmovies.models.Trailer;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * Created by kuriakinzeng on 7/15/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
    private final static String TAG = "Adapter";

    private LayoutInflater mLayoutInflater;
    private Review[] mReviewList;
    private TextView mReviewAuthor;
    private TextView mReviewContent;

    public void setReviewList (Review[] reviews) {
        mReviewList = reviews;
        notifyDataSetChanged();
    }

    public Review[] getReviewList() {
        return mReviewList;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_review_view, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        Review review = mReviewList[position];
        mReviewAuthor.setText(review.getAuthor());
        mReviewContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if (mReviewList == null){
            return 0;
        }
        return mReviewList.length;
    }

    public class ReviewHolder extends ViewHolder {
        public ReviewHolder (View view) {
            super(view);
            mReviewContent = (TextView) view.findViewById(R.id.tv_review_content);
            mReviewAuthor = (TextView) view.findViewById(R.id.tv_review_author);
        }
    }
}
