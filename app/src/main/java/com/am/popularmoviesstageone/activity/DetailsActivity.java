package com.am.popularmoviesstageone.activity;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.am.popularmoviesstageone.R;
import com.am.popularmoviesstageone.adapter.ReviewsAdapter;
import com.am.popularmoviesstageone.adapter.TrailersAdapter;
import com.am.popularmoviesstageone.databinding.ActivityDetailsBinding;
import com.am.popularmoviesstageone.model.Movie;
import com.am.popularmoviesstageone.model.ReviewList;
import com.am.popularmoviesstageone.model.TrailerList;
import com.am.popularmoviesstageone.model.moviedetails.MovieDetails;
import com.am.popularmoviesstageone.room.MovieDao;
import com.am.popularmoviesstageone.room.MoviesDatabase;
import com.am.popularmoviesstageone.util.AMApplication;
import com.am.popularmoviesstageone.util.FUNC;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.am.popularmoviesstageone.util.CONST.BASE_BACKGROUND_IMAGE_URL;
import static com.am.popularmoviesstageone.util.CONST.BASE_POSTERS_URL;
import static com.am.popularmoviesstageone.util.CONST.EXTRA_MOVIE;
import static com.am.popularmoviesstageone.util.IntentsUtil.openVideoOnYoutube;


public class DetailsActivity extends BaseActivity {

    private MoviesDatabase myDatabase;
    private MovieDao movieDao;

    private TrailersAdapter mTrailersAdapter;
    private ReviewsAdapter mReviewsAdapter;
    private ActivityDetailsBinding mLayout;
    private MovieDetails movieDetails;
    private Movie mMovie;

    private boolean isFavourite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDatabase = ((AMApplication) getApplication()).getMyDatabase();
        movieDao = myDatabase.movieDao();
        mMovie = getIntent().getExtras().getParcelable(EXTRA_MOVIE);
        mLayout = DataBindingUtil.setContentView(this, R.layout.activity_details);
        setSupportActionBar(mLayout.toolbar);
        isFavourite = movieDao.getById(mMovie.getId()) != null;
        if (isFavourite) {
            mLayout.favoriteFab.setImageResource(R.drawable.ic_heart_full);
        }
        setFabListeners();
        getMovieDetails(mMovie.getId());
        initRecyclerView();
    }

    private void setFabListeners() {
        mLayout.favoriteFab.setOnClickListener(view -> {
            if (isFavourite) {
                Snackbar.make(view, R.string.title_remove_from_favorite, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                mLayout.favoriteFab.setImageResource(R.drawable.ic_heart_empty);
                movieDao.delete((mMovie));
                isFavourite = false;
            } else {

                Snackbar.make(view, R.string.title_add_to_favorite, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                mLayout.favoriteFab.setImageResource(R.drawable.ic_heart_full);
                movieDao.insert((mMovie));
                isFavourite = true;


            }
        });
    }

    private void initRecyclerView() {
        mLayout.trailersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mTrailersAdapter = new TrailersAdapter(this, trailer -> openVideoOnYoutube(DetailsActivity.this, trailer.getKey()));
        mLayout.trailersRecyclerView.setAdapter(mTrailersAdapter);
        mLayout.trailersRecyclerView.setNestedScrollingEnabled(false);
        mReviewsAdapter = new ReviewsAdapter(this);

        mLayout.reviewsRecyclerView.setNestedScrollingEnabled(false);
        mLayout.reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLayout.reviewsRecyclerView.setAdapter(mReviewsAdapter);
        mLayout.reviewsRecyclerView.setNestedScrollingEnabled(false);
    }

    private void getMovieDetails(int movieId) {
        mApiService.getMovieDetails(movieId + "").enqueue(new Callback<MovieDetails>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<MovieDetails> call, @NonNull Response<MovieDetails> response) {
                movieDetails = response.body();
                populateUi(movieId);

            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                Logger.e("Failed to fetch Movie Details", t);
            }
        });


    }

    private void populateUi(int movieId) {
        mLayout.movieTitleTextView
                .setText(movieDetails != null ? movieDetails.getOriginalTitle() : "Original Title Not Found");
        mLayout.ratingTextView.setText(movieDetails.getVoteAverage() + "");
        mLayout.descriptionTextView.setText(movieDetails.getOverview());

        Glide.with(DetailsActivity.this)
                .load(BASE_POSTERS_URL + movieDetails.getPosterPath())
                .into(mLayout.posterImageView);
        Glide.with(DetailsActivity.this)
                .load(BASE_BACKGROUND_IMAGE_URL + movieDetails.getBackdropPath())
                .into(mLayout.backDropImageView);

        Date releaseDate = movieDetails.getReleaseDate();
        mLayout.releaseYearTextView
                .setText(FUNC.getDateDetails(releaseDate));

        getMovieVideos(movieId);
        getMovieReviews(movieId);
        mLayout.toolbar.setTitle(movieDetails.getTitle());
        setSupportActionBar(mLayout.toolbar);
    }

    private void getMovieVideos(int movieId) {
        mApiService.getMovieTrailers(movieId + "").enqueue(new Callback<TrailerList>() {
            @Override
            public void onResponse(Call<TrailerList> call, Response<TrailerList> response) {
                final TrailerList trailerList = response.body();
                mTrailersAdapter.addAll(trailerList.getTrailers());

                if (trailerList.getTrailers() != null && trailerList.getTrailers().size() != 0) {
                    mTrailersAdapter.addAll(trailerList.getTrailers());
                } else {
                    mLayout.trailersTitleTextView.setVisibility(View.GONE);
                    mLayout.trailersRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<TrailerList> call, Throwable t) {
                Logger.e("Failed to fetch Movie Trailers", t);
            }
        });
    }

    private void getMovieReviews(int movieId) {
        mApiService.getMovieReviews(movieId + "").enqueue(new Callback<ReviewList>() {
            @Override
            public void onResponse(Call<ReviewList> call, Response<ReviewList> response) {
                final ReviewList movieReviewsEntity = response.body();
                if (movieReviewsEntity.getReviewList() != null && movieReviewsEntity.getReviewList().size() != 0) {
                    mReviewsAdapter.addAll(movieReviewsEntity.getReviewList());
                } else {
                    mLayout.reviewsTitleTextView.setVisibility(View.GONE);
                    mLayout.reviewsRecyclerView.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFailure(Call<ReviewList> call, Throwable t) {
                Logger.e("Failed to fetch Movie Reviews", t);
            }
        });
    }


}
