package com.example.corra.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieSeenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieSeenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieSeenFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private TextView txtMovieSeen;
    private ListView lstMovieSeen;

    private final static String URL_GET_MOVIE_SEEN = "/get_movie_seen.php";

    public MovieSeenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        /* GET MOVIE SEEN FROM DB*/
        lstMovieSeen = (ListView) getActivity().findViewById(R.id.lstMovieSeen);
        txtMovieSeen = (TextView) getActivity().findViewById(R.id.txtMovieSeen);
        getJSON(MainActivity.HOST_URL + URL_GET_MOVIE_SEEN + "?id=" +
                AccessToken.getCurrentAccessToken().getUserId());
    }

    private void UpdateMovieSeenList(String result){
            final ArrayList<Movie> movieList = new ArrayList<>();
            ArrayList<String> movieTitle = new ArrayList<>();

        /* Check the connection, if on download JSON advice list*/
            try {
                JSONArray list = new JSONArray(result);

                if (list.length() == 0){
                    txtMovieSeen.setText(getString(R.string.txt_movie_seen_empty));
                    return;
                }
                txtMovieSeen.setText(getString(R.string.txt_movie_seen));

                for (int i = 0; i<list.length(); i++){
                    movieTitle.add(list.getJSONObject(i).getString("title"));
                    movieList.add(new Movie(list.getJSONObject(i)));
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_list_item_1, movieTitle);
                lstMovieSeen.setAdapter(adapter);

                lstMovieSeen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(view.getContext(), MovieDetailActivity.class);
                        intent.putExtra(MainActivity.MOVIE_SELECTED, movieList.get(position));
                        intent.putExtra(MovieDetailActivity.MOVIE_SEEN, true);
                        startActivity(intent);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     @return A new instance of fragment MovieSeenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieSeenFragment newInstance() {
        MovieSeenFragment fragment = new MovieSeenFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_seen, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

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
                UpdateMovieSeenList(s);
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
