package com.example.mkash32.popmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mkash32.popmovies.Activities.MovieDetailsActivity;
import com.example.mkash32.popmovies.Constants;
import com.example.mkash32.popmovies.Fragments.MoviesListFragment;
import com.example.mkash32.popmovies.Movie;
import com.example.mkash32.popmovies.R;
import com.example.mkash32.popmovies.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mkash32 on 24/12/15.
 */
public class RecyclerGridAdapter extends RecyclerView.Adapter<RecyclerGridAdapter.RecyclerViewHolder> {

    private ArrayList<Movie> movies;
    private Context c;
    private MoviesListFragment.OnFragmentInteractionListener mListener;

    public RecyclerGridAdapter(ArrayList<Movie> movies, Context c, MoviesListFragment.OnFragmentInteractionListener mListener) {
        this.movies = movies;
        this.c = c;
        this.mListener = mListener;
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
            Movie movie = movies.get(position);
            holder.getTitle().setText(movie.getTitle());
            holder.setMovie(movie);
            String backDropURL = Constants.STANDARD_IMAGE_URLTEMP+movie.getImagePath();
            Picasso.with(c).load(backDropURL).into(holder.getBackDrop());
    }

    @Override
    public int getItemCount() {
        if(movies!=null)
            return movies.size();
        else
            return 0;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private ImageView backDrop;
        private TextView title;
        private CardView card;
        private Movie movie;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            backDrop = (ImageView) itemView.findViewById(R.id.image_backdrop);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            card = (CardView) itemView.findViewById(R.id.card_view);
            card.setOnClickListener(this);
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

        public void setMovie(Movie movie) {
            this.movie = movie;
        }

        @Override
        public void onClick(View view) {
            if(Utils.isTablet(c))
                mListener.onMovieSelected(movie);       //alert activity that movie is selected
            else {
                Intent intent = new Intent(c, MovieDetailsActivity.class);
                intent.putExtra("id", movie.getId());
                intent.putExtra("title", movie.getTitle());
                intent.putExtra("url", Constants.WIDE_IMAGE_URLTEMP + movie.getPosterPath());
                c.startActivity(intent);
            }
        }
    }
}
