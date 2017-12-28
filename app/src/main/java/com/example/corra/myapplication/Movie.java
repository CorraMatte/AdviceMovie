package com.example.corra.myapplication;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by corra on 28/12/17.
 */

public class Movie {
    static final String API_KEY_TMDB = "6d2ad88449829f6c94c1ee93d859a1fd";
    static final String LANG = "it-IT";
    static final  String DOMAIN = "https://api.themoviedb.org/3/search/movie?";

    public String title;
    public String original_title;
    public Double vote_average;
    public String poster_path;
    public String overview;
    public String release_date;

    Movie(JSONObject obj) throws JSONException {
        this.title = obj.getString("title");
        this.original_title = obj.getString("original_title");
        this.vote_average = obj.getDouble("vote_average");
        this.poster_path = obj.getString("poster_path");
        this.overview = obj.getString("overview");
        this.release_date = obj.getString("release_date");
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


}
