package com.example.mkash32.popmovies.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mkash32.popmovies.Adapters.MovieDetailsAdapter;
import com.example.mkash32.popmovies.Adapters.MovieDetailsTabAdapter;
import com.example.mkash32.popmovies.Data.MovieDBContract;
import com.example.mkash32.popmovies.Movie;
import com.example.mkash32.popmovies.R;
import com.example.mkash32.popmovies.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MovieDetailsFragment extends Fragment {

    private Movie movie;
    private String id;
    private MovieDetailsTabAdapter adapter;
    private ListView listView;
    private Context context;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    public static MovieDetailsFragment newInstance(String param1, String param2) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
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
        View v = inflater.inflate(R.layout.fragment_movie_details, container, false);
        listView = (ListView) v.findViewById(R.id.list_view);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void fetchMovieDetails() {
        //close activity if there is no internet connection
        if(!Utils.isNetworkAvailable(context)){
            if(getView()!=null)
                Snackbar.make(getView(), "No internet connection", Snackbar.LENGTH_LONG)
                        .show();

            ReadMovieDBTask task = new ReadMovieDBTask();
            task.execute(id);
        }
        else {
            FetchMovieDetailsTask task = new FetchMovieDetailsTask();
            task.execute(id);
        }
    }

    public class FetchMovieDetailsTask extends AsyncTask<String,Void,Movie>
    {
        @Override
        protected Movie  doInBackground(String... ids) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String responseString = "";
            try {
                URL url = new URL(Utils.getMovieDetailsURL(ids[0]));
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                responseString = buffer.toString();

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            //Converting string into JSON and parsing into Movie Object
            try {
                JSONObject js = new JSONObject(responseString);
                Movie movie = Utils.parseMovieDetails(js);
                return movie;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Movie result) {
            super.onPostExecute(result);
            movie = result;
            adapter.setMovie(movie);
            adapter.notifyDataSetChanged();
        }
    }

    public class ReadMovieDBTask extends AsyncTask<String,Void,Movie> {
        @Override
        protected Movie doInBackground(String... ids) {

            Cursor c = getActivity().getContentResolver().query(MovieDBContract.MovieEntry.buildMovieUri(Long.parseLong(ids[0])), null, null, null, null);
            Movie dbMovie = Utils.readMovieFromCursor(c);

            return dbMovie;
        }

        @Override
        protected void onPostExecute(Movie result) {
            super.onPostExecute(result);
            movie = result;
            adapter.setMovie(movie);
            adapter.notifyDataSetChanged();
        }
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        this.id = movie.getId();

        if(adapter == null)
        {
            adapter = new MovieDetailsTabAdapter(movie,context);
            listView.setAdapter(adapter);
        }
        else {
            //initial displaying of movie details
            adapter.setMovie(movie);
            adapter.notifyDataSetChanged();
        }
        //movie details should be fetched again for Trailers
        fetchMovieDetails();
    }
}
