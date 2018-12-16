package com.am.popularmoviesstageone.data.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.am.popularmoviesstageone.data.model.Movie;
import com.am.popularmoviesstageone.data.model.Review;
import com.am.popularmoviesstageone.data.model.Trailer;

/*
 * A Good practice is to Create a Dao For Each @Entity
 * */
@Database(entities = {Movie.class, Review.class, Trailer.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class MoviesDatabase extends RoomDatabase {

    public static final String NAME = "movies_database";
    private static MoviesDatabase instance;
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

        }
    };

    public static synchronized MoviesDatabase getInstance(Context context) {
        if (instance != null) {

            instance = Room.databaseBuilder(context.getApplicationContext(), MoviesDatabase.class, MoviesDatabase.NAME)
                    .fallbackToDestructiveMigration()
                    //ToDo: Remove This
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_1_2)
                    .build();

        }
        return instance;
    }

    public abstract MovieDao movieDao();

}
