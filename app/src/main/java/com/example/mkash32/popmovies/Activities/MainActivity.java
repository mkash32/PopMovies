package com.example.mkash32.popmovies.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mkash32.popmovies.Constants;
import com.example.mkash32.popmovies.Movie;
import com.example.mkash32.popmovies.R;
import com.example.mkash32.popmovies.RecyclerGridAdapter;
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

public class MainActivity extends AppCompatActivity {

    private ArrayList<Movie> movies = new ArrayList<Movie>();
    private RecyclerView recyclerGrid;
    private RecyclerGridAdapter recyclerAdapter;
    private boolean currentSortPop = true;  //current sorted state, initially will be sorted by popularity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerGrid = (RecyclerView) findViewById(R.id.recycler_grid);
        recyclerGrid.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        recyclerAdapter = new RecyclerGridAdapter(movies,this);
        recyclerGrid.setAdapter(recyclerAdapter);

        setSupportActionBar(toolbar);

        FetchMoviesTask fmt = new FetchMoviesTask();
        fmt.execute(Constants.GET_MOVIES_POP_URL);

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
        else if(id == R.id.movieSortPop && !currentSortPop){
            FetchMoviesTask task = new FetchMoviesTask();
            task.execute(Constants.GET_MOVIES_POP_URL);
        }
        else if(id == R.id.movieSortRating){
            FetchMoviesTask task = new FetchMoviesTask();
            task.execute(Constants.GET_MOVIES_RATED_URL);
        }

        return super.onOptionsItemSelected(item);
    }

    public class FetchMoviesTask extends AsyncTask<String,Void,ArrayList<Movie>>
    {

        @Override
        protected void onPreExecute() {
            //check for internet

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

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
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
                Log.d("URL",urls[0]);
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
        }
    }
}