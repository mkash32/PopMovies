package com.example.mkash32.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by mkash32 on 24/12/15.
 */
public class RecyclerGridAdapter extends RecyclerView.Adapter<RecyclerGridAdapter.RecyclerViewHolder> {

    private ArrayList<Movie> movies;
    private Context c;

    public RecyclerGridAdapter(ArrayList<Movie> movies, Context c) {
        this.movies = movies;
        this.c = c;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(c).inflate(R.layout.movie_grid_element,parent,false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
            holder.getTitle().setText(movies.get(position).getTitle());
            String backDropURL = Constants.STANDARD_IMAGE_URLTEMP+movies.get(position).getImagePath();
            Picasso.with(c).load(backDropURL).into(holder.getBackDrop());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView backDrop;
        private TextView title;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            backDrop = (ImageView) itemView.findViewById(R.id.image_backdrop);
            title = (TextView) itemView.findViewById(R.id.tv_title);
        }

        public ImageView getBackDrop() {
            return backDrop;
        }

        public void setBackDrop(ImageView backDrop) {
            this.backDrop = backDrop;
        }

        public TextView getTitle() {
            return title;
        }

        public void setTitle(TextView title) {
            this.title = title;
        }
    }
}
