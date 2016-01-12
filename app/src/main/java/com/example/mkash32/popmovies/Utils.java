package com.example.mkash32.popmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.mkash32.popmovies.MovieDBContract;

import java.util.ArrayList;

/**
 * Created by mkash32 on 24/12/15.
 */
// This class contains all utility functions
public class Utils {

    public static ArrayList<Movie> parsePopularMovies(JSONObject popMoviesJSON)
    {
        try {

            JSONArray moviesJSON = popMoviesJSON.getJSONArray("results");
            ArrayList<Movie> movies = new ArrayList<Movie>();

            for(int i =0;i<moviesJSON.length();i++)
            {
                JSONObject movie = moviesJSON.getJSONObject(i);
                String releaseDate = movie.getString("release_date");
                String id = ""+movie.getInt("id");
                String title = movie.getString("title");
                double popularity = movie.getDouble("popularity");
                String imagePath = movie.getString("backdrop_path");
                String posterPath = movie.getString("poster_path");


                movies.add(new Movie(id,title,imagePath,posterPath,releaseDate,popularity));
            }

            return movies;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Movie parseMovieDetails(JSONObject movieJSON)
    {
        Movie movie = null;
        try {
                String releaseDate = movieJSON.getString("release_date");
                String id = ""+movieJSON.getInt("id");
                String title = movieJSON.getString("title");
                double popularity = movieJSON.getDouble("popularity");
                double vote_avg = movieJSON.getDouble("vote_average");
                String imagePath = movieJSON.getString("backdrop_path");
                String posterPath = movieJSON.getString("poster_path");
                String overview = movieJSON.getString("overview");
                int runtime;
                try {
                    runtime = movieJSON.getInt("runtime");
                }catch(JSONException e)
                {
                    runtime= 0;
                }
                movie = new Movie(id,title,imagePath,releaseDate,posterPath,overview,popularity,vote_avg,runtime);
                return movie;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getMovieDetailsURL(String id)
    {
        String url = Constants.GET_MOVIE_DETAILS;
        return url.replace("#",id);
    }

    public static String formatText(Movie movie) {
        String output = "";
        output += "Release Date: " + movie.getReleaseDate()+"\n";
        output += "User Rating : " + movie.getVote_avg()+"\n";
        output += "Popularity  : " + movie.getPopularity()+"\n";
        if(movie.getRuntime() !=0 )
            output += "Runtime     : " + movie.getRuntime()+" min.\n";

        return output;
    }

    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static ArrayList<Movie> readMoviesFromCursor(Cursor c){
        ArrayList<Movie> movies = new ArrayList<Movie>();

        while(c.moveToNext())
        {
            int _id = c.getInt(0);
            String id = ""+_id;
            String title = c.getString(1);
            String image = c.getString(2);
            String poster = c.getString(3);
            String releaseDate = c.getString(4);
            String overview =    c.getString(5);
            int runtime = c.getInt(6);
            float vote_avg = c.getFloat(7);
            float popularity = c.getFloat(8);
            movies.add(new Movie(id,title,image,poster,releaseDate,overview,popularity,vote_avg,runtime));
        }

        c.close();

        return movies;
    }

    public static ContentValues[] prepareToStoreMovies(ArrayList<Movie> movies)
    {
        ContentValues[] values = new ContentValues[movies.size()];

        for(int i=0;i<movies.size();i++)
        {
                ContentValues value = new ContentValues();
                Movie movie = movies.get(i);
                value.put(MovieDBContract.MovieEntry.COLUMN_ID,movie.getId());
                value.put(MovieDBContract.MovieEntry.COLUMN_TITLE,movie.getTitle());
                value.put(MovieDBContract.MovieEntry.COLUMN_IMAGE,movie.getImagePath());
                value.put(MovieDBContract.MovieEntry.COLUMN_POSTER ,movie.getPosterPath());
                value.put(MovieDBContract.MovieEntry.COLUMN_RELEASE_DATE,movie.getReleaseDate());
                value.put(MovieDBContract.MovieEntry.COLUMN_OVERVIEW,movie.getOverview());
                value.put(MovieDBContract.MovieEntry.COLUMN_RUNTIME,movie.getRuntime());
                value.put(MovieDBContract.MovieEntry.COLUMN_VOTE,movie.getVote_avg());
                value.put(MovieDBContract.MovieEntry.COLUMN_POPULARITY,movie.getPopularity());

                values[i] = value;
        }

        return values;
    }




}
