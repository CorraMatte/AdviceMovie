package com.example.corra.myapplication;

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
        new getJSON().execute(MainActivity.HOST_URL + URL_GET_TOTAL_MOVIE + "?id="
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

    private class getJSON extends getJsonData{
        //this method will be called after execution so here we are displaying a toast with the json string
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            UpdateCountText(s);
        }
    }
}
