package com.example.popularmovies.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.popularmovies.MovieMvpController;
import com.example.popularmovies.R;
import com.example.popularmovies.utilities.MovieFilteringType;

/**
 * Created by Victor Holotescu on 23-02-2018.
 */

public class MainActivity extends AppCompatActivity {

    private static final String CURRENT_MOVIE_ID_KEY = "current-movie-id-key";
    private static final String CURRENT_SORT_FILTER_KEY = "current-sort-filter-key";
    private MovieMvpController mvpTabletController;

    private SharedPreferences mPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String filter = mPrefs.getString(CURRENT_SORT_FILTER_KEY, MovieFilteringType.TOP_RATED);

        int movieId = -1;
        // Load previously saved state, if available.
        if (savedInstanceState != null) {
            movieId = savedInstanceState.getInt(CURRENT_MOVIE_ID_KEY);
        }

        mvpTabletController = MovieMvpController.createMoviesView(this, movieId, filter);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_MOVIE_ID_KEY, mvpTabletController.getMovieId());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_sort_popular:
                mvpTabletController.loadMovies(MovieFilteringType.POPULAR);
                saveSortType(MovieFilteringType.POPULAR);
                break;
            case R.id.menu_sort_top_rated:
                mvpTabletController.loadMovies(MovieFilteringType.TOP_RATED);
                saveSortType(MovieFilteringType.TOP_RATED);
                break;
            case R.id.menu_sort_favorites:
                mvpTabletController.loadMovies(MovieFilteringType.FAVORITE);
                saveSortType(MovieFilteringType.FAVORITE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveSortType(String sortType){
        SharedPreferences.Editor e = mPrefs.edit();
        e.putString(CURRENT_SORT_FILTER_KEY, sortType);
        e.apply();
    }
}
