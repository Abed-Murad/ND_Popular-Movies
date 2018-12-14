package com.am.popularmoviesstageone.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.am.popularmoviesstageone.model.Movie;

@Database(entities = {Movie.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class MoviesDatabase extends RoomDatabase {
    public static final String NAME = "movies_database";

    public abstract MovieDao movieDao();
}
