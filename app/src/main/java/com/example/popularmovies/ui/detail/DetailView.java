package com.example.popularmovies.ui.detail;

import android.support.annotation.Nullable;

import com.example.popularmovies.data.models.movie.Movie;
import com.example.popularmovies.data.models.review.Review;
import com.example.popularmovies.data.models.video.Video;

import java.util.List;

/**
 * Created by Victor Holotescu on 19-02-2018.
 */

interface DetailView {

    void showMovieDetails(Movie movieEntry);

    void showReviews(List<Review> reviews);

    void showVideos(List<Video> videos);

    void showReviewsLoadingIndicator();

    void showReviewsError(String error);

    void hideReviewLoadingIndicator();

    void showNoMovieSelected();

    void showError();

    void showLoadingIndicator();

    void showContentLayout();

    void hideVideoLoadingIndicator();

    void showVideosLoadingIndicator();

    void showVideosError(@Nullable String error);

    void showFavoriteButton(Boolean isFavorite);

    void showFavoriteAddedToast();

    void showFavoriteRemovedToast();

    boolean isActive();
}
