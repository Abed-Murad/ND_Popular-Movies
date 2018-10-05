package com.am.popularmoviesstageone.room;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.os.DeadObjectException;
import android.view.ViewOutlineProvider;

import java.util.List;

public class Repository {
    private FavMoviesDao mMoviesDao;

    private LiveData<List<FavMovieEntity>> mMoviesList;

    public Repository(Application application) {
        MoviesDatabase db = MoviesDatabase.getsInstance(application.getApplicationContext());
        mMoviesDao = db.favMovieDao();
        mMoviesList = mMoviesDao.loadAllMovies();
    }

    public LiveData<List<FavMovieEntity>> getmMoviesList() {
        return mMoviesList;
    }

    public void insert(FavMovieEntity movie) {
        new InsertMovieAsyncTask(mMoviesDao).execute(movie);
    }

//    public boolean loadMovieById(int movieId) {
//       return new LoadMovieByIdAsyncTask(mMoviesDao).execute(movieId);
//
//    }

    private static class InsertMovieAsyncTask extends AsyncTask<FavMovieEntity, FavMovieEntity, Void> {
        FavMoviesDao mMoviesDao;

        public InsertMovieAsyncTask(FavMoviesDao moviesDao) {
            this.mMoviesDao = moviesDao;
        }

        @Override
        protected Void doInBackground(FavMovieEntity... favMovieEntities) {
            mMoviesDao.insertMovie(favMovieEntities[0]);
            return null;
        }

    }

    private static class LoadMovieByIdAsyncTask extends AsyncTask<Integer, Void, FavMovieEntity> {
        private FavMoviesDao mMoviesDao;

        public LoadMovieByIdAsyncTask(FavMoviesDao moviesDao) {
            this.mMoviesDao = moviesDao;
        }


        @Override
        protected FavMovieEntity doInBackground(Integer... integers) {
            FavMovieEntity favMovieEntity = mMoviesDao.loadMovieById(integers[0]);

            return favMovieEntity;
        }
    }
}
