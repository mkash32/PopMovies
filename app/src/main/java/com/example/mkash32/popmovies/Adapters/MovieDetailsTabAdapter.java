package com.example.mkash32.popmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mkash32.popmovies.Constants;
import com.example.mkash32.popmovies.Movie;
import com.example.mkash32.popmovies.R;
import com.example.mkash32.popmovies.Utils;
import com.squareup.picasso.Picasso;

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
        Log.d("AAKASH", "Number of trailers " + numberOfTrailers);
        this.trailers = movie.getTrailers();
        this.c = c;
    }

    @Override
    public int getCount() {
        return 5+numberOfTrailers;
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
        View v = null;
        switch (i){
            case 0: //image
                v = LayoutInflater.from(c).inflate(R.layout.poster,viewGroup,false);
                ImageView poster = (ImageView)v.findViewById(R.id.image_poster);
                Picasso.with(c).load(Constants.WIDE_IMAGE_URLTEMP + movie.getPosterPath()).into(poster);
                break;

            case 1: //Overview
                v = LayoutInflater.from(c).inflate(R.layout.content_card,viewGroup,false);
                TextView title = (TextView) v.findViewById(R.id.tv_title);
                TextView overview = (TextView) v.findViewById(R.id.tv_content);

                title.setText("Overview");
                overview.setText(movie.getOverview());
                break;

            case 2: //Description
                v = LayoutInflater.from(c).inflate(R.layout.content_card,viewGroup,false);
                TextView description_title = (TextView) v.findViewById(R.id.tv_title);
                TextView description = (TextView) v.findViewById(R.id.tv_content);

                description_title.setText("Details");
                description.setText(Utils.formatText(movie));
                break;

            case 3: //Trailer title
                v = LayoutInflater.from(c).inflate(R.layout.title_card,viewGroup,false);
                TextView trailerTitle = (TextView) v.findViewById(R.id.tv_title);
                trailerTitle.setText("Trailers");
                break;
            default:
                v = inflateView(i,viewGroup);
        }
        return v;
    }

    private View inflateView(int position, ViewGroup viewGroup)
    {
        View v = null;
        if(position <= 3+numberOfTrailers){
            v = LayoutInflater.from(c).inflate(R.layout.trailer_card,viewGroup,false);
            TextView trailerTitle = (TextView) v.findViewById(R.id.tv_trailer);
            ImageView thumbnail = (ImageView) v.findViewById(R.id.video_thumbnail);
            CardView trailerCard = (CardView) v.findViewById(R.id.trailer_card);

            int i = position - 3;
            String trailer = trailers.get(i);
            final String[] info = trailer.split(";");

            trailerTitle.setText(info[0]);

            Picasso.with(c).load(Utils.getVideoThumbnail(info[1])).into(thumbnail);


            trailerCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    c.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.TRAILER_YOUTUBE + info[1])));
                }
            });
        }
        else{   //reviews
            v = LayoutInflater.from(c).inflate(R.layout.trailer_card,viewGroup,false);//change later
        }

        return v;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}
