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
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import static com.am.popularmoviesstageone.util.IntentsUtill.watchYoutubeVideo;


public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = DetailsActivity.class.getSimpleName();

    private MoviesTrailersAdapter mTrailersAdapter;
    private ActivityDetailsBinding mLayout;
    private ContentDetailsBinding mContentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayout = DataBindingUtil.setContentView(this, R.layout.activity_details);
        setSupportActionBar(mLayout.toolbar);

        mContentLayout = mLayout.contentLayout ;
        mContentLayout.trailersRv.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false));
        mTrailersAdapter = new MoviesTrailersAdapter(this,
                trailer -> watchYoutubeVideo(DetailsActivity.this, trailer.getKey()));
        mContentLayout.trailersRv.setAdapter(mTrailersAdapter);
        mContentLayout.trailersRv.setNestedScrollingEnabled(false);


        getSupportActionBar().setTitle("Leon: The Professional");
        mLayout.fab.setOnClickListener(view -> Snackbar.make(view, "Added to favorite ",
                Snackbar.LENGTH_LONG).setAction("Action", null).show());

    }


}
