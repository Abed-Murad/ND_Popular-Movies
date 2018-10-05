package com.am.popularmoviesstageone.room;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class FavMoviesViewModel extends AndroidViewModel {

    private Repository mRepository;
    private LiveData<List<FavMovieEntity>> favMoviesList;

    public FavMoviesViewModel(@NonNull Application application) {
        super(application);
        mRepository = new Repository(application);
        favMoviesList = mRepository.getmMoviesList();

    }

    public LiveData<List<FavMovieEntity>> getFavMoviesList() {
        return favMoviesList;
    }

    public void insert(FavMovieEntity movie) {
        mRepository.insert(movie);

    }
}
