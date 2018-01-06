package com.example.corra.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieToAcceptActivity extends AppCompatActivity {

    private final static String URL_REFUSED = "/advice_refused.php";
    private final static String URL_ACCEPTED = "/advice_accepted.php";

    private TextView txtDetailTitle;
    private TextView txtDetailOrigTitle;
    private TextView txtDetailRate;
    private TextView txtDetailOverview;
    private TextView txtReleaseDate;
    private ImageView imgDetail;

    private TextView txtUserSender;

    Context context = this;

    private String advice_id;
    private Movie movie;
    private String userSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_to_accept);

        movie = (Movie) getIntent().getParcelableExtra(MainActivity.MOVIE_SELECTED);
        advice_id = getIntent().getStringExtra(AdviceListFragment.ADVICE_ID);
        userSender = getIntent().getStringExtra(AdviceListFragment.USER_SENDER);

        bindFragmentViews();
        fillLayout();
    }

    private void bindFragmentViews(){
        txtDetailTitle = (TextView) findViewById(R.id.txtDetailTitle);
        txtDetailOrigTitle = (TextView) findViewById(R.id.txtDetailOrigTitle);
        txtDetailRate = (TextView) findViewById(R.id.txtDetailRate);
        txtDetailOverview = (TextView) findViewById(R.id.txtDetailOverview);
        imgDetail = (ImageView) findViewById(R.id.imgDetail);
        txtReleaseDate = (TextView) findViewById(R.id.txtReleaseDate);
        txtUserSender = (TextView) findViewById(R.id.txtAdviceBy);
    }

    /* Fill the textview with the data */
    private void fillLayout(){

        txtDetailTitle.setText(movie.title);
        String orig_title = "(" + movie.original_title + ")";
        txtDetailOrigTitle.setText(orig_title);

        String rate = getString(R.string.txt_detail_rate) + " ";
        if (movie.vote_average != 0) rate += movie.vote_average.toString() + "/10";
        else rate += "-";
        txtDetailRate.setText(rate);

        String overview = "";
        if (!movie.overview.equals("")) overview = movie.overview;
        txtDetailOverview.setText(overview);

        String release = "";
        if (!movie.release_date.equals(""))
            release = getString(R.string.txt_release_date) + " " + movie.release_date;
        txtReleaseDate.setText(release);

        txtUserSender.setText(getString(R.string.txt_advice_by) + ": " + userSender);

        if (!movie.poster_path.equals("null")){
            String url = MovieDetailActivity.URL_IMAGE + movie.poster_path;
            // show The Image in a ImageView
            new DownloadImageTask(imgDetail).execute(url);
        }
        else imgDetail.setVisibility(View.GONE);
    }

    public void acceptAdvice(View view){
        new getJSON().execute(MainActivity.HOST_URL + URL_ACCEPTED + "?id=" + advice_id);
    }

    public void refuseAdvice(View view){
        new getJSON().execute(MainActivity.HOST_URL + URL_REFUSED + "?id=" + advice_id);
    }

    private class getJSON extends getJsonData{
        //this method will be called after execution so here we are displaying a toast with the json string
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            startActivity(new Intent(context, MainActivity.class));
        }
    }
}
