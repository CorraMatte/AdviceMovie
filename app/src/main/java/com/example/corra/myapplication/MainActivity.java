package com.example.corra.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements DownloadCallback,
MovieListFragment.OnFragmentInteractionListener,
AdviceListFragment.OnFragmentInteractionListener,
SearchMovieFragment.OnFragmentInteractionListener,
SettingsFragment.OnFragmentInteractionListener{

    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment mNetworkFragment;


    public static final String MOVIE_SELECTED = "com.example.corra.myapplication.MOVIE_SELECTED";

    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean mDownloading = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_movie_list:
                    selectedFragment = MovieListFragment.newInstance();
                    /* Retrieve advice from DB */
                    //retrieveMovies();
                    break;
                case R.id.navigation_search:
                    selectedFragment = SearchMovieFragment.newInstance();
                    break;
                case R.id.navigation_advice_list:
                    selectedFragment = AdviceListFragment.newInstance();
                    break;
                case R.id.navigation_settings:
                    selectedFragment = SettingsFragment.newInstance();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager());

        /* Retrieve advice from DB */
        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, MovieListFragment.newInstance());
        transaction.commit();

        //retrieveMovies();
    }

    /* Send the request to get the movie list, onClick of the user */
    public void searchMovie(View view){
        finishDownloading();
        EditText mTxtSearchMovie = (EditText) findViewById(R.id.txtSearchMovie);
        String url = Movie.searchMovieOnline(mTxtSearchMovie.getText().toString());
        startDownload(url);
    }

    /* Retrieve movie to see from the DB*/
    private void retrieveMovies(){
        ListView mLstShowMovie = (ListView) findViewById(R.id.lstShowMovie);
        ArrayList<String> myStringArray = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, myStringArray);
        mLstShowMovie.setAdapter(adapter);
    }

    public void UpdateMovieSearchList(String result){
        try {
            JSONObject obj = new JSONObject(result);
            JSONArray list = obj.getJSONArray("results");
            final ArrayList<Movie> movie_list = new ArrayList<>();
            ArrayList<String> myStringArray = new ArrayList<>();
            for (int i = 0; i<list.length(); i++){
                movie_list.add(new Movie(list.getJSONObject(i)));
                myStringArray.add(movie_list.get(i).toString());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, myStringArray);

            ListView mLstShowMovie = (ListView) findViewById(R.id.lstShowSearchedMovie);
            mLstShowMovie.setAdapter(adapter);
            mLstShowMovie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(view.getContext(), MovieDetail.class);
                    intent.putExtra(MOVIE_SELECTED, movie_list.get(position));
                    startActivity(intent);
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* Networking routines */
    @Override
    public void updateFromDownload(String result) {
        if (result != null) {
            UpdateMovieSearchList(result);
        } else {
            System.out.println("Error! " + getString(R.string.connection_error));
        }
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

    private void startDownload(String url) {
        if (!mDownloading && mNetworkFragment != null) {
            // Execute the async download.
            mNetworkFragment.startDownload(url);
            mDownloading = true;
        }
    }

    @Override
    public void finishDownloading() {
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
