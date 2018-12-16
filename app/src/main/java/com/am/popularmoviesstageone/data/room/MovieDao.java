package com.am.popularmoviesstageone.data.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

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
    List<Long> insertAll(List<Movie> movieList);

    @Update
    void update(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("DELETE FROM Movie")
    void deleteAll();

}
