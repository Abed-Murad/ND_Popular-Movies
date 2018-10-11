package com.am.popularmoviesstageone.network;

import com.am.popularmoviesstageone.BuildConfig;
import com.am.popularmoviesstageone.R;
import com.am.popularmoviesstageone.model.MovieReviewsEntity;
import com.am.popularmoviesstageone.model.MovieVideosEntity;
import com.am.popularmoviesstageone.model.MoviesList;
import com.am.popularmoviesstageone.model.moviedetails.MovieDetails;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiRequests {
    String API_KEY = BuildConfig.API_KEY;

    @GET("movie/popular?api_key=" + API_KEY)
    Call<MoviesList> getPopularMovies(@Query("language") String language,
                                      @Query("page") String page,
                                      @Query("region") String region);

    @GET("movie/top_rated?api_key=" + API_KEY)
    Call<MoviesList> getTopRatedMovies(@Query("language") String language,
                                       @Query("page") String page,
                                       @Query("region") String region);

    @GET("movie/{id}/videos?api_key=" + API_KEY + "&language=en-US")
    Call<MovieVideosEntity> getMovieVideos(@Path("id") String movieId);

    @GET("movie/{id}/reviews?api_key=" + API_KEY + "&language=en-US")
    Call<MovieReviewsEntity> getMovieReviews(@Path("id") String movieId);

    @GET("movie/{id}?api_key=" + API_KEY + "&language=en-US")
    Call<MovieDetails> getMovieDetails(@Path("id") String movieId);


    // Post Request Example
    @POST("login")
    @FormUrlEncoded
    Call<Void> postRequst(@Field("ClientSecret") String secret,
                          @Header("Signature") String signature,
                          @Header("Token") String Token);

    // Delete Request Example
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "user/{user_id}/favComps", hasBody = true)
    Call<Void> deleteRequest(@Path("user_id") String userId,
                             @Field("comp_id") String compId,
                             @Header("Signature") String signature,
                             @Header("Token") String token);

    // Put Request Example
    @FormUrlEncoded
    @HTTP(method = "PUT", path = "comments/{comment_id}", hasBody = true)
    Call<Void> putRequest(@Path("comment_id") String comment_id,
                          @Field("new_text") String new_text,
                          @Field("page") String page,
                          @Header("Signature") String signature,
                          @Header("Token") String token);


}
