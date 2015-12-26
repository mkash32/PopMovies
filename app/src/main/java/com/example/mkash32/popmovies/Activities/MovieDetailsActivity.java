package com.example.mkash32.popmovies.Activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mkash32.popmovies.Constants;
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
    private TextView title;
    private RatingBar rating;
    private ImageView image;
    private Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String id = getIntent().getStringExtra("id");

        title = (TextView) findViewById(R.id.movie_title);
        rating = (RatingBar) findViewById(R.id.ratingBar);
        image = (ImageView) findViewById(R.id.image_backdrop);

        FetchMovieDetailsTask task = new FetchMovieDetailsTask();
        task.execute(id);
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
            title.setText(movie.getTitle());
            rating.setRating((float) (movie.getPopularity()/20));
            String backDropURL = Constants.STANDARD_IMAGE_URLTEMP+movie.getImagePath();
            Picasso.with(activity).load(backDropURL).into(image);

        }
    }
}
