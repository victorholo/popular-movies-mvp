package com.example.popularmovies.data.db;

import android.content.ContentValues;

import com.example.popularmovies.data.models.movie.Movie;

/**
 * Created by Victor Holotescu on 23-02-2018.
 */

class MovieValues {


    public static ContentValues from(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry._ID, movie.getId());
        values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(MovieContract.MovieEntry.COLUMN_RATING, movie.getVoteAverage());
        values.put(MovieContract.MovieEntry.COLUMN_PICTURE, movie.getCachedImage());
        values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getOriginalTitle());
        return values;
    }
}
