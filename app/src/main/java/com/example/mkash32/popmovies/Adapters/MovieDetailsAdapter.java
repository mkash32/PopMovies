package com.example.mkash32.popmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mkash32.popmovies.Constants;
import com.example.mkash32.popmovies.Movie;
import com.example.mkash32.popmovies.R;
import com.example.mkash32.popmovies.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mkash32 on 27/12/15.
 */

//Complexity of the recycler view item and adapter has increased due to ineffective use. The recycler view isn't the best fit for this situation
//but for parallax scrolling, recycler view is required. Better implementation resulting in same features is yet to be determined.

public class MovieDetailsAdapter extends RecyclerView.Adapter<MovieDetailsAdapter.RecyclerViewHolder> {

    private Movie movie;
    private Context c;
    private int numberOfTrailers;
    private ArrayList<String> trailers;

    public MovieDetailsAdapter(Movie movie, Context c) {
        this.movie = movie;
        this.c = c;
        this.numberOfTrailers = movie.getTrailers().size();
        this.trailers = movie.getTrailers();
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        this.trailers = movie.getTrailers();
        numberOfTrailers = trailers.size();
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
                    descriptionConfiguration(holder);
                    break;
            case 1: title.setText("Details");
                    content.setText(Utils.formatText(movie));
                    content.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                    descriptionConfiguration(holder);
                    break;
            default: setTrailers(holder,position);
        }
    }

    public void setTrailers(RecyclerViewHolder holder, int position) {
        if (position == 2)
        {
            holder.getTitle().setText("Trailers");

            holder.getTitleCard().setVisibility(View.VISIBLE);
            holder.getTrailerCard().setVisibility(View.GONE);

            if(numberOfTrailers == 0)
                holder.content.setText("No trailers Available");
            else
                holder.getContentCard().setVisibility(View.GONE);
        }
        else if(position <= 2 + numberOfTrailers)
        {
            int i = position - 3;
                String trailer = trailers.get(i);
                final String[] info = trailer.split(";");

                trailerConfiguration(holder);

                holder.getTrailer().setText(info[0]);

                Picasso.with(c).load(Utils.getVideoThumbnail(info[1])).into(holder.getVideoThumbnail());


            holder.getTrailerCard().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.TRAILER_YOUTUBE + info[1])));
                }
            });
        }
    }

    //since recycler view is being used, only one type of view is being inflated and recycled even for different forms of information,
    //therefore I made the recycler item into components, and hide/show the components based on the type of data to be displayed
    //recycler view is a necessity for parallax scrolling, that is why recycler view is being used even though the complexity of the list items and adapter is increasing


    //this is configuration is for descriptive items
    private void descriptionConfiguration(RecyclerViewHolder holder)
    {
        holder.getTitleCard().setVisibility(View.VISIBLE);
        holder.getContentCard().setVisibility(View.VISIBLE);

        holder.getTrailerCard().setVisibility(View.GONE);

    }

    //this configuration is for trailer items
    private void trailerConfiguration(RecyclerViewHolder holder)
    {
        holder.getTitleCard().setVisibility(View.GONE);
        holder.getContentCard().setVisibility(View.GONE);

        holder.getTrailerCard().setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return 3 + numberOfTrailers;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        private TextView title,content,trailer;
        private CardView titleCard,contentCard,trailerCard;
        private ImageView videoThumbnail;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.li_title);
            content = (TextView) itemView.findViewById(R.id.li_content);
            titleCard = (CardView) itemView.findViewById(R.id.title_card);
            contentCard = (CardView) itemView.findViewById(R.id.content_card);
            trailerCard = (CardView) itemView.findViewById(R.id.trailer_card);
            trailer = (TextView) itemView.findViewById(R.id.li_trailer);
            videoThumbnail = (ImageView) itemView.findViewById(R.id.video_thumbnail);
        }

        public TextView getTitle() {
            return title;
        }

        public TextView getContent() {
            return content;
        }

        public TextView getTrailer() {
            return trailer;
        }

        public CardView getTitleCard() {
            return titleCard;
        }

        public CardView getContentCard() {
            return contentCard;
        }

        public CardView getTrailerCard() {
            return trailerCard;
        }

        public ImageView getVideoThumbnail() {
            return videoThumbnail;
        }
    }
}

