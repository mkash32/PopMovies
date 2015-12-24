package com.example.mkash32.popmovies;

/**
 * Created by mkash32 on 23/12/15.
 */
public class Constants {

    //public static String API_KEY = "?api_key="; //  API key generated from themoviedb.org --> https://www.themoviedb.org/documentation/api

    public static String POPULAR_MOVIES = "/movie/popular";
    public static String MOVIE_BASE_ADDRESS = "https://api.themoviedb.org/3";

    public static String IMAGE_BASE_ADDRESS = "http://image.tmdb.org/t/p";
    public static String STANDARD_WIDTH = "/w185";

    public static String GET_POP_MOVIES_URL = MOVIE_BASE_ADDRESS+POPULAR_MOVIES+API_KEY;

    public static String STANDARD_IMAGE_URLTEMP= IMAGE_BASE_ADDRESS+STANDARD_WIDTH;

}
