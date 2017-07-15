package com.example.kuriakinzeng.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kuriakinzeng on 7/15/17.
 */

public class Review implements Parcelable {

    @SerializedName("id")
    private String id;
    @SerializedName("content")
    private String content;
    @SerializedName("author")
    private String author;
    @SerializedName("url")
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(content);
        parcel.writeString(author);
        parcel.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }
    
    // deserialize
    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        public Review createFromParcel(Parcel in) {
            Review review = new Review();
            review.id = in.readString();
            review.content = in.readString();
            review.author = in.readString();
            review.url = in.readString();
            return review;
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
    
}
