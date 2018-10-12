package com.am.popularmoviesstageone.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.am.popularmoviesstageone.R;
import com.am.popularmoviesstageone.adapter.MoviesReviewsAdapter;
import com.am.popularmoviesstageone.adapter.MoviesTrailersAdapter;
import com.am.popularmoviesstageone.room.FavMovieEntity;
import com.am.popularmoviesstageone.room.MoviesDatabase;
import com.am.popularmoviesstageone.model.Movie;
import com.am.popularmoviesstageone.model.MovieReviewsEntity;
import com.am.popularmoviesstageone.model.MovieVideosEntity;
import com.am.popularmoviesstageone.network.APIClient;
import com.am.popularmoviesstageone.network.ApiRequests;
import com.am.popularmoviesstageone.room.Repository;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.am.popularmoviesstageone.util.CONST.BASE_BACKGROUND_IMAGE_URL;
import static com.am.popularmoviesstageone.util.CONST.BASE_POSTERS_URL;
import static com.am.popularmoviesstageone.util.CONST.EXTRA_MOVIE_ID;
import static com.am.popularmoviesstageone.util.IntentsUtill.watchYoutubeVideo;

public class MovieDetailsActivity extends BaseActivity {

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
    RecyclerView mTrailersRecyclerView;
    @BindView(R.id.rv_reviews)
    RecyclerView mReviewsRecyclerView;
    @BindView(R.id.favoriteFab)
    FloatingActionButton fab;
    @BindView(R.id.iv_movie_background)
    ImageView mMovieBackground;
    private ApiRequests apiService = APIClient.getClient().create(ApiRequests.class);

    private MoviesTrailersAdapter mTrailersAdapter;
    private MoviesReviewsAdapter mReviewsAdapter;
    private MoviesDatabase mDb;

    private Movie movie;
    private int movieId;
    private boolean isFavourite = false;

    Repository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mRepository = new Repository(getApplication());

        mDb = MoviesDatabase.getsInstance(this);

        mTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false));
        mTrailersAdapter = new MoviesTrailersAdapter(this,
                trailer -> watchYoutubeVideo(MovieDetailsActivity.this, trailer.getKey()));
        mTrailersRecyclerView.setAdapter(mTrailersAdapter);
        mTrailersRecyclerView.setNestedScrollingEnabled(false);

        mReviewsAdapter = new MoviesReviewsAdapter(this);
        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);
        mReviewsRecyclerView.setNestedScrollingEnabled(false);

        movie = getIntent().getExtras().getParcelable(EXTRA_MOVIE_ID);
        movieId = movie.getId();

        mNameTextView.setText(movie.getTitle());
        mReleaseDateTextView.setText(movie.getReleaseDate());
        mUserRatingTextView.setText(String.valueOf(movie.getVoteAverage()));
        mOverviewTextView.setText(movie.getOverview());
        Glide.with(this).load(BASE_POSTERS_URL + movie.getPosterPath()).into(mPosterImageView);
        Glide.with(this).load(BASE_BACKGROUND_IMAGE_URL + movie.getBackdropPath()).into(mMovieBackground);

        getMovieVideos();
        getMovieReviews();
        setupViewModel();
    }

    private void setupViewModel() {
//        FavMoviesViewModel favMoviesModelView = ViewModelProviders.of(this).get(FavMoviesViewModel.class);
//        favMoviesModelView.getFavMoviesList().observe(this, new Observer<List<FavMovieEntity>>() {
//            @Override
//            public void onChanged(@Nullable List<FavMovieEntity> favMovieEntities) {
//            }
//        });


//        if (mRepository.loadMovieById(movieId) != null) {
//            fab.setImageResource(R.drawable.ic_star_fill_24dp);
//            isFavourite = true;
//        } else {
//            fab.setImageResource(R.drawable.ic_star_empty_24dp);
//            isFavourite = false;
//        }
    }


    private void getMovieVideos() {
        apiService.getMovieVideos(movieId + "").enqueue(new Callback<MovieVideosEntity>() {
            @Override
            public void onResponse(Call<MovieVideosEntity> call, Response<MovieVideosEntity> response) {
                final MovieVideosEntity movieVideosEntity = response.body();
                mTrailersAdapter.addAll(movieVideosEntity.getTrailers());
            }

            @Override
            public void onFailure(Call<MovieVideosEntity> call, Throwable t) {
                Log.e(TAG, t.toString());
                Toast.makeText(MovieDetailsActivity.this, "Error Loading Trailers", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getMovieReviews() {
        apiService.getMovieReviews(movieId + "").enqueue(new Callback<MovieReviewsEntity>() {
            @Override
            public void onResponse(Call<MovieReviewsEntity> call, Response<MovieReviewsEntity> response) {
                final MovieReviewsEntity movieReviewsEntity = response.body();
                mReviewsAdapter.addAll(movieReviewsEntity.getReviewList());
            }

            @Override
            public void onFailure(Call<MovieReviewsEntity> call, Throwable t) {
                Log.e(TAG, t.toString());
                Toast.makeText(MovieDetailsActivity.this, "Error Loading Reviews", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onFabClicked(View view) {
        if (isFavourite) {
            mDb.favMovieDao().deleteMovie(mDb.favMovieDao().loadMovieById(movieId));
            fab.setImageResource(R.drawable.ic_star_empty_24dp);
            isFavourite = false;
        } else {
            mDb.favMovieDao().insertMovie(new FavMovieEntity(movieId, movie.getOriginalTitle(), movie.getPosterPath()));
            fab.setImageResource(R.drawable.ic_star_fill_24dp);
            isFavourite = true;
        }
    }
}
