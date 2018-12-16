package com.am.popularmoviesstageone.data;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.annotation.NonNull;

import com.am.popularmoviesstageone.data.model.Movie;

public class DetailsActivityViewModel extends AndroidViewModel {
    private MoviesRepository mRepository;
    public DetailsActivityViewModel(@NonNull Application application) {
        super(application);
        mRepository = new MoviesRepository(application);
    }

    public void insert(Movie movie) {
        mRepository.insert(movie);
    }

    public void delete(Movie mMovie) {
        mRepository.delete(mMovie);
    }
}
