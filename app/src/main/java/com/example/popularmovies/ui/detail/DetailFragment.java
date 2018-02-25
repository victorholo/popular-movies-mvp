package com.example.popularmovies.ui.detail;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.popularmovies.R;
import com.example.popularmovies.data.models.movie.Movie;
import com.example.popularmovies.data.models.review.Review;
import com.example.popularmovies.data.models.video.Video;
import com.example.popularmovies.utilities.MoviePosterUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DetailFragment extends Fragment implements DetailView, View.OnClickListener, VideosRecyclerAdapter.VideosAdapterOnClickHandler {

    @BindView(R.id.yearTextView)
    TextView mYearTextView;

    @BindView(R.id.averageTextView)
    TextView mAverageTextView;

    @BindView(R.id.plotTextView)
    TextView mPlotTextView;

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;

    @BindView(R.id.messageTextView)
    TextView mMessageTextView;

    @BindView(R.id.reviewErrorTextView)
    TextView mReviewsErrorTextView;

    @BindView(R.id.videoErrorTextView)
    TextView mVideosErrorTextView;

    @BindView(R.id.posterImageView)
    ImageView mPosterImage;

    @BindView(R.id.reviewsLoadingIndicator)
    ProgressBar mReviewsLoadingIndicator;

    @BindView(R.id.videosLoadingIndicator)
    ProgressBar mVideosLoadingIndicator;

    @BindView(R.id.mainLoadingIndicator)
    ProgressBar mMainLoadingIndicator;

    @BindView(R.id.contentLayout)
    ConstraintLayout mContentLayout;

    @BindView(R.id.favoriteButton)
    ToggleButton mFavoriteButton;

    @BindView(R.id.videoRecyclerView)
    RecyclerView mVideoRecyclerView;

    private DetailPresenter mDetailPresenter;

    private Unbinder mUnbinder;

    private VideosRecyclerAdapter mVideosAdapter;

    public DetailFragment() {
        // Requires empty public constructor
    }

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    private static final String CURRENT_VIDEOS_LAYOUT_POSITION_KEY = "current-videos-layout-position-key";

    private Parcelable mCurrentRecyclerSavedState = null;

    private LinearLayoutManager mLinearLayoutManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        mUnbinder = ButterKnife.bind(this, root);

        mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mVideoRecyclerView.setLayoutManager(mLinearLayoutManager);
        mVideoRecyclerView.setHasFixedSize(true);

        mVideosAdapter = new VideosRecyclerAdapter(getActivity(), this);
        mVideoRecyclerView.setAdapter(mVideosAdapter);

        mFavoriteButton.setOnClickListener(this);
        mDetailPresenter.loadMovie();

        return root;
    }

    public void setPresenter(DetailPresenter detailPresenter) {
        mDetailPresenter = detailPresenter;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            mCurrentRecyclerSavedState = savedInstanceState.getParcelable(CURRENT_VIDEOS_LAYOUT_POSITION_KEY);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CURRENT_VIDEOS_LAYOUT_POSITION_KEY, mLinearLayoutManager.onSaveInstanceState());
    }

    @Override
    public void showMovieDetails(Movie movieEntry) {

        String title = movieEntry.getOriginalTitle();
        if (title != null) {
            mTitleTextView.setText(title);
        }

        String releaseDate = movieEntry.getReleaseDate();
        if (releaseDate != null) {
            mYearTextView.setText(movieEntry.getReleaseDate());
        }

        double average = movieEntry.getVoteAverage();
        if (average != 0) {
            mAverageTextView.setText(String.valueOf(average));
        }

        String overview = movieEntry.getOverview();
        if (overview != null) {
            mPlotTextView.setText(movieEntry.getOverview());
        }

        byte[] cachedImage = movieEntry.getCachedImage();
        if (cachedImage != null) {
            Bitmap posterBitmap = MoviePosterUtils.getBitmapFromByteArray(cachedImage);
            mPosterImage.setImageBitmap(posterBitmap);

        } else {
            String posterPath = movieEntry.getPosterPath();
            if (posterPath != null) {
                String urlString = MoviePosterUtils.getBigMoviePosterUrlString(movieEntry.getPosterPath());
                Picasso.with(getActivity()).load(urlString).into(mPosterImage);
            }
        }
    }


    //With the help of https://stackoverflow.com/a/12439378/2768211
    private void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            //try to play in youtube app
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            //fallback to web
            startActivity(webIntent);
        }
    }

    @Override
    public void showReviews(List<Review> reviews) {

        LinearLayout reviewItemsLayout = getActivity().findViewById(R.id.reviewItemsLayout);
        reviewItemsLayout.removeAllViews();


        if (reviews != null && reviews.size() > 0) {
            for (Review review : reviews) {
                View reviewItemLayout = LayoutInflater.from(getActivity()).inflate(R.layout.review_item, null, false);

                String content = review.getContent();
                if (content != null) {
                    TextView bodyTextView = reviewItemLayout.findViewById(R.id.bodyTextView);
                    bodyTextView.setText(content);
                }

                String author = review.getAuthor();
                if (author != null) {
                    TextView authorTextView = reviewItemLayout.findViewById(R.id.authorTextView);
                    authorTextView.setText(getString(R.string.review_author_base, author));
                }
                reviewItemsLayout.addView(reviewItemLayout);
            }
        } else showReviewsError(getResources().getString(R.string.no_reviews_label));
    }

    @Override
    public void showVideos(List<Video> videos) {
        if (videos != null && videos.size() > 0) {
            mVideoRecyclerView.setVisibility(View.VISIBLE);
            mVideosAdapter.swapVideos(videos);
            if(mCurrentRecyclerSavedState != null){
                mLinearLayoutManager.onRestoreInstanceState(mCurrentRecyclerSavedState);
            }
        } else {
            showVideosError(getResources().getString(R.string.no_videos_label));
        }
    }

    @Override
    public void showReviewsLoadingIndicator() {
        mReviewsLoadingIndicator.setVisibility(View.VISIBLE);
        mReviewsErrorTextView.setVisibility(View.GONE);
    }

    @Override
    public void showReviewsError(@Nullable String error) {
        mReviewsLoadingIndicator.setVisibility(View.GONE);
        mReviewsErrorTextView.setVisibility(View.VISIBLE);
        if (error == null) mReviewsErrorTextView.setText(R.string.review_loading_error);
        else mReviewsErrorTextView.setText(error);
    }

    @Override
    public void hideReviewLoadingIndicator() {
        mReviewsLoadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void showNoMovieSelected() {
        mContentLayout.setVisibility(View.GONE);
        mMessageTextView.setVisibility(View.VISIBLE);
        mMessageTextView.setText(getResources().getString(R.string.no_movie_selected_label));
        mMainLoadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        mContentLayout.setVisibility(View.GONE);
        mMessageTextView.setVisibility(View.VISIBLE);
        mMessageTextView.setText(getResources().getString(R.string.an_error_has_occured));
        mMainLoadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingIndicator() {
        mContentLayout.setVisibility(View.GONE);
        mMessageTextView.setVisibility(View.GONE);
        mMainLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideVideoLoadingIndicator() {
        mVideosLoadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void showVideosLoadingIndicator() {
        mVideoRecyclerView.setVisibility(View.GONE);
        mVideosLoadingIndicator.setVisibility(View.VISIBLE);
        mVideosErrorTextView.setVisibility(View.GONE);
    }

    @Override
    public void showVideosError(@Nullable String error) {
        mVideoRecyclerView.setVisibility(View.GONE);
        mVideosLoadingIndicator.setVisibility(View.GONE);
        mVideosErrorTextView.setVisibility(View.VISIBLE);
        if (error == null) mVideosErrorTextView.setText(R.string.videos_loading_error);
        else mVideosErrorTextView.setText(error);
    }

    @Override
    public void showFavoriteButton(Boolean isFavorite) {
        mFavoriteButton.setChecked(isFavorite);
    }

    @Override
    public void showFavoriteAddedToast() {
        Toast.makeText(getActivity(), R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFavoriteRemovedToast() {
        Toast.makeText(getActivity(), R.string.removed_from_favorites, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showContentLayout() {
        mContentLayout.setVisibility(View.VISIBLE);
        mMessageTextView.setVisibility(View.GONE);
        mMainLoadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.favoriteButton:
                Boolean isfavorite = mDetailPresenter.getIsFavorite();
                if (!isfavorite) {
                    Bitmap bitmap = ((BitmapDrawable) mPosterImage.getDrawable()).getBitmap();
                    byte[] imageByteArray = MoviePosterUtils.getImageByteArray(bitmap);
                    mDetailPresenter.addToFavorites(imageByteArray);
                } else {
                    mDetailPresenter.removeFromFavorites();
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public void onItemClicked(String videoId) {
        watchYoutubeVideo(videoId);
    }
}
