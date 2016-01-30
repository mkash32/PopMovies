package com.example.mkash32.popmovies;

import java.util.Date;

/**
 * Created by mkash32 on 23/12/15.
 */
public class Constants {

    //Base URL and relative paths
    //public static String API_KEY = "api_key="; //  API key generated from themoviedb.org --> https://www.themoviedb.org/documentation/api
    public static String currentYear = ""+new Date().getYear();

    public static String MOVIE_BASE_ADDRESS = "https://api.themoviedb.org/3";
    public static String POPULAR_MOVIES = "/discover/movie?sort_by=";
        public static String SORT_BY_POPULARITY = "popularity.desc";
        public static String SORT_BY_HIGHEST_RATED = "vote_average.desc";
    public static String FIND_MOVIE = "/movie/#";
        public static String APPEND_TO_RESPONSE = "&append_to_response=videos";

    public static String IMAGE_BASE_ADDRESS = "http://image.tmdb.org/t/p";
    public static String STANDARD_WIDTH = "/w185";
    public static String WIDE = "/w342";

    public static String FAVORITES_URL = "favorites_url";   //indicator for favorites in ReadMovies AsyncTask

    //Final URL tempelates
    public static String GET_MOVIES_POP_URL = MOVIE_BASE_ADDRESS+POPULAR_MOVIES+SORT_BY_POPULARITY+"&"+API_KEY;
    public static String GET_MOVIES_RATED_URL = MOVIE_BASE_ADDRESS+POPULAR_MOVIES+SORT_BY_HIGHEST_RATED+"&"+API_KEY;

    public static String GET_TRAILERS = MOVIE_BASE_ADDRESS+FIND_MOVIE+"/videos?"+API_KEY;
    public static String GET_REVIEWS = MOVIE_BASE_ADDRESS+FIND_MOVIE+"/reviews?"+API_KEY;

    public static String GET_MOVIE_DETAILS = MOVIE_BASE_ADDRESS+FIND_MOVIE+"?"+API_KEY+APPEND_TO_RESPONSE; //need to replace # with movie id, done by Util method

    public static String STANDARD_IMAGE_URLTEMP= IMAGE_BASE_ADDRESS+STANDARD_WIDTH;
    public static String WIDE_IMAGE_URLTEMP= IMAGE_BASE_ADDRESS+WIDE;

    public static String TRAILER_YOUTUBE = "https://youtube.com/watch?v=";
    public static String THUMBNAIL_YOUTUBE = "http://img.youtube.com/vi/#/default.jpg"; //insert video id

}
