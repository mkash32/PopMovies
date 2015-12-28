package com.example.mkash32.popmovies.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mkash32.popmovies.Constants;
import com.example.mkash32.popmovies.Movie;
import com.example.mkash32.popmovies.R;
import com.example.mkash32.popmovies.Adapters.RecyclerGridAdapter;
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

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private ArrayList<Movie> movies = new ArrayList<Movie>();
    private RecyclerView recyclerGrid;
    private TextView errorTV;
    private RecyclerGridAdapter recyclerAdapter;
    private SwipeRefreshLayout refreshLayout;
    private boolean currentSortPop = true;  //current sorted state, initially will be sorted by popularity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        errorTV = (TextView) findViewById(R.id.tv_error);
        recyclerGrid = (RecyclerView) findViewById(R.id.recycler_grid);
        recyclerGrid.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        movies.size();
        recyclerAdapter = new RecyclerGridAdapter(movies,this);
        recyclerGrid.setAdapter(recyclerAdapter);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);

        setSupportActionBar(toolbar);

        fetchMovies(Constants.GET_MOVIES_POP_URL);

    }

    @Override
    public boolean  onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this,AboutActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_refresh){
            refreshLayout.setRefreshing(true);
            onRefresh();
        }
        else if(id == R.id.movieSortPop && !currentSortPop){
            fetchMovies(Constants.GET_MOVIES_POP_URL);
        }
        else if(id == R.id.movieSortRating){
            fetchMovies(Constants.GET_MOVIES_RATED_URL);
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchMovies(final String url){
        if(!Utils.isNetworkAvailable(this)){
            refreshLayout.setRefreshing(false);
            errorTV.setVisibility(View.VISIBLE);
        }
        else {
            errorTV.setVisibility(View.GONE);
            FetchMoviesTask fmt = new FetchMoviesTask();
            fmt.execute(url);
        }
    }

    @Override
    public void onRefresh() {
        String url = currentSortPop?Constants.GET_MOVIES_POP_URL:Constants.GET_MOVIES_RATED_URL;
        fetchMovies(url);
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
                if(urls[0].equals(Constants.GET_MOVIES_POP_URL))
                    currentSortPop = true;
                else
                    currentSortPop = false;
            }

            //Converting string into JSON and parsing into Movie Objects
            try {

                JSONObject js = new JSONObject(responseString);
                ArrayList<Movie> movies = Utils.parsePopularMovies(js);
                return movies;

            } catch (JSONException e) {
                e.printStackTrace();
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
        }
    }
}
