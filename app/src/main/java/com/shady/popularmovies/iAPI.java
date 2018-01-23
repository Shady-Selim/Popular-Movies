package com.shady.popularmovies;

/**
 * Created by Shady on 8/15/2017.
 */

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface iAPI {
//    Get Top Rated Movies
    @GET("top_rated")
    Call<MoviesClass> listTopRated(@Query("api_key") String api_key);

//    Get PopularClass Movies
    @GET("popular")
    Call<MoviesClass> listPopular(@Query("api_key") String api_key);

//    Get Videos
    @GET("{id}/videos")
    Call<VideosClass> listVideos(@Path("id") String id, @Query("api_key") String api_key);

//    Get Reviews
    @GET("{id}/reviews")
    Call<ReviewsClass> listReviews(@Path("id") String id, @Query("api_key") String api_key);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
