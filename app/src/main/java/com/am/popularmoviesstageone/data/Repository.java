package com.am.popularmoviesstageone.data;

import android.app.Application;
import android.os.AsyncTask;

import com.am.popularmoviesstageone.data.model.Movie;
import com.am.popularmoviesstageone.data.room.MovieDao;
import com.am.popularmoviesstageone.data.room.MoviesDatabase;
import com.am.popularmoviesstageone.util.AMApplication;

import java.util.List;

import androidx.lifecycle.LiveData;

/**
 * This class is a layer between the modelview and the room database or the web api so
 * the model view dent have to deal with the network or sql for getting the data he want
 */
public class Repository {
    private MovieDao mMovieDao;
    private LiveData<List<Movie>> mFavMoviesList;

    //Use the application to get context for creating the database
    public Repository(Application application) {
//        MoviesDatabase database = MoviesDatabase.getInstance(application);
        MoviesDatabase database = ((AMApplication) application).getMyDatabase();
        mMovieDao = database.movieDao();
        mFavMoviesList = mMovieDao.getAll();
    }

    /*
    Those Are The methods that the ViewModel Will See
     */
    public void insert(Movie movie) {
        new InsertAsyncTask(mMovieDao).execute(movie);
    }
    public void update(Movie movie) {
        new UpdateAsyncTask(mMovieDao).execute(movie);

    }
    public void delete(Movie movie) {
        new DeleteAsyncTask(mMovieDao).execute(movie);

    }
    public void deleteAll() {
        new DeleteAllAsyncTask(mMovieDao).execute();

    }
   /*
    Those Are The methods that the ViewModel Will See
   */

    public LiveData<List<Movie>> getFavMoviesList() {
        return mFavMoviesList;
    }

    // It's static to not have a reference to the Repository class , otherwise it will cause a memory leak
    private static class InsertAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao mMovieDao;

        private InsertAsyncTask(MovieDao movieDao) {
            this.mMovieDao = movieDao;
        }
        @Override
        protected Void doInBackground(Movie... movies) {
            mMovieDao.insert(movies[0]);
            return null;
        }
    }
    // It's static to not have a reference to the Repository class , otherwise it will cause a memory leak
    private static class UpdateAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao mMovieDao;

        private UpdateAsyncTask(MovieDao movieDao) {
            this.mMovieDao = movieDao;
        }
        @Override
        protected Void doInBackground(Movie... movies) {
            mMovieDao.update(movies[0]);
            return null;
        }
    }
    // It's static to not have a reference to the Repository class , otherwise it will cause a memory leak
    private static class DeleteAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao mMovieDao;

        private DeleteAsyncTask(MovieDao movieDao) {
            this.mMovieDao = movieDao;
        }
        @Override
        protected Void doInBackground(Movie... movies) {
            mMovieDao.delete(movies[0]);
            return null;
        }
    }
    // It's static to not have a reference to the Repository class , otherwise it will cause a memory leak
    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private MovieDao mMovieDao;

        private DeleteAllAsyncTask(MovieDao movieDao) {
            this.mMovieDao = movieDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            mMovieDao.deleteAll();
            return null;
        }
    }


}
