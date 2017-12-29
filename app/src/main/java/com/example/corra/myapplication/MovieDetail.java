package com.example.corra.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MovieDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Movie movie = (Movie) getIntent().getParcelableExtra(MainActivity.MOVIE_SELECTED);
        System.out.println(movie);
    }

}
