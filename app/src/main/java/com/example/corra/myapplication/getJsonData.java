package com.example.corra.myapplication;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by corra on 06/01/18.
 */

public class getJsonData extends AsyncTask<String, Void, String> {

    /*this method will be called before execution you can display a progress bar or something
        so that user can understand that he should wait as network operation may take some time */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //in this method we are fetching the json string
    @Override
    protected String doInBackground(String... strings)  {
        try {
            //creating a URL
            URL url = new URL(strings[0]);
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
