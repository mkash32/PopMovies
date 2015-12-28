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

    public static String IMAGE_BASE_ADDRESS = "http://image.tmdb.org/t/p";
    public static String STANDARD_WIDTH = "/w185";
    public static String WIDE = "/w342";


    //Final URL tempelates
    public static String GET_MOVIES_POP_URL = MOVIE_BASE_ADDRESS+POPULAR_MOVIES+SORT_BY_POPULARITY+"&"+API_KEY;
    public static String GET_MOVIES_RATED_URL = MOVIE_BASE_ADDRESS+POPULAR_MOVIES+SORT_BY_HIGHEST_RATED+"&"+API_KEY;

    public static String GET_MOVIE_DETAILS = MOVIE_BASE_ADDRESS+FIND_MOVIE+"?"+API_KEY; //need to replace # with movie id, done by Util method

    public static String STANDARD_IMAGE_URLTEMP= IMAGE_BASE_ADDRESS+STANDARD_WIDTH;
    public static String WIDE_IMAGE_URLTEMP= IMAGE_BASE_ADDRESS+WIDE;

}
