package com.am.popularmoviesstageone.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.am.popularmoviesstageone.R;
import com.am.popularmoviesstageone.model.MovieTrailerEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesTrailersAdapter extends RecyclerView.Adapter<MoviesTrailersAdapter.TrailerHolder> {

    private Context mContext;
    private List<MovieTrailerEntity> mTrailerList;
    private LayoutInflater mInflater;

    public MoviesTrailersAdapter(Context mContext) {
        this.mContext = mContext;
        mTrailerList = new ArrayList<>();
    }

    @NonNull
    @Override
    public TrailerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.item_trailer, parent, false);
        return new TrailerHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mTrailerList == null ? 0 : mTrailerList.size();
    }

    public void add(MovieTrailerEntity item) {
        mTrailerList.add(item);
        notifyItemInserted(mTrailerList.size() - 1);
    }

    public void addAll(List<MovieTrailerEntity> appendedItemList) {
        if (appendedItemList == null || appendedItemList.size() <= 0) {
            return;
        }
        if (this.mTrailerList == null) {
            this.mTrailerList = new ArrayList<>();
        }
        this.mTrailerList.addAll(appendedItemList);
        notifyDataSetChanged();
    }

    public class TrailerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_play_icon)
        ImageView mPlayItemImageView;
        @BindView(R.id.tv_trailer_name)
        TextView mTrailerNameTextView;
        @BindView(R.id.tv_trailer_language)
        TextView mTrailerLanguageTextView;

        public TrailerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        private void bindData(MovieTrailerEntity trailer) {

        }
    }


}
