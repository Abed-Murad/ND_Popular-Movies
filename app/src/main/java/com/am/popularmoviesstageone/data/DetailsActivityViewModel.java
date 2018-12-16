package com.am.popularmoviesstageone.data;

import android.app.Application;

import com.am.popularmoviesstageone.data.model.Movie;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class DetailsActivityViewModel extends AndroidViewModel {
    private Repository mRepository;
    public DetailsActivityViewModel(@NonNull Application application) {
        super(application);
        mRepository = new Repository(application);
    }

    public void insert(Movie movie) {
        mRepository.insert(movie);
    }

    public void delete(Movie mMovie) {
        mRepository.delete(mMovie);
    }
    public void update(Movie movie) {
        mRepository.update(movie);
    }

}
