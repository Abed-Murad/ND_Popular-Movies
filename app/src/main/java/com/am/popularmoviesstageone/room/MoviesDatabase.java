package com.am.popularmoviesstageone.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

//Has to set exportSchema to false or it will throw an exception
@Database(entities = {FavMovieEntity.class}, version = 3, exportSchema = false)
public abstract class MoviesDatabase extends RoomDatabase {

    private static final String TAG = MoviesDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favorites_database";

    private static MoviesDatabase sInstance;

    public static MoviesDatabase getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating New Database Instance");
                // Will Create the database if not-exists and get a referecne to it
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MoviesDatabase.class,
                        MoviesDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(TAG, "Getting the database instance");
        return sInstance;
    }


    public abstract FavMoviesDao favMovieDao();
}
