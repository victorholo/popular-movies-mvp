package com.example.popularmovies.ui.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.popularmovies.R;
import com.example.popularmovies.data.DataRepository;
import com.example.popularmovies.utilities.ActivityUtils;
import com.example.popularmovies.utilities.InjectorUtils;

/**
 * Created by Victor Holotescu on 23-02-2018.
 */

public class DetailActivity extends AppCompatActivity {

    public static final String MOVIE_ID_EXTRA = "extra_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int movieId = getIntent().getIntExtra(MOVIE_ID_EXTRA, -1);

        DetailFragment mDetailFragment = (DetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (mDetailFragment == null) {
            mDetailFragment = DetailFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    mDetailFragment, R.id.contentFrame);
        }
        DataRepository dataRepository = InjectorUtils.provideRepository(this.getApplicationContext());
        // Create the presenter
        DetailPresenter detailPresenter = new DetailPresenterImpl(
                movieId,
                dataRepository,
                mDetailFragment);

        mDetailFragment.setPresenter(detailPresenter);
    }

}
