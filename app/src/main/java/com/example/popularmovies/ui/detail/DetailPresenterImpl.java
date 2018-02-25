package com.example.popularmovies.ui.detail;

import com.example.popularmovies.data.DataRepository;
import com.example.popularmovies.data.models.movie.Movie;
import com.example.popularmovies.data.models.review.Review;
import com.example.popularmovies.data.models.video.Video;
import com.example.popularmovies.data.network.NetworkDataSource;

import java.util.List;

/**
 * Created by Victor Holotescu on 20-02-2018.
 */

public class DetailPresenterImpl implements DetailPresenter {

    private final DetailView mDetailView;
    private final DataRepository mDataRepository;

    private int mMovieId;

    public DetailPresenterImpl(int movieId, DataRepository dataRepository, DetailView detailView) {
        mMovieId = movieId;
        mDetailView = detailView;
        mDataRepository = dataRepository;
    }


    @Override
    public Movie getSelectedMovie() {
        if(mMovieId != -1) {
            Movie movieEntry = mDataRepository.getMovie(mMovieId);
            return  movieEntry;
        }
        return null;
    }

    @Override
    public void loadMovie() {
        if(mDetailView.isActive()) mDetailView.showLoadingIndicator();
        Movie movie = getSelectedMovie();
        if(movie != null) {
            mDetailView.showMovieDetails(movie);
            mDetailView.showFavoriteButton(getIsFavorite());
            mDetailView.showContentLayout();
            loadReviews(mMovieId);
            loadVideos(mMovieId);
        }else mDetailView.showError();
    }

    @Override
    public void loadReviews(int id) {
        if(mDetailView != null) mDetailView.showReviewsLoadingIndicator();

        mDataRepository.getReviewsById(new NetworkDataSource.GetReviewsListener() {
            @Override
            public void onSuccess(List<Review> reviews) {
                if(mDetailView != null) {
                    mDetailView.hideReviewLoadingIndicator();
                    mDetailView.showReviews(reviews);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                if(mDetailView != null) mDetailView.showReviewsError(null);
            }

            @Override
            public void onNetworkFailure() {
                if(mDetailView != null) mDetailView.showReviewsError(null);
            }
        }, id);
    }

    @Override
    public void loadVideos(int id) {
        if(mDetailView != null) mDetailView.showVideosLoadingIndicator();

        mDataRepository.getVideosById(new NetworkDataSource.GetVideosListener() {
            @Override
            public void onSuccess(List<Video> videos) {
                if(mDetailView != null) {
                    mDetailView.hideVideoLoadingIndicator();
                    mDetailView.showVideos(videos);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                if(mDetailView != null) mDetailView.showVideosError(null);


            }

            @Override
            public void onNetworkFailure() {
                if(mDetailView != null) mDetailView.showVideosError(null);

            }
        }, id);
    }

    @Override
    public int getMovieId() {
        return mMovieId;
    }

    @Override
    public void setMovieId(int id) {
        mMovieId = id;
    }

    @Override
    public Boolean getIsFavorite() {
        return mDataRepository.getIsFavorite(mMovieId);
    }

    @Override
    public void addToFavorites(byte[] picture) {
        Movie movie = getSelectedMovie();
        movie.setCachedImage(picture);
        Boolean inserted = mDataRepository.insertFavorite(movie);
        if(inserted){
            mDetailView.showFavoriteButton(true);
            mDetailView.showFavoriteAddedToast();
        }
    }

    @Override
    public void removeFromFavorites() {
        Boolean isRemoved = mDataRepository.deleteFavorite(mMovieId);
        if(isRemoved){
            mDetailView.showFavoriteButton(false);
            mDetailView.showFavoriteRemovedToast();
        }
    }

    @Override
    public void setShowError() {
        if(mDetailView.isActive()){
            mDetailView.showError();
        }
    }

    @Override
    public void setLoadingIndicator() {
        if(mDetailView.isActive()){
            mDetailView.showLoadingIndicator();
        }
    }

}
