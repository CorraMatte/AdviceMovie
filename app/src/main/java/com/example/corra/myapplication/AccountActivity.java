package com.example.corra.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class AccountActivity extends AppCompatActivity {

    private TextView txtFacebookName;
    private TextView txtFacebookEmail;
    private TextView txtFacebookAge;
    private ImageView imgProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        txtFacebookName = (TextView) findViewById(R.id.txtFacebookName);
        txtFacebookEmail = (TextView) findViewById(R.id.txtFacebookMail);
        txtFacebookAge = (TextView) findViewById(R.id.txtFacebookAge);
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
                            txtFacebookEmail.setText("mail\n" + obj.getString("email"));
                            txtFacebookAge.setText("Età " + obj.getJSONObject("age_range").getString("min"));
                            new DownloadImageTask(imgProfilePicture).execute(
                                    obj.getJSONObject("picture").getJSONObject("data").getString("url")
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,picture,email,age_range");
        request.setParameters(parameters);
        request.executeAsync();
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
