package com.example.popularmovies.data;

import android.util.Log;

import com.example.popularmovies.data.db.LocalDataSource;
import com.example.popularmovies.data.models.movie.Movie;
import com.example.popularmovies.data.network.NetworkDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Holotescu on 19-02-2018.
 */

public class DataRepository {
    private static final String LOG_TAG = DataRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static DataRepository sInstance;

    private final NetworkDataSource mMoviesNetworkDataSource;
    private final LocalDataSource mLocalDataSource;
    private List<Movie> mCachedMovies;

    private DataRepository(NetworkDataSource networkDataSource, LocalDataSource localDataSource) {
        mMoviesNetworkDataSource = networkDataSource;
        mLocalDataSource = localDataSource;
        mCachedMovies = new ArrayList<>();
    }

    public synchronized static DataRepository getInstance(NetworkDataSource moviesNetworkDataSource, LocalDataSource localDataSource) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new DataRepository(moviesNetworkDataSource, localDataSource);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    public Movie getMovie(int id) {
        if (mCachedMovies != null) {
            for (Movie movie : mCachedMovies) {
                if (movie.getId() == id) return movie;
            }
        }
        return null;
    }


    public void getMoviesFromNetwork(GetMoviesListener callback, String sortType) {
        mMoviesNetworkDataSource.getMovies(new GetMoviesListener() {
            @Override
            public void onSuccess(List<Movie> movies) {
                mCachedMovies = movies;
                callback.onSuccess(movies);
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onNetworkFailure() {
                callback.onNetworkFailure();
            }
        }, sortType);
    }

    public void getCachedMovies(GetMoviesListener callback) {
       callback.onSuccess(mCachedMovies);
    }

    public void getMoviesFromDatabase(GetMoviesListener callback) {
        mLocalDataSource.getFavorites(new GetMoviesListener() {
            @Override
            public void onSuccess(List<Movie> movies) {
                mCachedMovies = movies;
                callback.onSuccess(movies);
            }

            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onNetworkFailure() {
                callback.onNetworkFailure();
            }
        });
    }

    public Boolean getIsFavorite(int id){
        return mLocalDataSource.getIsFavourite(id);
    }

    public boolean deleteFavorite(int id){
        return mLocalDataSource.removeFromFavorites(id);
    }

    public boolean insertFavorite(Movie movie){
        return mLocalDataSource.addToFavorites(movie);
    }

    public void getReviewsById(NetworkDataSource.GetReviewsListener callback, int id) {
        mMoviesNetworkDataSource.getReviews(callback, id);
    }

    public void getVideosById(NetworkDataSource.GetVideosListener callback, int id) {
        mMoviesNetworkDataSource.getVideos(callback, id);
    }
}
