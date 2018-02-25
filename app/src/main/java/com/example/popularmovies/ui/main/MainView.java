package com.example.popularmovies.ui.main;

import android.os.Parcelable;
import android.widget.ImageView;

import com.example.popularmovies.data.models.movie.Movie;

import java.util.List;

/**
 * Created by Victor Holotescu on 19-02-2018.
 */

interface MainView {
    void showLoading();

    void showGrid(List<Movie> moviePosters, Parcelable gridPosition);

    void showError();

    void showDetailView(int movieId);

    boolean isActive();

    String getStringResource(int resId);

}
