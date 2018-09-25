package com.am.popularmoviesstageone.room;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class FavMoviesModelView extends AndroidViewModel {

    private LiveData<List<FavMovieEntity>> favMoviesList;

    public FavMoviesModelView(@NonNull Application application) {
        super(application);
        MoviesDatabase database = MoviesDatabase.getsInstance(this.getApplication());
    }

    public LiveData<List<FavMovieEntity>> getFavMoviesList() {
        return favMoviesList;
    }
}
