package com.example.popularmovies.utilities;

import android.content.Context;

import com.example.popularmovies.AppExecutors;
import com.example.popularmovies.data.DataRepository;
import com.example.popularmovies.data.db.LocalDataSource;
import com.example.popularmovies.data.db.MoviesLocalDataSource;
import com.example.popularmovies.data.network.ApiService;
import com.example.popularmovies.data.network.MoviesNetworkDataSource;
import com.example.popularmovies.data.network.NetworkDataSource;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Victor Holotescu on 19-02-2018.
 */

public class InjectorUtils {

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";

    public static DataRepository provideRepository(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        NetworkDataSource networkDataSource = MoviesNetworkDataSource.getInstance(service);

        AppExecutors appExecutors = AppExecutors.getInstance();

        LocalDataSource localDataSource = MoviesLocalDataSource.getInstance(context.getApplicationContext().getContentResolver(), appExecutors);

        return DataRepository.getInstance(networkDataSource, localDataSource);
    }


}
