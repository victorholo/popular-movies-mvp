package com.example.popularmovies.data.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.popularmovies.AppExecutors;
import com.example.popularmovies.data.GetMoviesListener;
import com.example.popularmovies.data.models.movie.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Holotescu on 23-02-2018.
 */

public class MoviesLocalDataSource implements LocalDataSource {


    private static final String LOG_TAG = MoviesLocalDataSource.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static MoviesLocalDataSource sInstance;

    private final ContentResolver mContentResolver;

    private MoviesLocalDataSource(ContentResolver contentResolver, AppExecutors appExecutors) {
        mContentResolver = contentResolver;
        AppExecutors mAppExecutors = appExecutors;
    }

    /**
     * Get the singleton for this class
     */
    public static MoviesLocalDataSource getInstance(ContentResolver contentResolver, AppExecutors appExecutors) {
        Log.d(LOG_TAG, "Getting the local data source");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new MoviesLocalDataSource(contentResolver, appExecutors);
                Log.d(LOG_TAG, "Made new local data source");
            }
        }
        return sInstance;
    }

    @Override
    public void getFavorites(GetMoviesListener callback) {
            Cursor cursor = mContentResolver.query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);

            List<Movie> favoriteMovies = new ArrayList<>();
            while(cursor.moveToNext()){
                Movie favoriteMovie = new Movie();

                byte[] picture = cursor.getBlob(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_PICTURE));
                favoriteMovie.setCachedImage(picture);

                String title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
                favoriteMovie.setOriginalTitle(title);

                String overview = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW));
                favoriteMovie.setOverview(overview);

                double rating = cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING));
                favoriteMovie.setVoteAverage(rating);

                String releseDate = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
                favoriteMovie.setReleaseDate(releseDate);

                int id = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry._ID));
                favoriteMovie.setId(id);

                favoriteMovies.add(favoriteMovie);
            }
            cursor.close();

            callback.onSuccess(favoriteMovies);



    }

    @Override
    public Boolean getIsFavourite(int id) {
        Cursor cursor = mContentResolver.query(MovieContract.MovieEntry.buildTasksUriWith(String.valueOf(id)), null, null, null, null);
        Boolean isFavourite = cursor.getCount() > 0;
        cursor.close();

        return isFavourite;
    }

    @Override
    public Boolean addToFavorites(Movie movie) {
        if(movie.getCachedImage() == null)
            return false;
        ContentValues values = MovieValues.from(movie);
        mContentResolver.insert(MovieContract.MovieEntry.CONTENT_URI, values);

        return true;
    }

    @Override
    public Boolean removeFromFavorites(int id) {

        int deleted = mContentResolver.delete(MovieContract.MovieEntry.buildTasksUriWith(String.valueOf(id)), null, null);
        Log.v(LOG_TAG, String.valueOf(deleted));
        return deleted > 0;
    }
}
