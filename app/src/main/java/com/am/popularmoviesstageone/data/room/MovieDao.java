package com.am.popularmoviesstageone.data.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.am.popularmoviesstageone.data.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM Movie WHERE id = :movieId")
    Movie getById(int movieId);

    @Query("SELECT * FROM Movie")
    LiveData<List<Movie>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Movie movie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<Movie> movieList);

    @Delete
    void delete(Movie movie);

}
