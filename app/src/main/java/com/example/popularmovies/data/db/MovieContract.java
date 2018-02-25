package com.example.popularmovies.data.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Victor Holotescu on 23-02-2018.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.popularmovies";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVOURITES = "favourites";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVOURITES)
                .build();

        public static final String TABLE_NAME = "favorite_movies";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_PICTURE = "picture";

        public static final String COLUMN_RATING = "rating";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_OVERVIEW = "overview";


        public static Uri buildTasksUriWith(String id) {
            Uri uri = CONTENT_URI.buildUpon().appendPath(id).build();
            return uri;
        }
    }
}
