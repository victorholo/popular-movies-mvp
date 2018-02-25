package com.example.popularmovies.data.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Victor Holotescu on 22-02-2018.
 */


public interface ApiService {

    @GET("{sortType}")
    Call<com.example.popularmovies.data.models.movie.Response> getMovies(@Path("sortType") String sortType, @Query(MoviesNetworkDataSource.API_KEY_PARAM) String api_key);

    @GET("{id}/reviews")
    Call<com.example.popularmovies.data.models.review.Response> getReviews(@Path("id") int id, @Query(MoviesNetworkDataSource.API_KEY_PARAM) String api_key);

    @GET("{id}/videos")
    Call<com.example.popularmovies.data.models.video.Response> getVideos(@Path("id") int id, @Query(MoviesNetworkDataSource.API_KEY_PARAM) String api_key);
}
