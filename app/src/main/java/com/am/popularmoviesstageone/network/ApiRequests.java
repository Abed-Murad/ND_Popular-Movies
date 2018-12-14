package com.am.popularmoviesstageone.network;

import com.am.popularmoviesstageone.model.MovieReviewsEntity;
import com.am.popularmoviesstageone.model.MovieVideosEntity;
import com.am.popularmoviesstageone.model.MoviesList;
import com.am.popularmoviesstageone.model.moviedetails.MovieDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiRequests {

    @GET("movie/popular")
    Call<MoviesList> getPopularMovies(@Query("language") String language,
                                      @Query("page") String page,
                                      @Query("region") String region);

    @GET("movie/top_rated")
    Call<MoviesList> getTopRatedMovies(@Query("language") String language,
                                       @Query("page") String page,
                                       @Query("region") String region);

    @GET("movie/{id}/videos")
    Call<MovieVideosEntity> getMovieTrailers(@Path("id") String movieId);

    @GET("movie/{id}/reviews")
    Call<MovieReviewsEntity> getMovieReviews(@Path("id") String movieId);

    @GET("movie/{id}")
    Call<MovieDetails> getMovieDetails(@Path("id") String movieId);
}
