package com.example.mkash32.popmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

                movies.add(new Movie(id,title,imagePath,releaseDate,popularity));
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
                String imagePath = movieJSON.getString("backdrop_path");
                String posterPath = movieJSON.getString("poster_path");
                String overview = movieJSON.getString("overview");
                movie = new Movie(id,title,imagePath,releaseDate,posterPath,overview,popularity);
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
}
