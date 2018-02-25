package com.example.popularmovies.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by Victor Holotescu on 19-02-2018.
 */

public class MoviePosterUtils {

    private static final String BASE_PICTURE_URL = "http://image.tmdb.org/t/p/";

    private static final String WIDTH_BIG = "w342";
    private static final String WIDTH_SMALL = "w185";

    public static String getSmallMoviePosterUrlString(String path) {
        String urlString = BASE_PICTURE_URL + WIDTH_SMALL + path;
        return urlString;
    }

    public static String getBigMoviePosterUrlString(String path) {
        String urlString = BASE_PICTURE_URL + WIDTH_BIG + path;
        return urlString;
    }

    public static byte[] getImageByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap getBitmapFromByteArray(byte[] imageByteArray){
        return BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
    }

}
