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

import org.json.JSONArray;
import org.json.JSONException;

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
        new getJSON().execute(MainActivity.HOST_URL + URL_GET_MOVIE_SEEN + "?id=" +
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

    private class getJSON extends getJsonData{
        //this method will be called after execution so here we are displaying a toast with the json string
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            UpdateMovieSeenList(s);
        }
    }
}
