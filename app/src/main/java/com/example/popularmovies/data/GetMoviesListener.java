package com.example.popularmovies.data;

import com.example.popularmovies.data.models.movie.Movie;

import java.util.List;

/**
 * Created by Victor Holotescu on 23-02-2018.
 */

public interface GetMoviesListener {
    void onSuccess(List<Movie> movies);

    void onFailure(Throwable throwable);

    void onNetworkFailure();
}