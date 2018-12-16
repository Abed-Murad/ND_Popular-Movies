package com.am.popularmoviesstageone.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.am.popularmoviesstageone.databinding.ItemMoviePosterBinding;

import com.am.popularmoviesstageone.data.model.Movie;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.am.popularmoviesstageone.util.CONST.BASE_POSTERS_URL;

public class PostersAdapter extends RecyclerView.Adapter<PostersAdapter.ViewHolder> {

    private List<Movie> movieList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private LayoutInflater mInflater;


    public PostersAdapter(Context context, OnItemClickListener listener) {
        this.mContext = context;
        this.movieList = new ArrayList<>();
        this.onItemClickListener = listener;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMoviePosterBinding binding = ItemMoviePosterBinding.inflate(mInflater, parent, false);
        return new ViewHolder(binding);
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
        private ItemMoviePosterBinding mBinding;
        public ViewHolder(ItemMoviePosterBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }

        private void bind(Movie movie, OnItemClickListener onItemClickListener) {
            String posterUrl = BASE_POSTERS_URL + movie.getPosterPath();
            String movieName = movie.getOriginalTitle();
            Glide.with(mContext).load(posterUrl).into(mBinding.moviePosterImageVIew);
            itemView.setOnClickListener(view -> {
                onItemClickListener.onItemClick(movie);
            });
            mBinding.movieNameTextView.setText(movieName);
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

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }


}
