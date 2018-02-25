package com.example.popularmovies;

import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.example.popularmovies.data.DataRepository;
import com.example.popularmovies.ui.detail.DetailFragment;
import com.example.popularmovies.ui.detail.DetailPresenter;
import com.example.popularmovies.ui.detail.DetailPresenterImpl;
import com.example.popularmovies.ui.main.MainFragment;
import com.example.popularmovies.ui.main.MainPresenter;
import com.example.popularmovies.ui.main.MainPresenterImpl;
import com.example.popularmovies.utilities.ActivityUtils;
import com.example.popularmovies.utilities.InjectorUtils;

import static com.example.popularmovies.utilities.ActivityUtils.isTablet;

/**
 * Created by Victor Holotescu on 23-02-2018.
 */

public class MovieMvpController {

    private FragmentActivity mFragmentActivity;

    private String mFilter;

    private int mMovieId;
    private static MoviesTabletPresenter mMoviesTabletPresenter;

    private static MainPresenter mMoviesPresenter;

    private MovieMvpController(FragmentActivity fragmentActivity, int movieId, String filter) {
        mFragmentActivity = fragmentActivity;
        if(mFilter == null) mFilter = filter;
        mMovieId = movieId;
    }

    public static MovieMvpController createMoviesView(FragmentActivity fragmentActivity, int movieId, String filter) {
        MovieMvpController moviesMvpController =  new MovieMvpController(fragmentActivity, movieId, filter);

        moviesMvpController.initViews();
        return moviesMvpController;
    }

    private void initViews() {
        if (isTablet(mFragmentActivity)) {
            createTabletElements();
        } else {
            createPhoneElements();
        }
    }

    private void createPhoneElements() {
        MainFragment mainFragment = findOrCreateMainFragment(R.id.contentFrame);
        mMoviesPresenter = createMainPresenter(mainFragment);
        mMoviesPresenter.setFiltering(mFilter);
        mainFragment.setPresenter(mMoviesPresenter);
    }

    private void createTabletElements() {
        // Fragment 1: List
        MainFragment mainFragment = findOrCreateMainFragment(R.id.contentFrame_list);
        mMoviesPresenter = createMainPresenter(mainFragment);

        // Fragment 2: Detail
        DetailFragment detailFragment = findOrCreateDetailFragmentForTablet();
        DetailPresenter detailPresenter = createDetailPresenter(detailFragment);

        DataRepository dataRepository = InjectorUtils.provideRepository(mFragmentActivity.getApplicationContext());
        // Fragments connect to their presenters through a tablet presenter:
        mMoviesTabletPresenter = new MoviesTabletPresenter(dataRepository, mMoviesPresenter);

        mainFragment.setPresenter(mMoviesTabletPresenter);
        detailFragment.setPresenter(mMoviesTabletPresenter);
        mMoviesTabletPresenter.setDetailPresenter(detailPresenter);
        mMoviesTabletPresenter.setFiltering(mFilter);
    }

    @NonNull
    private DetailPresenter createDetailPresenter(DetailFragment detailFragment) {
        DataRepository dataRepository = InjectorUtils.provideRepository(mFragmentActivity.getApplicationContext());

        return new DetailPresenterImpl(mMovieId, dataRepository, detailFragment);
    }

    private MainPresenter createMainPresenter(MainFragment mainFragment) {
        DataRepository dataRepository = InjectorUtils.provideRepository(mFragmentActivity.getApplicationContext());

        mMoviesPresenter = new MainPresenterImpl(dataRepository, mainFragment) {
        };
        return mMoviesPresenter;
    }

    @NonNull
    private MainFragment findOrCreateMainFragment(@IdRes int fragmentId) {
        MainFragment mainFragment =
                (MainFragment) getFragmentById(fragmentId);
        if (mainFragment == null) {
            // Create the fragment
            mainFragment = MainFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mainFragment, fragmentId);
        }
        return mainFragment;
    }

    @NonNull
    private DetailFragment findOrCreateDetailFragmentForTablet() {
        DetailFragment detailFragment =
                (DetailFragment) getFragmentById(R.id.contentFrame_detail);
        if (detailFragment == null) {
            // Create the fragment
            detailFragment = DetailFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), detailFragment, R.id.contentFrame_detail);
        }
        return detailFragment;
    }

    private Fragment getFragmentById(int contentFrame_detail) {
        return getSupportFragmentManager().findFragmentById(contentFrame_detail);
    }

    public int getMovieId() {
        if(isTablet(mFragmentActivity)){
            return mMoviesTabletPresenter.getMovieId();
        }else{
            return -1;
        }
    }

    public void setMovieId(int id){
        if(isTablet(mFragmentActivity)){
            mMoviesTabletPresenter.setMovieId(id);
        }
    }

    private FragmentManager getSupportFragmentManager() {
        return mFragmentActivity.getSupportFragmentManager();
    }

    public void loadMovies(String sortType){
        if(isTablet(mFragmentActivity)){
            mMoviesTabletPresenter.loadMovies(sortType);
        }else{
            mMoviesPresenter.loadMovies(sortType);
        }
    }
}
