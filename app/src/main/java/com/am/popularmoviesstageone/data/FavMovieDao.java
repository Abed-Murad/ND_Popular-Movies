package com.am.popularmoviesstageone.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;


@Dao
public interface FavMovieDao {
    @Query("SELECT * FROM movies")
    List<FavMovieEntity> loadAllMovies();

    @Insert
    void insertMovie(FavMovieEntity movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(FavMovieEntity movie);

    @Delete
    void deleteMovie(FavMovieEntity movie);

}
