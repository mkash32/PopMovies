package com.example.mkash32.popmovies.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mkash32.popmovies.Movie;
import com.example.mkash32.popmovies.Fragments.MoviesListFragment;
import com.example.mkash32.popmovies.R;

public class MainActivity extends AppCompatActivity implements MoviesListFragment.OnFragmentInteractionListener{

    private MoviesListFragment moviesListFrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        moviesListFrag =(MoviesListFragment) getSupportFragmentManager().findFragmentById(R.id.movie_list_frag);
    }

    @Override
    public boolean  onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this,AboutActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.action_refresh){
            moviesListFrag.onRefresh();
        }
        else if(id == R.id.movieSortPop && moviesListFrag.getDisplaySetting() != 0){
            moviesListFrag.setDisplaySetting(0);
            moviesListFrag.fetchMovies();
        }
        else if(id == R.id.movieSortRating && moviesListFrag.getDisplaySetting() != 1){
            moviesListFrag.setDisplaySetting(1);
            moviesListFrag.fetchMovies();
        }
        else if(id == R.id.favorite && moviesListFrag.getDisplaySetting() != 2){
            moviesListFrag.setDisplaySetting(2);
            moviesListFrag.fetchMovies();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieSelected(Movie movie) {

    }
}
