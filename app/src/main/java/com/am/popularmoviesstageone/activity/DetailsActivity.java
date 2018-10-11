package com.am.popularmoviesstageone.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.am.popularmoviesstageone.R;
import com.am.popularmoviesstageone.adapter.MoviesTrailersAdapter;
import com.am.popularmoviesstageone.databinding.ActivityDetailsBinding;
import com.am.popularmoviesstageone.databinding.ContentDetailsBinding;
import com.am.popularmoviesstageone.model.Movie;
import com.am.popularmoviesstageone.model.MovieVideosEntity;
import com.am.popularmoviesstageone.model.moviedetails.MovieDetails;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.am.popularmoviesstageone.util.CONST.BASE_BACKGROUND_IMAGE_URL;
import static com.am.popularmoviesstageone.util.CONST.BASE_POSTERS_URL;
import static com.am.popularmoviesstageone.util.CONST.EXTRA_MOVIE;
import static com.am.popularmoviesstageone.util.IntentsUtill.watchYoutubeVideo;


public class DetailsActivity extends BaseActivity {
    private static final String TAG = DetailsActivity.class.getSimpleName();

    private MoviesTrailersAdapter mTrailersAdapter;
    private ActivityDetailsBinding mLayout;
    private ContentDetailsBinding mContentLayout;
    private MovieDetails movieDetails;
    private Integer movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayout = DataBindingUtil.setContentView(this, R.layout.activity_details);
        setSupportActionBar(mLayout.toolbar);

        movieId = getIntent().getExtras().getInt(EXTRA_MOVIE);
        getMovieDetails(movieId);
        mContentLayout = mLayout.contentLayout;

        mContentLayout.trailersRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mTrailersAdapter = new MoviesTrailersAdapter(this,
                trailer -> watchYoutubeVideo(DetailsActivity.this, trailer.getKey()));
        mContentLayout.trailersRv.setAdapter(mTrailersAdapter);
        mContentLayout.trailersRv.setNestedScrollingEnabled(false);


//        mContentLayout.movieTitleTv.setText(movieId.getOriginalTitle());
//        mContentLayout.durationTv.setText(movieId.getRunTime() + "");
//        mContentLayout.ratingTv.setText("Rating - " + movieId.getVoteAverage());
//        mContentLayout.overviewTv.setText(movieId.getOverview());
//        Glide.with(this).load(BASE_POSTERS_URL + movieId.getPosterPath()).into(mContentLayout.imageView);
//        Glide.with(this).load(BASE_BACKGROUND_IMAGE_URL + movieId.getBackdropPath()).into(mLayout.movieBackdropIv);
//        mContentLayout.relaseDateTv.setText(movieId.getReleaseDate() + " (Released)");
//        getSupportActionBar().setTitle(movieId.getTitle());
//        mLayout.fab.setOnClickListener(view -> Snackbar.make(view, "Added to favorite ",
//                Snackbar.LENGTH_LONG).setAction("Action", null).show());

    }


    private void getMovieDetails(int movieId) {
        apiService.getMovieDetails(movieId + "").enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                final MovieDetails movieDetails = response.body();
                Log.d(TAG, "onResponse: " + movieDetails);
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                Log.e(TAG, t.toString());
                Toast.makeText(DetailsActivity.this, "Error Loading Movie Details", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
