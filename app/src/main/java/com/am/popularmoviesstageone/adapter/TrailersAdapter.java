package com.am.popularmoviesstageone.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.am.popularmoviesstageone.data.model.Trailer;
import com.am.popularmoviesstageone.databinding.ItemTrailerBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.am.popularmoviesstageone.util.CONST.BASE_TRAILERS_URL;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerHolder> {

    private Context mContext;
    private List<Trailer> mTrailerList;
    private LayoutInflater mInflater;
    private OnItemClickListener mListener;

    public TrailersAdapter(Context context, OnItemClickListener listener) {
        this.mContext = context;
        this.mListener = listener;
        this.mTrailerList = new ArrayList<>();
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @NonNull
    @Override
    public TrailerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTrailerBinding binding = ItemTrailerBinding.inflate(mInflater, parent, false);
        return new TrailerHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerHolder holder, int position) {
        Trailer trailer = mTrailerList.get(position);
        holder.bindData(trailer);
    }

    @Override
    public int getItemCount() {
        return mTrailerList == null ? 0 : mTrailerList.size();
    }

    public void add(Trailer item) {
        mTrailerList.add(item);
        notifyItemInserted(mTrailerList.size() - 1);
    }

    public void addAll(List<Trailer> appendedItemList) {
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


        private ItemTrailerBinding mBinding;
        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClicked(mTrailerList.get(getAdapterPosition()));
            }
        };

        public TrailerHolder(ItemTrailerBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
            mBinding.getRoot().setOnClickListener(onClickListener);
            mBinding.trailerThumpImageView.setOnClickListener(onClickListener);
            mBinding.playIconImageVIew.setOnClickListener(onClickListener);
            mBinding.trailerTitleTextVIew.setOnClickListener(onClickListener);
        }

        @SuppressLint("SetTextI18n")
        private void bindData(Trailer trailer) {
            mBinding.trailerTitleTextVIew.setText(trailer.getName());
            Glide.with(mContext).load(BASE_TRAILERS_URL.replace("VIDEO_ID", trailer.getKey())).into(mBinding.trailerThumpImageView);
            mBinding.executePendingBindings();

        }
    }

    public interface OnItemClickListener {
        void onItemClicked(Trailer trailer);
    }

}
