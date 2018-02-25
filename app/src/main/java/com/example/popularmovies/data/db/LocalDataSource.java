package com.example.popularmovies.data.db;

import com.example.popularmovies.data.GetMoviesListener;
import com.example.popularmovies.data.models.movie.Movie;

/**
 * Created by Victor Holotescu on 23-02-2018.
 */

public interface LocalDataSource {

    void getFavorites(GetMoviesListener callback);

    Boolean getIsFavourite(int id);

    Boolean addToFavorites(Movie movie);

    Boolean removeFromFavorites(int id);
}
