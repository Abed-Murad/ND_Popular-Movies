package com.am.popularmoviesstageone.util;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.res.Configuration;

import com.am.popularmoviesstageone.data.room.MoviesDatabase;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

public class AMApplication extends Application {
    MoviesDatabase mMoviesDatabase;

    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!

    @Override
    public void onCreate() {
        super.onCreate();

        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .tag("am_logs")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

        mMoviesDatabase = Room.databaseBuilder(this, MoviesDatabase.class, MoviesDatabase.NAME)
                .fallbackToDestructiveMigration()
                //ToDo: Remove This
                .allowMainThreadQueries()
                .build();



    }
    public MoviesDatabase getMyDatabase() {
        return mMoviesDatabase;
    }



    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


}
