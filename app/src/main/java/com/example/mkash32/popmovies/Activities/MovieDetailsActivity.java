package com.example.mkash32.popmovies.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mkash32.popmovies.Adapters.MovieDetailsAdapter;
import com.example.mkash32.popmovies.Constants;
import com.example.mkash32.popmovies.Data.MovieDBContract;
import com.example.mkash32.popmovies.Movie;
import com.example.mkash32.popmovies.R;
import com.example.mkash32.popmovies.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MovieDetailsActivity extends AppCompatActivity {

    private Movie movie;
    private ImageView image;
    private String id;
    private MovieDetailsAdapter adapter;
    private android.support.v7.widget.ShareActionProvider shareActionProvider;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.setTitle(getIntent().getStringExtra("title"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = getIntent().getStringExtra("id");

        image = (ImageView) findViewById(R.id.image_parallax);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MovieDetailsAdapter(new Movie(), this);
        recyclerView.setAdapter(adapter);

        Picasso.with(this).load(getIntent().getStringExtra("url")).into(image);

        fetchMovieDetails(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_details, menu);

        //shareActionProvider = (ShareActionProvider) menu.findItem(R.id.action_share).getActionProvider();
        MenuItem shareItem = menu.findItem(R.id.action_share);
        shareActionProvider = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorites && movie != null) {
            SaveFavoriteDBTask task = new SaveFavoriteDBTask();
            task.execute(movie.getId());
        }

        return super.onOptionsItemSelected(item);
    }

    private void setShareIntent()
    {
        if(shareActionProvider != null){

            if(movie.getTrailers().isEmpty())
            {
                shareActionProvider.setShareIntent(null);   //disables share action provider
                return;
            }

            String[] trailer = movie.getTrailers().get(0).split(";");
            Intent shareIntent = new Intent();
            shareIntent.setType("text/plain");
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT,Constants.TRAILER_YOUTUBE+trailer[1]);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Share Trailer");

            shareActionProvider.setShareIntent(shareIntent);
        }
    }


    private void fetchMovieDetails(final String id) {
        //close activity if there is no internet connection
        if(!Utils.isNetworkAvailable(this)){
            Snackbar.make(findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_LONG)
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
        protected void onPreExecute() {
            //check for internet

        }

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
            setShareIntent();
        }
    }

    public class ReadMovieDBTask extends AsyncTask<String,Void,Movie> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Movie doInBackground(String... ids) {

            Cursor c = getContentResolver().query(MovieDBContract.MovieEntry.buildMovieUri(Long.parseLong(ids[0])), null, null, null, null);
            Movie dbMovie = Utils.readMovieFromCursor(c);

            return dbMovie;
        }

        @Override
        protected void onPostExecute(Movie result) {
            super.onPostExecute(result);
            movie = result;
            adapter.setMovie(movie);
            adapter.notifyDataSetChanged();
            setShareIntent();
        }
    }

    public class SaveFavoriteDBTask extends AsyncTask<String,Void,Boolean> {
        @Override
        protected Boolean doInBackground(String... ids) {
            try {
                getContentResolver().insert(MovieDBContract.FavoritesEntry.CONTENT_URI, Utils.preparetoSaveFavorite(ids[0]));
            }catch (SQLiteConstraintException exception){
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if(success)
                Toast.makeText(context,"Saved as Favorite",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context,"Already saved!",Toast.LENGTH_SHORT).show();
        }
    }
}
