package com.am.popularmoviesstageone.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.am.popularmoviesstageone.R;
import com.am.popularmoviesstageone.room.FavMovieEntity;
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
    private OnItemClickListener onItemClickListener;

    public MoviesPostersAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.movieList = new ArrayList<>();
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_poster, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(movieList.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final String TAG = ViewHolder.class.getSimpleName();
        @BindView(R.id.iv_movie_poster)
        ImageView moviePosterImageView;
        @BindView(R.id.tv_movie_name)
        TextView movieNameTextView;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        private void bind(Movie movie, OnItemClickListener onItemClickListener) {
            String posterUrl = BASE_POSTERS_URL + movie.getPosterPath();
            String movieName =   movie.getOriginalTitle();
            Glide.with(context).load(posterUrl).into(moviePosterImageView);
            itemView.setOnClickListener(view -> {
                onItemClickListener.onItemClick(movie);
            });
            movieNameTextView.setText(movieName);


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

    public void addAllFav(List<FavMovieEntity> favMovieEntities) {
        for (FavMovieEntity favMovieEntity : favMovieEntities) {
            add(new Movie(favMovieEntity.getMovieId(), favMovieEntity.getMovieName(), favMovieEntity.getMoviePoster()));
        }
    }




    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }


}
