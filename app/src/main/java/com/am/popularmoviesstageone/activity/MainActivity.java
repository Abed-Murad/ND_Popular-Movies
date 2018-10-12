package com.am.popularmoviesstageone.activity;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.am.popularmoviesstageone.R;
import com.am.popularmoviesstageone.adapter.MoviesPostersAdapter;
import com.am.popularmoviesstageone.databinding.ActivityMainBinding;
import com.am.popularmoviesstageone.databinding.ContentMainBinding;
import com.am.popularmoviesstageone.model.MoviesList;
import com.am.popularmoviesstageone.room.FavMovieEntity;
import com.am.popularmoviesstageone.room.MoviesDatabase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.am.popularmoviesstageone.util.CONST.EXTRA_MOVIE_ID;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String RECYCLER_STATE_KEY = "recycler_position";

    private final String POPULAR = "most_popular";
    private final String TOP_RATED = "top_rated";
    private final String FAVORITES = "favorites";

    private ActivityMainBinding mLayout;
    private ContentMainBinding mContentLayout;

    private MoviesPostersAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    private Toast mToast;
    private Parcelable mRecyclerState;

    private int mToastCount = 0;
    private int mCurrentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayout = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //TODO : Find A Better way to do this
        mContentLayout = mLayout.contentLayout;

        setSupportActionBar(mLayout.toolbar);

        mAdapter = new MoviesPostersAdapter(this, movie -> {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            intent.putExtra(EXTRA_MOVIE_ID, movie.getId());
            startActivity(intent);
        });
        mLayoutManager = new GridLayoutManager(this, calculateNoOfColumns(this));

        mContentLayout.rvMovies.setLayoutManager(mLayoutManager);
        mContentLayout.rvMovies.setAdapter(mAdapter);
        mContentLayout.rvMovies.setHasFixedSize(true);

        getPopularMovies();
        mContentLayout.swipeRefresh.setOnRefreshListener(this);
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

    @Override
    protected void onResume() {
        super.onResume();
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
                getPopularMovies();
                saveMenuSelection(POPULAR);
                getSupportActionBar().setTitle("Popular Movies");
                return true;
            case R.id.action_top_rated:
                getTopRatedMovies();
                saveMenuSelection(TOP_RATED);
                getSupportActionBar().setTitle("Top Rated Movies");
                return true;
            case R.id.action_favorites_movies:
                getFavoritesMovies();
                saveMenuSelection(FAVORITES);
                getSupportActionBar().setTitle("Favorite Movies");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getPopularMovies() {
        mApiService.getPopularMovies(null, null, null).enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                final MoviesList popularMoviesList = response.body();
                mAdapter.clear();
                mAdapter.addAll(popularMoviesList.getMovieList());
                hideProgressPar();
                mContentLayout.rvMovies.setVisibility(View.VISIBLE);
                mContentLayout.rvMovies.scrollToPosition(mCurrentPosition);
            }
            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void hideProgressPar() {
        mContentLayout.progressBar.setVisibility(View.GONE);
    }

    private void showProgressPar() {
        mContentLayout.progressBar.setVisibility(View.VISIBLE);
    }

    private void getTopRatedMovies() {
        mApiService.getTopRatedMovies(null, null, null).enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                final MoviesList popularMoviesList = response.body();
                mAdapter.clear();
                mAdapter.addAll(popularMoviesList.getMovieList());
                hideProgressPar();
                mContentLayout.rvMovies.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    public void getFavoritesMovies() {
        LiveData<List<FavMovieEntity>> moviesList = MoviesDatabase.getsInstance(this).favMovieDao().loadAllMovies();
        moviesList.observe(this, favMovieEntities -> {
            mAdapter.clear();
            mAdapter.addAllFav(favMovieEntities);
            hideProgressPar();
            mContentLayout.rvMovies.setVisibility(View.VISIBLE);
        });

    }

    public void saveMenuSelection(String newMenuSelection) {
        getPref().saveMenuSelection(newMenuSelection);
    }

    @Override
    public void onRefresh() {
        if (mToast == null) {
            mToast = Toast.makeText(MainActivity.this, "Count " + 0, Toast.LENGTH_LONG);
        }
        mToast.setText("Count " + mToastCount++);
        mToast.show();
        mContentLayout.swipeRefresh.setRefreshing(false);
    }
}
