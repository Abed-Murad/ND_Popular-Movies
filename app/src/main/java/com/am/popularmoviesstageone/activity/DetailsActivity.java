package com.am.popularmoviesstageone.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;

import com.am.popularmoviesstageone.R;
import com.am.popularmoviesstageone.adapter.ReviewsAdapter;
import com.am.popularmoviesstageone.adapter.TrailersAdapter;
import com.am.popularmoviesstageone.databinding.ActivityDetailsBinding;
import com.am.popularmoviesstageone.databinding.ContentDetailsBinding;
import com.am.popularmoviesstageone.model.MovieReviewsEntity;
import com.am.popularmoviesstageone.model.MovieVideosEntity;
import com.am.popularmoviesstageone.model.moviedetails.MovieDetails;
import com.am.popularmoviesstageone.util.FUNC;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.am.popularmoviesstageone.util.CONST.BASE_BACKGROUND_IMAGE_URL;
import static com.am.popularmoviesstageone.util.CONST.BASE_POSTERS_URL;
import static com.am.popularmoviesstageone.util.CONST.EXTRA_MOVIE_ID;
import static com.am.popularmoviesstageone.util.IntentsUtill.watchYoutubeVideo;


public class DetailsActivity extends BaseActivity {

    private TrailersAdapter mTrailersAdapter;
    private ReviewsAdapter mReviewsAdapter;
    private ActivityDetailsBinding mLayout;
    private ContentDetailsBinding mContentLayout;
    private MovieDetails movieDetails;

    private boolean isFavourite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int movieId = getIntent().getExtras().getInt(EXTRA_MOVIE_ID);
        mLayout = DataBindingUtil.setContentView(this, R.layout.activity_details);
        mContentLayout = mLayout.contentLayout;
        setSupportActionBar(mLayout.toolbar);
        getMovieDetails(movieId);



        mContentLayout.trailersRv.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        mTrailersAdapter = new TrailersAdapter(this,
                trailer -> watchYoutubeVideo(DetailsActivity.this, trailer.getKey()));
        mContentLayout.trailersRv.setAdapter(mTrailersAdapter);
        mContentLayout.trailersRv.setNestedScrollingEnabled(false);
        mReviewsAdapter = new ReviewsAdapter(this);

        mContentLayout.reviewsRv.setNestedScrollingEnabled(false);
        mContentLayout.reviewsRv.setLayoutManager(new LinearLayoutManager(this));
        mContentLayout.reviewsRv.setAdapter(mReviewsAdapter);
        mContentLayout.reviewsRv.setNestedScrollingEnabled(false);

        mLayout.fab.setOnClickListener(view -> {
            if (!isFavourite) {
                Snackbar.make(view, "Added to favorite ", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                mLayout.fab.setImageResource(R.drawable.ic_star_fill_24dp);
                isFavourite = true;
            } else {
                Snackbar.make(view, "Removed from favorite ", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                mLayout.fab.setImageResource(R.drawable.ic_star_empty_24dp);
                isFavourite = false;
            }
        });
    }

    private void getMovieDetails(int movieId) {
        mApiService.getMovieDetails(movieId + "").enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                movieDetails = response.body();

                mContentLayout.movieTitleTv.setText(movieDetails.getOriginalTitle());
                mContentLayout.durationTv.setText("Duration - " + movieDetails.getRuntime() + " min");
                mContentLayout.ratingTv.setText("Rating - " + movieDetails.getVoteAverage());
                mContentLayout.overviewTv.setText(movieDetails.getOverview());
                Glide.with(DetailsActivity.this).load(BASE_POSTERS_URL + movieDetails.getPosterPath()).into(mContentLayout.imageView);
                Glide.with(DetailsActivity.this).load(BASE_BACKGROUND_IMAGE_URL + movieDetails.getBackdropPath()).into(mLayout.movieBackdropIv);

                Date releaseDate = movieDetails.getReleaseDate();
                mContentLayout.relaseDateTv.setText(FUNC.getDateDetails(releaseDate)+" (" + movieDetails.getStatus() + ")");

                getMovieVideos(movieId);
                getMovieReviews(movieId);
                mLayout.toolbar.setTitle(movieDetails.getTitle());
                setSupportActionBar(mLayout.toolbar);

            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                Logger.e("Failed to fetch Movie Details", t);
            }
        });


    }

    private void getMovieVideos(int movieId) {
        mApiService.getMovieVideos(movieId + "").enqueue(new Callback<MovieVideosEntity>() {
            @Override
            public void onResponse(Call<MovieVideosEntity> call, Response<MovieVideosEntity> response) {
                final MovieVideosEntity movieVideosEntity = response.body();
                mTrailersAdapter.addAll(movieVideosEntity.getTrailers());
            }

            @Override
            public void onFailure(Call<MovieVideosEntity> call, Throwable t) {
                Logger.e("Failed to fetch Movie Trailers", t);
            }
        });
    }

    private void getMovieReviews(int movieId) {
        mApiService.getMovieReviews(movieId + "").enqueue(new Callback<MovieReviewsEntity>() {
            @Override
            public void onResponse(Call<MovieReviewsEntity> call, Response<MovieReviewsEntity> response) {
                final MovieReviewsEntity movieReviewsEntity = response.body();
                mReviewsAdapter.addAll(movieReviewsEntity.getReviewList());

            }

            @Override
            public void onFailure(Call<MovieReviewsEntity> call, Throwable t) {
                Logger.e("Failed to fetch Movie Reviews", t);
            }
        });
    }


}
