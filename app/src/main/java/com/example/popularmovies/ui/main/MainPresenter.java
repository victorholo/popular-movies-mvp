package com.example.popularmovies.ui.main;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.example.popularmovies.data.models.movie.Movie;

import java.util.List;

/**
 * Created by Victor Holotescu on 19-02-2018.
 */

public interface MainPresenter {

    void loadMovies(@Nullable String type);

    void setLoadingIndicator();

    void setErrorMessage();

    void updateGrid(List<Movie> movies);

    void updateDetail(int movieId);

    String getFiltering();

    void setFiltering(String filter);
}
