package com.am.popularmoviesstageone.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.am.popularmoviesstageone.R;
import com.am.popularmoviesstageone.adapter.MoviesPostersAdapter;
import com.am.popularmoviesstageone.data.FavMovieEntity;
import com.am.popularmoviesstageone.data.MoviesDatabase;
import com.am.popularmoviesstageone.model.MoviesList;
import com.am.popularmoviesstageone.network.APIClient;
import com.am.popularmoviesstageone.network.ApiRequests;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.am.popularmoviesstageone.util.CONST.EXTRA_MOVIE;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SCROLL_POSITION_KEY = "scroll_position";

    final String POPULAR = "most_popular";
    final String TOP_RATED = "top_rated";
    final String FAVORITES = "favorites";

    ApiRequests mApiService = APIClient.getClient().create(ApiRequests.class);

    @BindView(R.id.rv_movies)
    RecyclerView mMoviesPostersRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    MoviesPostersAdapter mAdapter;
    GridLayoutManager mLayoutManager;
    private Parcelable mSavedStateGridLayoutManager;

    private String currentMovieSelection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mAdapter = new MoviesPostersAdapter(this, movie -> {
            Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
            intent.putExtra(EXTRA_MOVIE, movie);
            startActivity(intent);
        });

        mLayoutManager = new GridLayoutManager(this, calculateNoOfColumns(this));
        mMoviesPostersRecyclerView.setHasFixedSize(true);
        mMoviesPostersRecyclerView.setAdapter(mAdapter);
        mMoviesPostersRecyclerView.setLayoutManager(mLayoutManager);

        currentMovieSelection = getMenuSelection();

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
                if (mSavedStateGridLayoutManager != null){
                    mLayoutManager.onRestoreInstanceState(mSavedStateGridLayoutManager);
                }
                break;
        }

        //Loading previous layout state
        if (savedInstanceState != null) {
            mSavedStateGridLayoutManager = savedInstanceState.getParcelable(SCROLL_POSITION_KEY);
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
        switch (id) {
            case R.id.action_popular_movies:
                getPopularMovies();
                saveMenuSelection(POPULAR);
                return true;
            case R.id.action_top_rated:
                getTopRatedMovies();
                saveMenuSelection(TOP_RATED);
                return true;
            case R.id.action_favorites_movies:
                getFavoritesMovies();
                saveMenuSelection(FAVORITES);
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
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void getTopRatedMovies() {
        mApiService.getTopRatedMovies(null, null, null).enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                final MoviesList popularMoviesList = response.body();
                mAdapter.clear();
                mAdapter.addAll(popularMoviesList.getMovieList());
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
        Parcelable listState = mMoviesPostersRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(SCROLL_POSITION_KEY, listState);

    }

    //Saving state of Grid Layout (including scroll position)
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SCROLL_POSITION_KEY, mLayoutManager.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Parcelable listState = savedInstanceState.getParcelable(SCROLL_POSITION_KEY);
        mMoviesPostersRecyclerView.getLayoutManager().onRestoreInstanceState(listState);

    }

    //Will return the default Most Popular list upon initial load. Otherwise it will return the selected menu item.
    public String getMenuSelection() {
        return getPref().getMenuSelection();
    }

    public void saveMenuSelection(String newMenuSelection) {
        getPref().saveMenuSelection(newMenuSelection);
    }
}
