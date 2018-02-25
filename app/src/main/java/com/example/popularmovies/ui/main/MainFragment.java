package com.example.popularmovies.ui.main;

import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.popularmovies.R;
import com.example.popularmovies.data.models.movie.Movie;
import com.example.popularmovies.ui.CustomRecyclerView;
import com.example.popularmovies.ui.detail.DetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainFragment extends Fragment implements MainView, View.OnClickListener, MoviesRecyclerAdapter.MoviesAdapterOnClickHandler {

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBar;

    @BindView(R.id.retry_button)
    Button mRetryButton;

    @BindView(R.id.error_textview)
    TextView mErrorTextView;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    private Unbinder mUnbinder;

    private MainPresenter mPresenter;

    private MoviesRecyclerAdapter mMoviesAdapter;

    private GridLayoutManager mGridLayoutManager;
    public MainFragment() {
        // Requires empty public constructor
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    private static final String CURRENT_GRID_POSITION_KEY = "current-grid-position-key";

    private Parcelable mCurrentRecyclerSavedState = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        mUnbinder = ButterKnife.bind(this, root);

        mRetryButton.setOnClickListener(this);

        mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesRecyclerAdapter(getActivity(), this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        mPresenter.loadMovies(null);

        return root;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            mCurrentRecyclerSavedState = savedInstanceState.getParcelable(CURRENT_GRID_POSITION_KEY);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CURRENT_GRID_POSITION_KEY, mGridLayoutManager.onSaveInstanceState());
    }

    public void setPresenter(MainPresenter mainPresenter) {
        mPresenter = mainPresenter;
    }

    @Override
    public void showLoading() {
        if(mProgressBar == null) return;
        mProgressBar.setVisibility(View.VISIBLE);
        mRetryButton.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showGrid(List<Movie> movies, Parcelable gridPosition) {
        if(mProgressBar == null) return;
        mProgressBar.setVisibility(View.INVISIBLE);
        mRetryButton.setVisibility(View.INVISIBLE);
        mErrorTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);

        mMoviesAdapter.swapMovies(movies);

        if (mCurrentRecyclerSavedState != null) {
            mGridLayoutManager.onRestoreInstanceState(mCurrentRecyclerSavedState);
        }
    }

    @Override
    public void showError() {
        if(mProgressBar == null) return;
        mProgressBar.setVisibility(View.INVISIBLE);
        mRetryButton.setVisibility(View.VISIBLE);
        mErrorTextView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showDetailView(int movieId) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.MOVIE_ID_EXTRA, movieId);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retry_button:
                mPresenter.loadMovies(null);
                break;
        }
    }


    @Override
    public void onItemClicked(int movieId) {
        mPresenter.updateDetail(movieId);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public String getStringResource(int resId) {
        return getResources().getString(resId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
