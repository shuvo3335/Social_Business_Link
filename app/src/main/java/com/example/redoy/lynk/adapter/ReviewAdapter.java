package com.example.redoy.lynk.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.redoy.lynk.R;
import com.example.redoy.lynk.model.ProfileReviewData;
import com.example.redoy.lynk.model.ProfileReviewResponse;
import com.example.redoy.lynk.model.ReviewItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private List<ReviewItem> reviewItems;
    private Context context;

    public ReviewAdapter(Context context, List<ReviewItem> reviewItems) {
        this.context = context;
        this.reviewItems = reviewItems;
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_review, parent, false);
        return new ReviewAdapter.ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        holder.mUserNameTextView.setText(reviewItems.get(position).getName());
        holder.mTimeStampTextView.setText(reviewItems.get(position).getTimestamp().replace(".000000",""));
        holder.mDescriptionTextView.setText(reviewItems.get(position).getDescription());
        holder.mRatingBar.setRating(Float.parseFloat(reviewItems.get(position).getRatedByUser()));
    }

    @Override
    public int getItemCount() {
        return reviewItems == null ? 0 : reviewItems.size();
    }

    public static class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_name_TextView)
        TextView mUserNameTextView;

        @BindView(R.id.timestamp_TextView)
        TextView mTimeStampTextView;

        @BindView(R.id.description_TextView)
        TextView mDescriptionTextView;

        @BindView(R.id.ratingBar)
        RatingBar mRatingBar;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}