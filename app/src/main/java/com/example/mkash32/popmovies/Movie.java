package com.example.mkash32.popmovies;

/**
 * Created by mkash32 on 24/12/15.
 */
public class Movie {

    private String id,title,imagePath,releaseDate;  //Simple Movie data
    private String posterPath, overview;
    private double popularity;

    public Movie(String id, String title, String imagePath,String releaseDate, double popularity) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
    }

    public Movie(String id, String title, String imagePath, String releaseDate, String posterPath, String overview, double popularity) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.overview = overview;
        this.popularity = popularity;
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
}
