package com.am.popularmoviesstageone.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

//This class is where all methods to access the database are defined
@Dao
public interface FavMoviesDao {
    @Query("SELECT * FROM favorites_table")
    LiveData<List<FavMovieEntity>> loadAllMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(FavMovieEntity movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(FavMovieEntity movie);

    @Delete
    void deleteMovie(FavMovieEntity movie);

    //Query to delete movie from database with that movie ID
    @Query("DELETE FROM favorites_table WHERE id = :favoriteMovieId")
    void deleteByFavoriteById(int favoriteMovieId);


    @Query("SELECT * FROM favorites_table WHERE id == :id")
    FavMovieEntity loadMovieById(int id);

}
