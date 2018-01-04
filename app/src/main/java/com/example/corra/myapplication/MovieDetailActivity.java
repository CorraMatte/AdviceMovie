package com.example.corra.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MovieDetailActivity extends AppCompatActivity {


    public static final String MOVIE_SEEN = "com.example.corra.myapplication.MOVIE_SEEN";
    public static final String IS_IN_LIST = "com.example.corra.myapplication.IS_IN_LIST";

    final static public String URL_IMAGE = "https://image.tmdb.org/t/p/w500";
    final static private String URL_INSERT_MOVIE = "/add_own_list.php";
    final static private String URL_DELETE_ADVICE = "/delete_auto_advice.php";
    final static private String URL_SET_MOVIE_SEEN = "/set_movie_seen.php";

    private TextView txtDetailTitle;
    private TextView txtDetailOrigTitle;
    private TextView txtDetailRate;
    private TextView txtDetailOverview;
    private TextView txtReleaseDate;
    private ImageView imgDetail;

    private Button btnAdd;
    private Button btnDel;
    private Button btnMovieSeen;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        bindFragmentViews();

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnDel = (Button) findViewById(R.id.btnDel);
        btnMovieSeen = (Button) findViewById(R.id.btnMovieSeen);

        movie = (Movie) getIntent().getParcelableExtra(MainActivity.MOVIE_SELECTED);
        fillLayout();

        boolean movieInList = getIntent().getBooleanExtra(IS_IN_LIST, false);
        boolean movieSeen = getIntent().getBooleanExtra(MOVIE_SEEN, false);

        if (!movieSeen)
            if (movieInList) btnAdd.setVisibility(View.GONE);
            else {
                btnDel.setVisibility(View.GONE);
                btnMovieSeen.setVisibility(View.GONE);
            }
        else{
            btnAdd.setVisibility(View.GONE);
            btnDel.setVisibility(View.GONE);
            btnMovieSeen.setVisibility(View.GONE);
        }
    }

    private void bindFragmentViews(){
        txtDetailTitle = (TextView) findViewById(R.id.txtDetailTitle);
        txtDetailOrigTitle = (TextView) findViewById(R.id.txtDetailOrigTitle);
        txtDetailRate = (TextView) findViewById(R.id.txtDetailRate);
        txtDetailOverview = (TextView) findViewById(R.id.txtDetailOverview);
        imgDetail = (ImageView) findViewById(R.id.imgDetail);
        txtReleaseDate = (TextView) findViewById(R.id.txtReleaseDate);
    }

    private void fillLayout(){
        /* Fill the textview with the data */
        txtDetailTitle.setText(movie.title);
        String orig_title = "(" + movie.original_title + ")";
        txtDetailOrigTitle.setText(orig_title);

        String rate = getString(R.string.txt_detail_rate) + " ";
        if (movie.vote_average != 0) rate += movie.vote_average.toString();
        else rate += "-";
        txtDetailRate.setText(rate);

        String overview = "";
        if (!movie.overview.equals("")) overview = movie.overview;
        txtDetailOverview.setText(overview);

        if (!movie.poster_path.equals("")){
            String url = URL_IMAGE + movie.poster_path;
            // show The Image in a ImageView
            new DownloadImageTask(imgDetail).execute(url);
        }

        String release = "";
        if (!movie.release_date.equals(""))
            release = getString(R.string.txt_release_date) + " " + movie.release_date;
        txtReleaseDate.setText(release);
    }

    public void sendMovie(View view){
        JSONObject postData = new JSONObject();
        try {
            postData.put("id", movie.id);
            postData.put("title", movie.title);
            postData.put("original_title", movie.original_title);
            postData.put("vote_average", movie.vote_average.toString());
            postData.put("release_date", movie.release_date);
            postData.put("overview", movie.overview);
            postData.put("poster_path", movie.poster_path);
            postData.put("id_user", AccessToken.getCurrentAccessToken().getUserId());
            new SendDeviceDetails().execute(MainActivity.HOST_URL + URL_INSERT_MOVIE,
                    postData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deleteAdvice(View view){
        JSONObject postData = new JSONObject();
        try {
            /* DELETE AN AUTO ADVICE*/
            postData.put("id_movie", movie.id);
            postData.put("id_user", AccessToken.getCurrentAccessToken().getUserId());
            new SendDeviceDetails().execute(MainActivity.HOST_URL + URL_DELETE_ADVICE,
                    postData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setMovieSeen(View view){
        JSONObject postData = new JSONObject();
        try {
            /* DELETE AN AUTO ADVICE*/
            postData.put("id_movie", movie.id);
            postData.put("id_user", AccessToken.getCurrentAccessToken().getUserId());
            new SendDeviceDetails().execute(MainActivity.HOST_URL + URL_SET_MOVIE_SEEN,
                    postData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void shareMovie(View view){
        Intent intent = new Intent(this, ShareActivity.class);
        intent.putExtra(MainActivity.MOVIE_SELECTED, movie);
        startActivity(intent);
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

    private class SendDeviceDetails extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String data = "";
            HttpURLConnection httpURLConnection = null;
            try {
                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes("PostData=" + params[1]);
                wr.flush();
                wr.close();
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);
                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
            return data;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //if (result.equals("movie_seen"))
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

}
