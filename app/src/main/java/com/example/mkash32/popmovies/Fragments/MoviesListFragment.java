package com.example.mkash32.popmovies.Fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mkash32.popmovies.Adapters.RecyclerGridAdapter;
import com.example.mkash32.popmovies.Constants;
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
import java.util.ArrayList;


public class MoviesListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private OnFragmentInteractionListener mListener;
    private ArrayList<Movie> movies = new ArrayList<Movie>();
    private Activity activity;
    private RecyclerView recyclerGrid;
    private TextView errorTV;
    private RecyclerGridAdapter recyclerAdapter;
    private SwipeRefreshLayout refreshLayout;
    private int displaySetting = 0; //0-Popular Movies, 1-Highest Rated, 2 - Favorites


    public MoviesListFragment() {
        // Required empty public constructor
    }

    public static MoviesListFragment newInstance() {
        MoviesListFragment fragment = new MoviesListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null)
            displaySetting = savedInstanceState.getInt("display");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies_list, container, false);
        activity = getActivity();

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recyclerGrid = (RecyclerView) view.findViewById(R.id.recycler_grid);

        recyclerGrid.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        recyclerAdapter = new RecyclerGridAdapter(movies,activity,mListener);
        recyclerGrid.setAdapter(recyclerAdapter);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);

        fetchMovies();

        return view;
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("display", displaySetting);
    }

    public void fetchMovies() {

        if(displaySetting == 2 )    //favorites
        {
            ReadMoviesDBTask readMoviesFromDB = new ReadMoviesDBTask();
            readMoviesFromDB.execute(Constants.FAVORITES_URL);
        }
        else
        {
            String url = displaySetting == 0 ? Constants.GET_MOVIES_POP_URL : Constants.GET_MOVIES_RATED_URL;
            if(!Utils.isNetworkAvailable(activity)){
                refreshLayout.setRefreshing(false);
                if(getView()!=null)
                    Snackbar.make(getView(), "No internet connection", Snackbar.LENGTH_LONG)
                            .show();
                ReadMoviesDBTask readMoviesFromDB = new ReadMoviesDBTask();
                readMoviesFromDB.execute(url);
            }
            else {
                FetchMoviesTask fmt = new FetchMoviesTask();
                fmt.execute(url);
            }
        }

    }

    @Override
    public void onRefresh() {
        //don't need to refresh for favorites because there wont be any changes in db
        if(displaySetting < 2)
        {
            refreshLayout.setRefreshing(true);
            fetchMovies();
        }
    }

    public interface OnFragmentInteractionListener {
        void onMovieSelected(Movie movie);
    }

    public class FetchMoviesTask extends AsyncTask<String,Void,ArrayList<Movie>>
    {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected ArrayList<Movie>  doInBackground(String... urls) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String responseString = "";

            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null)
                    return null;

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null)
                    buffer.append(line + "\n");

                if (buffer.length() == 0)
                    return null;

                responseString = buffer.toString();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                return null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }finally{
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

            //Converting string into JSON and parsing into Movie Objects
            try {

                JSONObject js = new JSONObject(responseString);
                ArrayList<Movie> movies = Utils.parsePopularMovies(js);
                return movies;

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception ex)
            {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> result) {
            super.onPostExecute(result);
            movies = result;
            recyclerAdapter.setMovies(result);
            recyclerAdapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
            StoreMoviesDBTask storeTask = new StoreMoviesDBTask();
            storeTask.execute();
        }
    }

    public class StoreMoviesDBTask extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {

            int stored = activity.getContentResolver().bulkInsert(MovieDBContract.MovieEntry.CONTENT_URI,Utils.prepareToStoreMovies(movies,displaySetting));
            return null;
        }
    }

    public class ReadMoviesDBTask extends AsyncTask<String,Void,ArrayList<Movie>>
    {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected ArrayList<Movie>  doInBackground(String... urls) {
            int sortValue;
            Cursor c = null;
            if(urls[0].equals(Constants.FAVORITES_URL))
            {
                displaySetting = 2;
                c = activity.getContentResolver().query(MovieDBContract.FavoritesEntry.CONTENT_URI, null, null, null, null);
            }
            else
            {
                if(urls[0].equals(Constants.GET_MOVIES_POP_URL))
                    displaySetting = 0;
                else
                    displaySetting = 1;

                c = activity.getContentResolver().query(MovieDBContract.MovieEntry.CONTENT_URI, null,MovieDBContract.MovieEntry.COLUMN_SORTPOP+" = "+displaySetting, null, null);
            }


            ArrayList<Movie> dbMovies = Utils.readMoviesFromCursor(c);

            return dbMovies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> result) {
            super.onPostExecute(result);
            movies = result;
            recyclerAdapter.setMovies(result);
            recyclerAdapter.notifyDataSetChanged();
        }
    }

    public int getDisplaySetting() {
        return displaySetting;
    }

    public void setDisplaySetting(int displaySetting) {
        this.displaySetting = displaySetting;
    }
}
