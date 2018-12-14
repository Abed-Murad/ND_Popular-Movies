package com.am.popularmoviesstageone.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.am.popularmoviesstageone.R;
import com.am.popularmoviesstageone.adapter.PostersAdapter;
import com.am.popularmoviesstageone.databinding.ActivityMainBinding;
import com.am.popularmoviesstageone.databinding.ContentMainBinding;
import com.am.popularmoviesstageone.model.Movie;
import com.am.popularmoviesstageone.model.MoviesList;
import com.am.popularmoviesstageone.util.AMApplication;
import com.orhanobut.logger.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.am.popularmoviesstageone.util.CONST.EXTRA_MOVIE_ID;
import static com.am.popularmoviesstageone.util.CONST.FAVORITES;
import static com.am.popularmoviesstageone.util.CONST.POPULAR;
import static com.am.popularmoviesstageone.util.CONST.TOP_RATED;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String RECYCLER_STATE_KEY = "recycler_position";

    private ActivityMainBinding mLayout;
    private ContentMainBinding mContentLayout;

    private PostersAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    private Toast mToast;
    private int mCurrentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayout = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mLayout.toolbar);

        //TODO : Find A Better way to do this
        mContentLayout = mLayout.contentLayout;



        mAdapter = new PostersAdapter(this, this::openDetailsActivity);
        mLayoutManager = new GridLayoutManager(this, calculateNoOfColumns(this));

        mContentLayout.rvMovies.setLayoutManager(mLayoutManager);
        mContentLayout.rvMovies.setAdapter(mAdapter);
        mContentLayout.rvMovies.setHasFixedSize(true);

        getMovies();
        mContentLayout.swipeRefresh.setOnRefreshListener(this);
    }

    private void openDetailsActivity(Movie movie) {
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra(EXTRA_MOVIE_ID, movie.getId());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        showProgressPar();
        switch (id) {
            case R.id.action_popular_movies:
                getPopular();
                return true;
            case R.id.action_top_rated:
                getTopRated();
                return true;
            case R.id.action_favorites_movies:
                getFavoritesMovies();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getPopular() {
        saveCategoryAs(POPULAR);
        getSupportActionBar().setTitle(R.string.title_populer_movies);

        mApiService.getPopularMovies(null, null, null).enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {

                final MoviesList popularMoviesList = response.body();
                mAdapter.clear();
                mAdapter.addAll(popularMoviesList.getMovieList());
                ((AMApplication) getApplication()).getMyDatabase().movieDao().insert(popularMoviesList.getMovieList());
                hideProgressPar();
                mContentLayout.rvMovies.setVisibility(View.VISIBLE);
                mContentLayout.progressBar.setVisibility(View.GONE);
                mContentLayout.rvMovies.scrollToPosition(mCurrentPosition);
                hideRefreshBar();
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                Logger.e("Failed to Fetch Popular Movies",t);
            }
        });
    }

    private void getTopRated() {
        saveCategoryAs(TOP_RATED);
        getSupportActionBar().setTitle(R.string.title_top_rated_movies);
        mApiService.getTopRatedMovies(null, null, null).enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                final MoviesList popularMoviesList = response.body();
                mAdapter.clear();
                mAdapter.addAll(popularMoviesList.getMovieList());
                hideProgressPar();
                mContentLayout.rvMovies.setVisibility(View.VISIBLE);
                hideRefreshBar();
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                Logger.e("Failed to Fetch Top Rated Movies", t);            }
        });
    }

    public void getFavoritesMovies() {
        saveCategoryAs(FAVORITES);
        getSupportActionBar().setTitle(R.string.title_favorite_movies);

    }

    public void saveCategoryAs(String category) {
        getPref().saveMenuSelection(category);
    }

    public String getCategory() {
        return getPref().getCategory();
    }

    private void getMovies() {
        switch (getCategory()) {
            case POPULAR:
                getPopular();
                break;
            case TOP_RATED:
                getTopRated();
                break;
            case FAVORITES:

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int scrollPosition = ((GridLayoutManager) (mContentLayout.rvMovies.getLayoutManager())).findFirstCompletelyVisibleItemPosition();
        outState.putInt(RECYCLER_STATE_KEY, scrollPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(RECYCLER_STATE_KEY, 0);
        }
    }

    @SuppressLint("ShowToast")
    @Override
    public void onRefresh() {
        if (mToast == null) {
            mToast = Toast.makeText(MainActivity.this, "Count " + 0, Toast.LENGTH_LONG);
        }
        mToast.setText("Movies List Updated");
        mToast.show();
        getMovies();
    }

    private void hideRefreshBar() {
        mContentLayout.swipeRefresh.setRefreshing(false);
    }

    private void hideProgressPar() {
        mContentLayout.progressBar.setVisibility(View.GONE);
    }

    private void showProgressPar() {
        mContentLayout.progressBar.setVisibility(View.VISIBLE);
    }

}
