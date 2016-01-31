package com.example.mkash32.popmovies.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.mkash32.popmovies.Movie;

import java.util.ArrayList;

/**
 * Created by mkash32 on 31/1/16.
 */
public class MovieDetailsTabAdapter extends BaseAdapter {

    private Movie movie;
    private Context c;
    private  int numberOfTrailers;
    private ArrayList<String> trailers;

    public MovieDetailsTabAdapter(Movie movie, Context c) {
        this.movie = movie;
        this.numberOfTrailers = movie.getTrailers().size();
        this.trailers = movie.getTrailers();
        this.c = c;
    }

    @Override
    public int getCount() {
        return 4+numberOfTrailers;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
