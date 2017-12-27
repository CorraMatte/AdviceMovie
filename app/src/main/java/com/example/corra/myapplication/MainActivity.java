package com.example.corra.myapplication;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements DownloadCallback {


    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment mNetworkFragment;

    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean mDownloading = false;

    private TextView mTxtTitle;
    private EditText mTxtSearchFilm;
    private Button mBtnSearch;
    private ListView mLstShowFilm;

    public final static String API_KEY = "AIzaSyDHxv1u18M4WM3r4BrGyKM9IBSlntwb1DQ";

    private void hideElements(View... elements){
        for (View elem: elements) {
            elem.setVisibility(View.GONE);
        }
    }
    private void showElements(View... elements){
        for (View elem: elements) {
            elem.setVisibility(View.VISIBLE);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_film_list:
                    hideElements(mTxtSearchFilm, mBtnSearch);
                    showElements(mLstShowFilm);
                    mTxtTitle.setText(R.string.title_film_list);
                    /* Retrieve advice from DB */
                    retrieveMovies();
                    return true;
                case R.id.navigation_search:
                    showElements(mTxtSearchFilm, mBtnSearch);
                    hideElements(mLstShowFilm);
                    mTxtTitle.setText(R.string.title_search);
                    return true;
                case R.id.navigation_advice_list:
                    hideElements(mTxtSearchFilm, mBtnSearch);
                    showElements(mLstShowFilm);
                    mTxtTitle.setText(R.string.title_advice_list);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTxtTitle = findViewById(R.id.txtTitle);
        mBtnSearch = findViewById(R.id.btnSearch);
        mLstShowFilm = findViewById(R.id.lstShowFilm);
        mTxtSearchFilm = findViewById(R.id.txtSearchFilm);
        BottomNavigationView navigation = findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(),
        //        "https://kgsearch.googleapis.com/v1/entities:search");
        System.out.println("State " + this.getActiveNetworkInfo());

        /* Retrieve advice from DB */
        retrieveMovies();
    }

    public void searchMovie(View view){
        JSONArray elements = Utilities.searchMovieOnline(mTxtSearchFilm.getText().toString());
        /*System.out.println("The element is " + elements);*/
    }

    private void retrieveMovies(){
        System.out.println("Retrieve Movies");
        ArrayList<String> myStringArray = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, myStringArray);
        mLstShowFilm.setAdapter(adapter);
    }

    @Override
    public void updateFromDownload(String result) {
        // Update your UI here based on result of download.
    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch(progressCode) {
            // You can add UI behavior for progress updates here.
            case Progress.ERROR:
                //...
                break;
            case Progress.CONNECT_SUCCESS:
                //...
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
                //...
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
                //...
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
            //...
                break;
        }
    }

    @Override
    public void finishDownloading() {
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
    }

}
