package com.example.popularmovies.ui.detail;

import com.example.popularmovies.data.models.movie.Movie;

/**
 * Created by Victor Holotescu on 19-02-2018.
 */

public interface DetailPresenter {

    Movie getSelectedMovie();

    void loadMovie();

    void loadReviews(int id);

    void loadVideos(int id);

    int getMovieId();

    void setMovieId(int id);

    Boolean getIsFavorite();

    void addToFavorites(byte[] picture);

    void removeFromFavorites();

    void setShowError();

    void setLoadingIndicator();
}
