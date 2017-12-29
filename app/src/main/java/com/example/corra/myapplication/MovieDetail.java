package com.example.corra.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class MovieDetail extends AppCompatActivity {

    private static String URL_IMAGE = "https://image.tmdb.org/t/p/w500";

    private TextView txtDetailTitle;
    private TextView txtDetailOrigTitle;
    private TextView txtDetailRate;
    private TextView txtDetailOverview;
    private ImageView imgDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        /* Get the views */
        txtDetailTitle = (TextView) findViewById(R.id.txtDetailTitle);
        txtDetailOrigTitle = (TextView) findViewById(R.id.txtDetailOrigTitle);
        txtDetailRate = (TextView) findViewById(R.id.txtDetailRate);
        txtDetailOverview = (TextView) findViewById(R.id.txtDetailOverview);
        imgDetail = (ImageView) findViewById(R.id.imgDetail);

        Movie movie = (Movie) getIntent().getParcelableExtra(MainActivity.MOVIE_SELECTED);
        fillLayout(movie);
    }

    private void fillLayout(Movie movie){
        /* Fill the textview with the data */
        txtDetailTitle.setText(movie.title);
        String orig_title = "(" + movie.original_title + ")";
        txtDetailOrigTitle.setText(orig_title);

        String rate = getString(R.string.txt_detail_rate);;
        if (movie.vote_average != 0) rate += movie.vote_average.toString();
        else rate += "-";
        txtDetailRate.setText(rate);

        String overview = getString(R.string.txt_overview_title);
        if (movie.overview.equals("")) overview += "Non disponibile";
        else overview += movie.overview;
        txtDetailOverview.setText(overview);

        if (movie.poster_path.equals(""))
            imgDetail.setImageResource(R.drawable.ic_notifications_black_24dp);
        else {
            String url = URL_IMAGE + movie.poster_path;

            // show The Image in a ImageView
            new DownloadImageTask(imgDetail).execute(url);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
