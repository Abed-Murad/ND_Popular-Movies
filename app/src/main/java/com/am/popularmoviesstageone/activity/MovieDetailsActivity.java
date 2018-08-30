package com.am.popularmoviesstageone.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.am.popularmoviesstageone.R;
import com.am.popularmoviesstageone.model.Movie;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.am.popularmoviesstageone.util.CONST.BASE_POSTERS_URL;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_movie_name)
    TextView movieNameTextView;
    @BindView(R.id.iv_movie_poster)
    ImageView moviePosterImageView;
    @BindView(R.id.tv_movie_release_date)
    TextView movieReleaseDateTextView;
    @BindView(R.id.tv_movie_user_rating)
    TextView movieUserRatingTextView;
    @BindView(R.id.tv_movie_length)
    TextView movieLengthTextView;
    @BindView(R.id.tv_movie_overview)
    TextView movieOverviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Movie movie = getIntent().getExtras().getParcelable(MOVIE_PARCELABLE_KEY);
        Glide.with(this).load(BASE_POSTERS_URL + movie.getPosterPath()).into(moviePosterImageView);
        movieNameTextView.setText(movie.getTitle());
        movieReleaseDateTextView.setText(movie.getReleaseDate());
        movieUserRatingTextView.setText(String.valueOf(movie.getVoteAverage()));
        movieOverviewTextView.setText(movie.getOverview());

    }

}
