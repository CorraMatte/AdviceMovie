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
 * {@link AdviceListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdviceListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdviceListFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public static final String ADVICE_ID = "com.example.corra.myapplication.ADVICE_ID";
    public static final String USER_SENDER = "com.example.corra.myapplication.USER_SENDER";

    private final String GET_MOVIE_TO_ACCEPT_URL = "/get_advice_to_accept.php";
    private ListView lstShowAdvice;
    private TextView txtTitleAdvice;
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        lstShowAdvice = (ListView) getActivity().findViewById(R.id.lstShowAdvice);
        txtTitleAdvice = (TextView) getActivity().findViewById(R.id.txtTitleAdvice);

        new getJSON().execute(MainActivity.HOST_URL + GET_MOVIE_TO_ACCEPT_URL + "?id=" +
                              AccessToken.getCurrentAccessToken().getUserId());
    }

    private void UpdateMovieAdviceList(String result){
        final ArrayList<Movie> movieList = new ArrayList<>();
        ArrayList<String> movieTitle = new ArrayList<>();
        final ArrayList<String> idAdvice = new ArrayList<>();
        final ArrayList<String> nameUserSender = new ArrayList<>();
        //System.out.println(result);
        /* Check the connection, if on download JSON advice list*/
        try {
            JSONArray list = new JSONArray(result);

            if (list.length() == 0){
                txtTitleAdvice.setText(getString(R.string.title_advice_list_empty));
                return;
            }
            txtTitleAdvice.setText(getString(R.string.title_advice_list));
            for (int i = 0; i<list.length(); i++){
                movieTitle.add(list.getJSONObject(i).getString("title"));
                movieList.add(new Movie(list.getJSONObject(i)));
                idAdvice.add(list.getJSONObject(i).getString("advice_id"));
                nameUserSender.add(list.getJSONObject(i).getString("name_user_sender"));

            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_list_item_1, movieTitle);
            lstShowAdvice.setAdapter(adapter);

            lstShowAdvice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(view.getContext(), MovieToAcceptActivity.class);
                    intent.putExtra(MainActivity.MOVIE_SELECTED, movieList.get(position));
                    intent.putExtra(ADVICE_ID, idAdvice.get(position));
                    intent.putExtra(USER_SENDER, nameUserSender.get(position));
                    startActivity(intent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public AdviceListFragment(){
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static AdviceListFragment newInstance() {
        AdviceListFragment fragment = new AdviceListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        return inflater.inflate(R.layout.fragment_advice_list, container, false);
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
            UpdateMovieAdviceList(s);
        }
    }
}
