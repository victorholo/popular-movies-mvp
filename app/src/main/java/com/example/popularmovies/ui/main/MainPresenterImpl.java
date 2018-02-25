package com.example.popularmovies.ui.main;

import android.os.Parcelable;
import android.widget.ImageView;

import com.example.popularmovies.data.GetMoviesListener;
import com.example.popularmovies.data.DataRepository;
import com.example.popularmovies.data.models.movie.Movie;
import com.example.popularmovies.utilities.MovieFilteringType;

import java.util.List;

/**
 * Created by Victor Holotescu on 19-02-2018.
 */

public class MainPresenterImpl implements MainPresenter, GetMoviesListener {

    private final MainView mMainView;
    private final DataRepository mDataRepository;

    private String mFilter = MovieFilteringType.TOP_RATED;

    private Parcelable mRecyclerPosition = null;
    public MainPresenterImpl(DataRepository dataRepository, MainView mainView) {
        mMainView = mainView;
        mDataRepository = dataRepository;
    }

    @Override
    public void loadMovies(String type) {
        setLoadingIndicator();

        if(type != null && type.equals(mFilter)){
            mDataRepository.getCachedMovies(this);
        }else{
            if(type == null) type = mFilter;

            if(type.equals(MovieFilteringType.FAVORITE)){
                mDataRepository.getMoviesFromDatabase(this);
            }else mDataRepository.getMoviesFromNetwork(this, type);
        }
        mFilter = type;
    }

    @Override
    public void setLoadingIndicator() {
        if (mMainView != null) mMainView.showLoading();
    }

    @Override
    public void setErrorMessage() {
        if (mMainView != null) mMainView.showError();
    }

    @Override
    public void updateGrid(List<Movie> movies) {
        if (mMainView != null) mMainView.showGrid(movies, mRecyclerPosition);
        mRecyclerPosition = null;
    }

    @Override
    public void updateDetail(int movieId) {
        if (mMainView != null) {
            mMainView.showDetailView(movieId);
        }
    }

    @Override
    public String getFiltering() {
        return mFilter;
    }

    @Override
    public void setFiltering(String filter) {
        mFilter = filter;
    }

    @Override
    public void onSuccess(List<Movie> movies) {
        updateGrid(movies);
    }

    @Override
    public void onFailure(Throwable throwable) {
        setErrorMessage();
    }

    @Override
    public void onNetworkFailure() {
        setErrorMessage();
    }
}
