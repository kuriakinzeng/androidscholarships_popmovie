package com.example.kuriakinzeng.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kuriakinzeng on 7/15/17.
 */

public class Trailer implements Parcelable {

    @SerializedName("id")
    private String id;
    @SerializedName("key")
    private String key;
    @SerializedName("name")
    private String name;
    @SerializedName("site")
    private String site;
    @SerializedName("size")
    private Integer size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(key);
        parcel.writeString(name);
        parcel.writeString(site);
        parcel.writeInt(size);
    }

    @Override
    public int describeContents() {
        return 0;
    }
    
    // deserialize
    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        public Trailer createFromParcel(Parcel in) {
            Trailer trailer = new Trailer();
            trailer.id = in.readString();
            trailer.key = in.readString();
            trailer.name = in.readString();
            trailer.site = in.readString();
            trailer.size = in.readInt();
            return trailer;
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
    
}
