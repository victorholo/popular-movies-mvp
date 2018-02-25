package com.example.popularmovies.data.network;

import android.util.Log;

import com.example.popularmovies.BuildConfig;
import com.example.popularmovies.data.GetMoviesListener;
import com.example.popularmovies.data.models.movie.Movie;
import com.example.popularmovies.data.models.video.Response;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by Victor Holotescu on 19-02-2018.
 */

public class MoviesNetworkDataSource implements NetworkDataSource{

    private static final String LOG_TAG = MoviesNetworkDataSource.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MoviesNetworkDataSource sInstance;

    public static final String API_KEY_PARAM = "api_key";
    private static final String API_KEY = BuildConfig.API_KEY;

    private static List<Movie> mMovieEntries;

    private final ApiService mApiService;
    private MoviesNetworkDataSource(ApiService apiService) {
        mApiService = apiService;
        mMovieEntries = new ArrayList<>();
    }

    /**
     * Get the singleton for this class
     */
    public static MoviesNetworkDataSource getInstance(ApiService apiService) {
        Log.d(LOG_TAG, "Getting the network data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MoviesNetworkDataSource(apiService);
                Log.d(LOG_TAG, "Made new network data source");
            }
        }
        return sInstance;
    }

    public void getMovies(GetMoviesListener callback, String sortType) {
        mMovieEntries.clear();

        retrofit2.Call<com.example.popularmovies.data.models.movie.Response> call =  mApiService.getMovies(sortType, API_KEY);

        call.enqueue(
                new retrofit2.Callback<com.example.popularmovies.data.models.movie.Response>() {

                    @Override
                    public void onResponse(Call<com.example.popularmovies.data.models.movie.Response> call, retrofit2.Response<com.example.popularmovies.data.models.movie.Response> response) {
                        if (response.isSuccessful()) {

                            com.example.popularmovies.data.models.movie.Response moviesResponse = response.body();
                            mMovieEntries = moviesResponse.getMovies();
                            callback.onSuccess(moviesResponse.getMovies());
                        } else {
                            callback.onFailure(new Throwable());
                        }
                    }

                    @Override
                    public void onFailure(Call<com.example.popularmovies.data.models.movie.Response> call, Throwable t) {
                        callback.onFailure(t);
                    }
                });
    }

    public void getReviews(GetReviewsListener callback, int id) {
        retrofit2.Call<com.example.popularmovies.data.models.review.Response> call =  mApiService.getReviews(id, API_KEY);

        call.enqueue(
                new retrofit2.Callback<com.example.popularmovies.data.models.review.Response>() {
                    @Override
                    public void onResponse(Call<com.example.popularmovies.data.models.review.Response> call, retrofit2.Response<com.example.popularmovies.data.models.review.Response> response) {
                        if (response.isSuccessful()) {

                            com.example.popularmovies.data.models.review.Response reviewsResponse = response.body();
                            callback.onSuccess(reviewsResponse.getReviews());
                        } else {
                            callback.onFailure(new Throwable());
                        }
                    }

                    @Override
                    public void onFailure(Call<com.example.popularmovies.data.models.review.Response> call, Throwable t) {
                        callback.onFailure(t);
                    }
                });
    }

    @Override
    public void getVideos(GetVideosListener callback, int id) {

        retrofit2.Call<com.example.popularmovies.data.models.video.Response> call =
                mApiService.getVideos(id, API_KEY);

        call.enqueue(
                new retrofit2.Callback<com.example.popularmovies.data.models.video.Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                        if (response.isSuccessful()) {

                            com.example.popularmovies.data.models.video.Response videosResponse = response.body();
                            callback.onSuccess(videosResponse.getVideos());
                        } else {
                            callback.onFailure(new Throwable());
                        }
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {
                        callback.onFailure(t);
                    }

                });
    }


}
