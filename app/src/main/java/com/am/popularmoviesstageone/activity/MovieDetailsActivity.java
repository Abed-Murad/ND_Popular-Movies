package com.am.popularmoviesstageone.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.am.popularmoviesstageone.R;
import com.am.popularmoviesstageone.model.Movie;
import com.am.popularmoviesstageone.model.MovieVideosEntity;
import com.am.popularmoviesstageone.model.MoviesList;
import com.am.popularmoviesstageone.network.APIClient;
import com.am.popularmoviesstageone.network.ApiRequests;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.am.popularmoviesstageone.util.CONST.BASE_POSTERS_URL;
import static com.am.popularmoviesstageone.util.CONST.EXTRA_MOVIE;

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

    private ApiRequests apiService = APIClient.getClient().create(ApiRequests.class);
    private int movieId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Movie movie = getIntent().getExtras().getParcelable(EXTRA_MOVIE);
        movieId = movie.getId();

        movieNameTextView.setText(movie.getTitle());
        movieReleaseDateTextView.setText(movie.getReleaseDate());
        movieUserRatingTextView.setText(String.valueOf(movie.getVoteAverage()));
        movieOverviewTextView.setText(movie.getOverview());
        Glide.with(this).load(BASE_POSTERS_URL + movie.getPosterPath()).into(moviePosterImageView);

        getMovieVideos();
    }


    private void getMovieVideos() {
        apiService.getMovieVideos(movieId+"").enqueue(new Callback<MovieVideosEntity>() {
            @Override
            public void onResponse(Call<MovieVideosEntity> call, Response<MovieVideosEntity> response) {
                final MovieVideosEntity movieVideosEntity = response.body();
            }

            @Override
            public void onFailure(Call<MovieVideosEntity> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

}
