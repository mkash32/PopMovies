package com.example.mkash32.popmovies;

import java.util.ArrayList;

/**
 * Created by mkash32 on 24/12/15.
 */
public class Movie {

    private String id,title,imagePath,releaseDate;  //Simple Movie data
    private String posterPath, overview;
    private double popularity,vote_avg;
    private int runtime;
    private ArrayList<String> trailers = new ArrayList<String>();     //Strings will be of the form name;key, seperation by semicolons

    public Movie() {

    }

    public Movie(String id, String title, String imagePath, String posterPath, String releaseDate, double popularity) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.posterPath = posterPath;
    }

    public Movie(String id, String title, String imagePath, String releaseDate, String posterPath, String overview, double popularity,double vote_avg, int runtime) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.overview = "\t"+overview;
        this.popularity = popularity;
        this.vote_avg =vote_avg;
        this.runtime = runtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getVote_avg() {
        return vote_avg;
    }

    public void setVote_avg(double vote_avg) {
        this.vote_avg = vote_avg;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public ArrayList<String> getTrailers() {
        return trailers;
    }

    public void setTrailers(ArrayList<String> trailers) {
        this.trailers = trailers;
    }
}
