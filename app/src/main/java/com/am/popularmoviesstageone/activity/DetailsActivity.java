package com.am.popularmoviesstageone.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;

import com.am.popularmoviesstageone.R;
import com.am.popularmoviesstageone.adapter.MoviesTrailersAdapter;
import com.am.popularmoviesstageone.databinding.ActivityDetailsBinding;
import com.am.popularmoviesstageone.databinding.ContentDetailsBinding;
import com.am.popularmoviesstageone.model.Movie;
import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import static com.am.popularmoviesstageone.util.CONST.BASE_BACKGROUND_IMAGE_URL;
import static com.am.popularmoviesstageone.util.CONST.BASE_POSTERS_URL;
import static com.am.popularmoviesstageone.util.CONST.EXTRA_MOVIE;
import static com.am.popularmoviesstageone.util.IntentsUtill.watchYoutubeVideo;


public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = DetailsActivity.class.getSimpleName();

    private MoviesTrailersAdapter mTrailersAdapter;
    private ActivityDetailsBinding mLayout;
    private ContentDetailsBinding mContentLayout;
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayout = DataBindingUtil.setContentView(this, R.layout.activity_details);
        setSupportActionBar(mLayout.toolbar);

        movie = getIntent().getExtras().getParcelable(EXTRA_MOVIE);
        mContentLayout = mLayout.contentLayout;

        mContentLayout.trailersRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mTrailersAdapter = new MoviesTrailersAdapter(this,
                trailer -> watchYoutubeVideo(DetailsActivity.this, trailer.getKey()));
        mContentLayout.trailersRv.setAdapter(mTrailersAdapter);
        mContentLayout.trailersRv.setNestedScrollingEnabled(false);
        mContentLayout.ratingTv.setText("Rating - " + movie.getVoteAverage());
        Glide.with(this).load(BASE_POSTERS_URL + movie.getPosterPath()).into(mContentLayout.imageView);
        Glide.with(this).load(BASE_BACKGROUND_IMAGE_URL + movie.getBackdropPath()).into(mLayout.movieBackdropIv);
        mContentLayout.relaseDateTv.setText(movie.getReleaseDate() + " (Released)");
        getSupportActionBar().setTitle(movie.getTitle());
        mLayout.fab.setOnClickListener(view -> Snackbar.make(view, "Added to favorite ",
                Snackbar.LENGTH_LONG).setAction("Action", null).show());

    }
}
