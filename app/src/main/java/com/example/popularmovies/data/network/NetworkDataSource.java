package com.example.popularmovies.data.network;

import com.example.popularmovies.data.GetMoviesListener;
import com.example.popularmovies.data.models.review.Review;
import com.example.popularmovies.data.models.video.Video;

import java.util.List;

/**
 * Created by Victor Holotescu on 22-02-2018.
 */

public interface NetworkDataSource {

    void getMovies(GetMoviesListener callback, String sortType);

    void getReviews(GetReviewsListener callback, int id);

    void getVideos(GetVideosListener callback, int id);

    interface GetVideosListener {
        void onSuccess(List<Video> movies);

        void onFailure(Throwable throwable);

        void onNetworkFailure();

    }

    interface GetReviewsListener {
        void onSuccess(List<Review> movies);

        void onFailure(Throwable throwable);

        void onNetworkFailure();

    }
}
