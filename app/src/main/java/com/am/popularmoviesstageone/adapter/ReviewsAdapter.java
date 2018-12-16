package com.am.popularmoviesstageone.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.am.popularmoviesstageone.R;
import com.am.popularmoviesstageone.data.model.Review;
import com.am.popularmoviesstageone.databinding.ItemReviewBinding;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.am.popularmoviesstageone.util.IntentsUtil.openUrlInChromeCustomTab;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.TrailerHolder> {


    private Context mContext;
    private List<Review> mReviewList;
    private LayoutInflater mInflater;

    public ReviewsAdapter(Context context) {
        this.mContext = context;
        this.mReviewList = new ArrayList<>();
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @NonNull
    @Override
    public TrailerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemReviewBinding binding = ItemReviewBinding.inflate(mInflater, parent, false);
        return new TrailerHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerHolder holder, int position) {
        Review review = mReviewList.get(position);
        holder.bindData(review);
    }

    @Override
    public int getItemCount() {
        return mReviewList == null ? 0 : mReviewList.size();
    }

    public void add(Review item) {
        mReviewList.add(item);
        notifyItemInserted(mReviewList.size() - 1);
    }

    public void addAll(List<Review> appendedItemList) {
        if (appendedItemList == null || appendedItemList.size() <= 0) {
            return;
        }
        if (this.mReviewList == null) {
            this.mReviewList = new ArrayList<>();
        }
        this.mReviewList.addAll(appendedItemList);
        notifyDataSetChanged();
    }

    public class TrailerHolder extends RecyclerView.ViewHolder {

        private ItemReviewBinding mBinding;

        public TrailerHolder(ItemReviewBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }


        // Reviewer : What does  @SuppressLint("SetTextI18n") mean ?
        @SuppressLint("SetTextI18n")
        private void bindData(Review review) {
            mBinding.reviewBodyTextView.setText(review.getContent());
            mBinding.reviewAuthorTextView.setText("Written by @" + review.getAuthor().replaceAll("\\s+", ""));
            mBinding.getRoot().setOnClickListener(v -> openUrlInChromeCustomTab(mContext, review.getUrl()));

        }
    }


}
