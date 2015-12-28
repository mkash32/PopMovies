package com.example.mkash32.popmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mkash32.popmovies.Activities.MovieDetailsActivity;
import com.example.mkash32.popmovies.Constants;
import com.example.mkash32.popmovies.Movie;
import com.example.mkash32.popmovies.R;
import com.example.mkash32.popmovies.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mkash32 on 27/12/15.
 */
public class MovieDetailsAdapter extends RecyclerView.Adapter<MovieDetailsAdapter.RecyclerViewHolder> {

    private Movie movie;
    private Context c;

    public MovieDetailsAdapter(Movie movie, Context c) {
        this.movie = movie;
        this.c = c;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(c).inflate(R.layout.movie_description,parent,false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        TextView title = holder.getTitle();
        TextView content = holder.getContent();
        switch (position){
            case 0: title.setText("Overview");
                    content.setText(movie.getOverview());
                    break;
            case 1: title.setText("Details");
                    content.setText(Utils.formatText(movie));
                    content.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    break;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        private TextView title,content;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.li_title);
            content = (TextView) itemView.findViewById(R.id.li_content);
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getContent() {
            return content;
        }
    }
}

