package com.am.popularmoviesstageone.room;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.am.popularmoviesstageone.model.Movie;

import java.util.List;

public interface MovieDao {

    @Query("SELECT * FROM Movie WHERE id := :movieId")
    Movie getById(int movieId);
    @Query("SELECT * FROM Movie")
    Movie getAll(int movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long inser(Movie movie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long inser(List<Movie> movieList);

    @Delete
    void delete(Movie movie);

}
