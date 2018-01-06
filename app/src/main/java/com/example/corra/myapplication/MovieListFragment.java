package com.example.corra.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieListFragment extends Fragment{
    private OnFragmentInteractionListener mListener;
    private TextView txtListWelcolme;

    private final String GET_ADVICE_URL = "/get_accepted_advice.php";

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        txtListWelcolme = (TextView) getActivity().findViewById(R.id.txtListWelcolme);

        retrieveMovies();
    }

    private void setFacebookName(){
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
                            String welcome = getString(R.string.title_movie_list) +  " "  +
                                            obj.getString("first_name");
                            txtListWelcolme.setText(welcome);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    /* Retrieve movie to see from the DB*/
    private void retrieveMovies(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        String url = MainActivity.HOST_URL + GET_ADVICE_URL + "?id=" + accessToken.getUserId();
        new getJSON().execute(url);
    }

    public void UpdateMovieList(String result){
        ListView mLstShowMovie = (ListView) getActivity().findViewById(R.id.lstShowMovie);
        final ArrayList<Movie> movieList = new ArrayList<>();
        //ArrayList<String> movieTitle = new ArrayList<>();
        /* Check the connection, if on download JSON advice list*/
        try {
            JSONArray list = new JSONArray(result);

            if (list.length() == 0){
                txtListWelcolme.setText(getString(R.string.title_movie_list_empty));
                return;
            }

            setFacebookName();
            for (int i = 0; i<list.length(); i++){
                //movieTitle.add(list.getJSONObject(i).getString("title"));
                movieList.add(new Movie(list.getJSONObject(i)));
            }

            ArrayAdapter<Movie> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1, movieList);
            mLstShowMovie.setAdapter(adapter);

            mLstShowMovie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(view.getContext(), MovieDetailActivity.class);
                    intent.putExtra(MainActivity.MOVIE_SELECTED, movieList.get(position));
                    intent.putExtra(MovieDetailActivity.IS_IN_LIST, true);
                    startActivity(intent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public MovieListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MovieListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieListFragment newInstance() {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
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

    private class getJSON extends getJsonData{
        //this method will be called after execution so here we are displaying a toast with the json string
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            UpdateMovieList(s);
        }
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
}
