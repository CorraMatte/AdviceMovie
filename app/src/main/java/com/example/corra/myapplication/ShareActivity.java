package com.example.corra.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ShareActivity extends AppCompatActivity {

    private static final String URL_ADD_ADVICE = "/add_advice.php";

    private Movie movie;
    private ListView lstFriends;
    private Context context = this;

    private TextView txtLeaveComment;
    private TextView txtComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        movie = getIntent().getParcelableExtra(MainActivity.MOVIE_SELECTED);
        lstFriends = (ListView) findViewById(R.id.lstFriends);
        txtComment = (TextView) findViewById(R.id.txtComment);
        txtLeaveComment = (TextView) findViewById(R.id.txtLeaveComment);

        getFriendList();
    }

    private void getFriendList() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken,
                "/" + accessToken.getUserId() + "/friends",
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        // Insert your code here
                        JSONObject obj = response.getJSONObject();
                        try {
                            JSONArray array = obj.getJSONArray("data");
                            final ArrayList<Friend> friends = new ArrayList<>();
                            if (array.length() == 0){
                                txtLeaveComment.setText(getString(R.string.txt_no_friends));
                                txtComment.setVisibility(View.GONE);
                                lstFriends.setVisibility(View.GONE);
                                return;
                            }
                            for (int i = 0; i < array.length(); i++)
                                friends.add(new Friend(array.getJSONObject(i).getString("name"),
                                                array.getJSONObject(i).getString("id")));

                            lstFriends.setAdapter(new ArrayAdapter<>(context,
                                    android.R.layout.simple_list_item_1, friends));
                            lstFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        // SEND DATA
                                        shareMovieToFriend(friends.get(position).id);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        request.executeAsync();
    }

    public void shareMovieToFriend(String id){
        // SEND ADVICE TO FRIEND.ID
        JSONObject postData = new JSONObject();
        try {
            /* DELETE AN AUTO ADVICE*/
            postData.put("id_movie", movie.id);
            postData.put("id_user_sender", AccessToken.getCurrentAccessToken().getUserId());
            postData.put("id_user_recv", id);
            postData.put("user_overview", txtComment.getText());

            postData.put("title", movie.title);
            postData.put("original_title", movie.original_title);
            postData.put("vote_average", movie.vote_average.toString());
            postData.put("release_date", movie.release_date);
            postData.put("overview", movie.overview);
            postData.put("poster_path", movie.poster_path);
            new SendJson().execute(MainActivity.HOST_URL + URL_ADD_ADVICE,
                    postData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class Friend{
        public String name;
        public String id;

        Friend(String name, String id){
            this.name = name;
            this.id = id;
        }
    }

    private class SendJson extends AsyncTask<String, Void, String> {
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
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }
}
