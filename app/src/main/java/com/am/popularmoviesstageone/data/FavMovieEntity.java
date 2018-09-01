package com.am.popularmoviesstageone.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "movies")
public class FavMovieEntity {
    @PrimaryKey
    private int movieId;
    private String movieName;

    public FavMovieEntity(int movieId, String movieName) {
        this.movieId = movieId;
        this.movieName = movieName;
    }
}
