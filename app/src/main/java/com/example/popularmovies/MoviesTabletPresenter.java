package com.example.popularmovies;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.example.popularmovies.data.DataRepository;
import com.example.popularmovies.data.GetMoviesListener;
import com.example.popularmovies.data.models.movie.Movie;
import com.example.popularmovies.ui.detail.DetailPresenter;
import com.example.popularmovies.ui.main.MainPresenter;
import com.example.popularmovies.utilities.MovieFilteringType;

import java.util.List;

/**
 * Created by Victor Holotescu on 23-02-2018.
 */

public class MoviesTabletPresenter implements MainPresenter, DetailPresenter, GetMoviesListener {

    private final MainPresenter mMainPresenter;
    private DetailPresenter mDetailPresenter;
    private final DataRepository mDataRepository;

    private Boolean mFirstLoad = true;

    public MoviesTabletPresenter(DataRepository dataRepository, MainPresenter mainPresenter) {
        mMainPresenter = mainPresenter;
        mDataRepository = dataRepository;
    }

    public void setDetailPresenter(DetailPresenter detailPresenter) {
        mDetailPresenter = detailPresenter;
    }

    @Override
    public Movie getSelectedMovie() {
        return mDetailPresenter.getSelectedMovie();
    }

    @Override
    public void loadMovie() {
        mDetailPresenter.loadMovie();
    }

    @Override
    public void loadReviews(int id) {
        mDetailPresenter.loadReviews(id);
    }

    @Override
    public void loadVideos(int id) {
        mDetailPresenter.loadVideos(id);
    }

    @Override
    public void loadMovies(@Nullable String type) {
        mMainPresenter.setLoadingIndicator();
        if(type == null) type = mMainPresenter.getFiltering();
        if (type.equals(mMainPresenter.getFiltering()) && (!mFirstLoad || mDetailPresenter.getMovieId() != -1)) {
           // String finalType = type;
            mDataRepository.getCachedMovies(new GetMoviesListener() {
                @Override
                public void onSuccess(List<Movie> movies) {
                    updateGrid(movies);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    mMainPresenter.setErrorMessage();
                    setShowError();
                }

                @Override
                public void onNetworkFailure() {
                    mMainPresenter.setErrorMessage();
                    setShowError();
                }
            });
        } else {
            if (type.equals(MovieFilteringType.FAVORITE)) {
                mDataRepository.getMoviesFromDatabase(this);
            } else mDataRepository.getMoviesFromNetwork(this, type);

        }

        mMainPresenter.setFiltering(type);
    }

    @Override
    public void setLoadingIndicator() {
        mMainPresenter.setLoadingIndicator();
    }

    @Override
    public void setErrorMessage() {
        mMainPresenter.setErrorMessage();
    }

    @Override
    public void updateGrid(List<Movie> movies) {
        mMainPresenter.updateGrid(movies);
    }

    @Override
    public void updateDetail(int movieId) {
        int oldMovieDetail = getMovieId();

        if (mDetailPresenter != null && oldMovieDetail != movieId) {
            setMovieId(movieId);
            loadMovie();
        }

    }

    @Override
    public String getFiltering() {
        return mMainPresenter.getFiltering();
    }

    @Override
    public void setFiltering(String filter) {
        mMainPresenter.setFiltering(filter);
    }

    @Override
    public int getMovieId() {
        if (mDetailPresenter != null) return mDetailPresenter.getMovieId();
        return -1;
    }

    @Override
    public void setMovieId(int id) {
        mDetailPresenter.setMovieId(id);
    }

    @Override
    public Boolean getIsFavorite() {
        return mDetailPresenter.getIsFavorite();
    }

    @Override
    public void addToFavorites(byte[] picture) {
        mDetailPresenter.addToFavorites(picture);
    }

    @Override
    public void removeFromFavorites() {
        mDetailPresenter.removeFromFavorites();
    }

    @Override
    public void setShowError() {
        mDetailPresenter.setShowError();
    }

    @Override
    public void onSuccess(List<Movie> movies) {
        updateGrid(movies);
        updateDetail(movies.get(0).getId());
        mFirstLoad = false;
    }

    @Override
    public void onFailure(Throwable throwable) {
        mMainPresenter.setErrorMessage();
        setShowError();
    }

    @Override
    public void onNetworkFailure() {
        mMainPresenter.setErrorMessage();
        setShowError();
    }
}
