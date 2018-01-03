package com.example.corra.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by corra on 28/12/17.
 */

public class Movie implements Parcelable{
    static final String API_KEY_TMDB = "6d2ad88449829f6c94c1ee93d859a1fd";
    static final String LANG = "it-IT";
    static final  String DOMAIN = "https://api.themoviedb.org/3/search/movie?";

    public String id;
    public String title;
    public String original_title;
    public Double vote_average;
    public String poster_path;
    public String overview;
    public String release_date;

    Movie(JSONObject obj) throws JSONException {
        this.id = obj.getString("id");
        this.title = obj.getString("title");
        this.original_title = obj.getString("original_title");
        this.vote_average = obj.getDouble("vote_average");
        this.poster_path = obj.getString("poster_path");
        this.overview = obj.getString("overview");
        this.release_date = obj.getString("release_date");
    }

    Movie(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.original_title = in.readString();
        this.vote_average = in.readDouble();
        this.overview = in.readString();
        this.release_date = in.readString();
        this.poster_path = in.readString();
    }

    @Override
    public String toString(){
        return this.title;
    }

    static String searchMovieOnline(String movie) {
        return DOMAIN + "query=" + movie.replace(" ", "-").toLowerCase()
                + "&api_key=" + API_KEY_TMDB
                + "&language=" + LANG;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.title);
        parcel.writeString(this.original_title);
        parcel.writeDouble(this.vote_average);
        parcel.writeString(this.overview);
        parcel.writeString(this.release_date);
        parcel.writeString(this.poster_path);
    }

    // This is to de-serialize the object
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

