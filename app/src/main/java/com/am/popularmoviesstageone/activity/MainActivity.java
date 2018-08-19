package com.am.popularmoviesstageone.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.am.popularmoviesstageone.R;
import com.am.popularmoviesstageone.adapter.MoviesPostersAdapter;
import com.am.popularmoviesstageone.model.MoviesList;
import com.am.popularmoviesstageone.network.APIClient;
import com.am.popularmoviesstageone.network.ApiRequests;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    ApiRequests apiService = APIClient.getClient().create(ApiRequests.class);

    @BindView(R.id.rv_movies)
    RecyclerView moviesPostersRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    MoviesPostersAdapter adapter;
    private boolean isPopularMoviesShowed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        adapter = new MoviesPostersAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        moviesPostersRecyclerView.setHasFixedSize(true);
        moviesPostersRecyclerView.setAdapter(adapter);
        moviesPostersRecyclerView.setLayoutManager(layoutManager);
        getPopularMovies();
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
            case R.id.action_change_type:

                if (isPopularMoviesShowed) {
                    getTopRatedMovies();
                } else {
                    getPopularMovies();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void getPopularMovies() {
        apiService.getPopularMovies(null, null, null).enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                final MoviesList popularMoviesList = response.body();
                adapter.clear();
                adapter.addAll(popularMoviesList.getMovieList());
                isPopularMoviesShowed = true;
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void getTopRatedMovies() {
        apiService.getTopRatedMovies(null, null, null).enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                final MoviesList popularMoviesList = response.body();
                adapter.clear();
                adapter.addAll(popularMoviesList.getMovieList());
                isPopularMoviesShowed = false;
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

}
