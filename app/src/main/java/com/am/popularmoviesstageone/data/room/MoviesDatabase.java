package com.am.popularmoviesstageone.data.room;

import android.app.Application;
import android.os.AsyncTask;

import com.am.popularmoviesstageone.data.model.Movie;
import com.am.popularmoviesstageone.data.model.Review;
import com.am.popularmoviesstageone.data.model.Trailer;
import com.am.popularmoviesstageone.util.AMApplication;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

/*
 * A Good practice is to Create a Dao For Each @Entity
 * */
@Database(entities = {Movie.class, Review.class, Trailer.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class MoviesDatabase extends RoomDatabase {

    public static final String NAME = "movies_database";
    private static MoviesDatabase instance = null;

    public abstract MovieDao movieDao();

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

        }
    };

    public static  MoviesDatabase getInstance(Application application) {

        if (instance != null) {
            instance = Room.databaseBuilder(((AMApplication)application), MoviesDatabase.class, MoviesDatabase.NAME)
                    .fallbackToDestructiveMigration()
//                    .addCallback(mRoomCallback)
                    .addMigrations(MIGRATION_1_2)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }


    //TODO: Find Why this callback doesnot work
    private static RoomDatabase.Callback mRoomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAsyncTask(instance).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateAsyncTask(instance).execute();
        }
    };

    private static class PopulateAsyncTask extends AsyncTask<Void, Void, Void> {
        private MovieDao mMovieDao;
        public PopulateAsyncTask(MoviesDatabase database) {
            this.mMovieDao = database.movieDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mMovieDao.insert(new Movie(238, "The Godfather", "/rPdtLWNsZmAtoZl9PK7S2wE3qiS.jpg"));
            mMovieDao.insert(new Movie(424, "Schindler's List", "/yPisjyLweCl1tbgwgtzBCNCBle.jpg"));
            mMovieDao.insert(new Movie(372058, "Your Name.", "/xq1Ugd62d23K2knRUx6xxuALTZB.jpg"));
            return null;
        }
    }

}
