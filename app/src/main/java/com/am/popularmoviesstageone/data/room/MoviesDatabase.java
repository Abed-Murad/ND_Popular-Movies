package com.am.popularmoviesstageone.data.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.am.popularmoviesstageone.data.model.Movie;
import com.am.popularmoviesstageone.data.model.Review;
import com.am.popularmoviesstageone.data.model.Trailer;

/*
 * A Good practice is to Create a Dao For Each @Entity
 * */
@Database(entities = {Movie.class, Review.class, Trailer.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class MoviesDatabase extends RoomDatabase {
    public static final String NAME = "movies_database";
    public abstract MovieDao movieDao();
}
