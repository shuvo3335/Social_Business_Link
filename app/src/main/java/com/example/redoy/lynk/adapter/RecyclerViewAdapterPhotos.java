package com.example.redoy.lynk.adapter;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.redoy.lynk.R;
import com.example.redoy.lynk.activity.PhotosActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapterPhotos extends RecyclerView.Adapter<RecyclerViewAdapterPhotos.RecyclerViewHolderPhotos> {

    private ArrayList<String> itemList;
    private Context context;

    public RecyclerViewAdapterPhotos(Context context, ArrayList<String> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewAdapterPhotos.RecyclerViewHolderPhotos onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list_photo, parent, false);
        RecyclerViewAdapterPhotos.RecyclerViewHolderPhotos rcv = new RecyclerViewAdapterPhotos.RecyclerViewHolderPhotos(layoutView, itemList, context);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterPhotos.RecyclerViewHolderPhotos holder, int position) {
        Picasso.get().load(itemList.get(position)).into(holder.itemPhoto);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public static class RecyclerViewHolderPhotos extends RecyclerView.ViewHolder {

        @BindView(R.id.list_item_imageView)
        public ImageView itemPhoto;

        public RecyclerViewHolderPhotos(final View itemView, final ArrayList<String> itemList, final Context context) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PhotosActivity.class);
                    intent.putStringArrayListExtra("images", itemList);
                    context.startActivity(intent);
                }
            });
        }
    }
}
