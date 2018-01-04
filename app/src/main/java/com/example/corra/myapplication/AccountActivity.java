package com.example.corra.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AccountActivity extends AppCompatActivity {

    private static final String URL_GET_TOTAL_MOVIE = "/get_total_movie.php";

    private TextView txtFacebookName;
    private TextView txtTotalMovie;
    private TextView txtTotalMovieAdvice;
    private ImageView imgProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        txtFacebookName = (TextView) findViewById(R.id.txtFacebookName);
        txtTotalMovie = (TextView) findViewById(R.id.txtTotalMovie);
        txtTotalMovieAdvice = (TextView) findViewById(R.id.txtAccoutMovieAdvice);

        imgProfilePicture = (ImageView) findViewById(R.id.imgProfilePicture);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                "/" + accessToken.getUserId(),
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        // Insert your code here
                        JSONObject obj = response.getJSONObject();
                        try {
                            txtFacebookName.setText(obj.getString("name"));
                            new DownloadImageTask(imgProfilePicture).execute(
                                    obj.getJSONObject("picture").getJSONObject("data").getString("url")
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,picture");
        request.setParameters(parameters);
        request.executeAsync();
        getJSON(MainActivity.HOST_URL + URL_GET_TOTAL_MOVIE + "?id="
                + accessToken.getUserId());
    }

    private void UpdateCountText(String result){
        try {
            JSONArray obj = new JSONArray(result);
            Integer totalMovie = obj.getJSONObject(0).getInt("total_movie");
            txtTotalMovie.setText(getString(R.string.txt_movie_seen) + ": "
                                    + totalMovie.toString());
            Integer totalAdvice = obj.getJSONObject(1).getInt("total_advice");
            txtTotalMovieAdvice.setText(getString(R.string.txt_account_movie_advice) + ": "
                    + totalAdvice.toString());
        } catch (JSONException e) {
            e.printStackTrace();
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


    //this method is actually fetching the json string
    private void getJSON(final String urlWebService) {
        /*
        * As fetching the json string is a network operation
        * And we cannot perform a network operation in main thread
        * so we need an AsyncTask
        * The constrains defined here are
        * Void -> We are not passing anything
        * Void -> Nothing at progress update as well
        * String -> After completion it should return a string and it will be the json string
        * */
        class GetJSON extends AsyncTask<Void, Void, String> {

            /*this method will be called before execution you can display a progress bar or something
            so that user can understand that he should wait as network operation may take some time */
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            //this method will be called after execution so here we are displaying a toast with the json string
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                UpdateCountText(s);
            }

            //in this method we are fetching the json string
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    //creating a URL
                    URL url = new URL(urlWebService);
                    //Opening the URL using HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();
                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    //A simple string to read values from each line
                    String json;
                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {
                        //appending it to string builder
                        sb.append(json + "\n");
                    }
                    //finally returning the read string
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }

        //creating asynctask object and executing it
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

}
