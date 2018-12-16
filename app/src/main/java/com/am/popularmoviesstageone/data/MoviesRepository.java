package com.am.popularmoviesstageone.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.am.popularmoviesstageone.data.model.Movie;
import com.am.popularmoviesstageone.data.room.MovieDao;
import com.am.popularmoviesstageone.data.room.MoviesDatabase;

import java.util.List;

/**
 * This class is a layer between the modelview and the room database or the web api so
 * the model view dent have to deal with the network or sql for getting the data he want
 */
public class MoviesRepository {
    private MovieDao mMovieDao;
    private LiveData<List<Movie>> mFavMoviesList;

    //Use the application to get context for creating the database
    public MoviesRepository(Application application) {
        MoviesDatabase database = MoviesDatabase.getInstance(application);
        mMovieDao = database.movieDao();
        mFavMoviesList = mMovieDao.getAll();
    }

    public void insert(Movie movie) {

    }
    public void update(Movie movie) {

    }
    public void delete(Movie movie) {

    }
    public void deleteAll(Movie movie) {

    }

    public MoviesRepository(LiveData<List<Movie>> mFavMoviesList) {
        this.mFavMoviesList = mFavMoviesList;
    }
}
