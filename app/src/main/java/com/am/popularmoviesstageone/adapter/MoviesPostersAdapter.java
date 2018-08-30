package com.am.popularmoviesstageone.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.am.popularmoviesstageone.R;
import com.am.popularmoviesstageone.activity.MovieDetailsActivity;
import com.am.popularmoviesstageone.model.Movie;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.am.popularmoviesstageone.util.CONST.BASE_POSTERS_URL;

public class MoviesPostersAdapter extends RecyclerView.Adapter<MoviesPostersAdapter.ViewHolder> {

    private List<Movie> movieList;
    private Context context;

    public MoviesPostersAdapter(Context context) {
        this.context = context;
        this.movieList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_poster, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final String TAG = ViewHolder.class.getSimpleName();
        @BindView(R.id.iv_movie_poster)
        ImageView moviePosterImageView;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra(MOVIE_PARCELABLE_KEY, movieList.get(getAdapterPosition()));
                context.startActivity(intent);
            });
        }

        private void bind() {
            String posterUrl = BASE_POSTERS_URL + movieList.get(getAdapterPosition()).getPosterPath();
            Glide.with(context).load(posterUrl).into(moviePosterImageView);
        }
    }

    public void add(Movie movie) {
        movieList.add(movie);
        notifyItemInserted(movieList.size() - 1);
    }

    public void clear() {
        movieList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Movie> movieList) {
        if (movieList != null) {
            for (Movie movie : movieList) {
                add(movie);
            }
        }
    }

}
