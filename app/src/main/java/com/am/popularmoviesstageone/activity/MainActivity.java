package com.am.popularmoviesstageone.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.am.popularmoviesstageone.R;
import com.am.popularmoviesstageone.adapter.MoviesPostersAdapter;
import com.am.popularmoviesstageone.databinding.ActivityMainBinding;
import com.am.popularmoviesstageone.databinding.ContentMainBinding;
import com.am.popularmoviesstageone.model.MoviesList;
import com.am.popularmoviesstageone.network.APIClient;
import com.am.popularmoviesstageone.network.ApiRequests;
import com.am.popularmoviesstageone.room.FavMovieEntity;
import com.am.popularmoviesstageone.room.FavMoviesViewModel;
import com.am.popularmoviesstageone.room.MoviesDatabase;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.am.popularmoviesstageone.util.CONST.EXTRA_MOVIE;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SCROLL_POSITION_KEY = "scroll_position";
    private final String POPULAR = "most_popular";
    private final String TOP_RATED = "top_rated";
    private final String FAVORITES = "favorites";

    private ActivityMainBinding mLayout;
    private ContentMainBinding mContentLayout;
    private MoviesPostersAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private Parcelable mSavedStateGridLayoutManager;
    private String currentMovieSelection;
    private FavMoviesViewModel mMoviesViewModel;
    private Toast mToast;

    private int mToastCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayout = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mContentLayout = mLayout.contentLayout;
        setSupportActionBar(mLayout.toolbar);
        mContentLayout.swipeRefresh.setOnRefreshListener(this);
        mAdapter = new MoviesPostersAdapter(this, movie -> {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            intent.putExtra(EXTRA_MOVIE, movie.getId());
            startActivity(intent);
        });
        mLayoutManager = new GridLayoutManager(this , 2);

        if (savedInstanceState != null) {
            mLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(SCROLL_POSITION_KEY));
        }

//        mLayoutManager = new GridLayoutManager(this, calculateNoOfColumns(this));
        mContentLayout.rvMovies.setHasFixedSize(true);
        mContentLayout.rvMovies.setAdapter(mAdapter);
        mContentLayout.rvMovies.setLayoutManager(mLayoutManager);

        currentMovieSelection = getMenuSelection();

        mMoviesViewModel = ViewModelProviders.of(this).get(FavMoviesViewModel.class);
        mMoviesViewModel.getFavMoviesList().observe(this, new Observer<List<FavMovieEntity>>() {
            @Override
            public void onChanged(@Nullable List<FavMovieEntity> favMovieEntities) {
                mAdapter.clear();
                mAdapter.addAllFav(favMovieEntities);
            }
        });


        //Loading previous layout state
//        if (savedInstanceState != null) {
//            mSavedStateGridLayoutManager = savedInstanceState.getParcelable(SCROLL_POSITION_KEY);
//        } else {

        switch (currentMovieSelection) {
            case POPULAR:
                getPopularMovies();
                break;
            case TOP_RATED:
                getTopRatedMovies();
                break;
            case FAVORITES:
                getFavoritesMovies();
                //Once data has loaded, load state of any previous layouts (including scroll position)
//                    if (mSavedStateGridLayoutManager != null) {
//                        mLayoutManager.onRestoreInstanceState(mSavedStateGridLayoutManager);
//                    }
                break;
        }

    }

    //    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save The List State
        mSavedStateGridLayoutManager = mLayoutManager.onSaveInstanceState();
        outState.putParcelable(SCROLL_POSITION_KEY, mSavedStateGridLayoutManager);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mSavedStateGridLayoutManager = savedInstanceState.getParcelable(SCROLL_POSITION_KEY);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSavedStateGridLayoutManager != null) {
            mLayoutManager.onRestoreInstanceState(mSavedStateGridLayoutManager);
        }
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
                //Once data has loaded, load state of any previous layouts (including scroll position)
                if (mSavedStateGridLayoutManager != null) {
                    mLayoutManager.onRestoreInstanceState(mSavedStateGridLayoutManager);
                }
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
        moviesList.observe(this, new Observer<List<FavMovieEntity>>() {
            @Override
            public void onChanged(@Nullable List<FavMovieEntity> favMovieEntities) {
                mAdapter.clear();
                mAdapter.addAllFav(favMovieEntities);
                hideProgressPar();
                mContentLayout.rvMovies.setVisibility(View.VISIBLE);
            }
        });

    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if (noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Parcelable listState = mContentLayout.rvMovies.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(SCROLL_POSITION_KEY, listState);
    }


    //Will return the default Most Popular list upon initial load. Otherwise it will return the selected menu item.
    public String getMenuSelection() {
        return getPref().getMenuSelection();
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
