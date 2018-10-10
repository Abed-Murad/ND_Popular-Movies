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
import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
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

        mContentLayout = mLayout.contentLayout;
        mContentLayout.trailersRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mTrailersAdapter = new MoviesTrailersAdapter(this,
                trailer -> watchYoutubeVideo(DetailsActivity.this, trailer.getKey()));
        mContentLayout.trailersRv.setAdapter(mTrailersAdapter);
        mContentLayout.trailersRv.setNestedScrollingEnabled(false);

        loadImageAsABackgournd(this, "http://image.tmdb.org/t/p/w500//fXVzk9OxQwROCuiWvd0Cv76qmZi.jpg", mLayout.appBar);
        getSupportActionBar().setTitle("Leon: The Professional");
        mLayout.fab.setOnClickListener(view -> Snackbar.make(view, "Added to favorite ",
                Snackbar.LENGTH_LONG).setAction("Action", null).show());

    }

    /**
     * @param context   Context For the Loading Process to be cancelled if the activity closed
     * @param imageUrl  imageUrl to be loaded in the View or viewGroup
     * @param viewGroup the viewGroup that we want to load the image into its background
     *                  this method is used for the to load an image to a view as a background for the views
     *                  that does not have setSrc attr
     */

    public void loadImageAsABackgournd(Context context, String imageUrl, ViewGroup viewGroup) {
        Glide.with(context).load(imageUrl).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewGroup.setBackground(resource);
                }
            }
        });
    }

}
