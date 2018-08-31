package com.am.popularmoviesstageone.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.am.popularmoviesstageone.R;
import com.am.popularmoviesstageone.adapter.MoviesTrailersAdapter;
import com.am.popularmoviesstageone.model.Movie;
import com.am.popularmoviesstageone.model.MovieTrailerEntity;
import com.am.popularmoviesstageone.model.MovieVideosEntity;
import com.am.popularmoviesstageone.network.APIClient;
import com.am.popularmoviesstageone.network.ApiRequests;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.am.popularmoviesstageone.util.CONST.BASE_POSTERS_URL;
import static com.am.popularmoviesstageone.util.CONST.BASE_YOUTUBE_URL;
import static com.am.popularmoviesstageone.util.CONST.EXTRA_MOVIE;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_movie_name)
    TextView mNameTextView;
    @BindView(R.id.iv_movie_poster)
    ImageView mPosterImageView;
    @BindView(R.id.tv_movie_release_date)
    TextView mReleaseDateTextView;
    @BindView(R.id.tv_movie_user_rating)
    TextView mUserRatingTextView;
    @BindView(R.id.tv_movie_length)
    TextView movieLengthTextView;
    @BindView(R.id.tv_movie_overview)
    TextView mOverviewTextView;
    @BindView(R.id.rv_trailers)
    RecyclerView mRecyclerView;

    private ApiRequests apiService = APIClient.getClient().create(ApiRequests.class);
    private MoviesTrailersAdapter mAdapter;
    private int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MoviesTrailersAdapter(this,
                trailer -> watchYoutubeVideo(MovieDetailsActivity.this, trailer.getKey()));
        mRecyclerView.setAdapter(mAdapter);

        Movie movie = getIntent().getExtras().getParcelable(EXTRA_MOVIE);
        movieId = movie.getId();

        mNameTextView.setText(movie.getTitle());
        mReleaseDateTextView.setText(movie.getReleaseDate());
        mUserRatingTextView.setText(String.valueOf(movie.getVoteAverage()));
        mOverviewTextView.setText(movie.getOverview());
        Glide.with(this).load(BASE_POSTERS_URL + movie.getPosterPath()).into(mPosterImageView);

        getMovieVideos();
    }


    private void getMovieVideos() {
        apiService.getMovieVideos(movieId + "").enqueue(new Callback<MovieVideosEntity>() {
            @Override
            public void onResponse(Call<MovieVideosEntity> call, Response<MovieVideosEntity> response) {
                final MovieVideosEntity movieVideosEntity = response.body();
                mAdapter.addAll(movieVideosEntity.getTrailers());
            }

            @Override
            public void onFailure(Call<MovieVideosEntity> call, Throwable t) {
                Log.e(TAG, t.toString());
                Toast.makeText(MovieDetailsActivity.this, "Error Loading Trailers", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static void watchYoutubeVideo(Context context, String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(BASE_YOUTUBE_URL + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

}
