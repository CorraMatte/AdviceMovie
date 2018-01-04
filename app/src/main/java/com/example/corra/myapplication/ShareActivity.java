package com.example.corra.myapplication;

import android.content.Context;
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

import java.util.ArrayList;

public class ShareActivity extends AppCompatActivity {

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
                            ArrayList<Friend> friends = new ArrayList<>();
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
                                        shareMovieToFriend();
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        request.executeAsync();
    }

    public void shareMovieToFriend(){

    }

    private class Friend{
        public String name;
        public String id;

        Friend(String name, String id){
            this.name = name;
            this.id = id;
        }
    }
}
