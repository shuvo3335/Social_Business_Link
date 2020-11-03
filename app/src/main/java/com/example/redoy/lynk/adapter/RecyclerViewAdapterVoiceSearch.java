package com.example.redoy.lynk.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redoy.lynk.R;
import com.example.redoy.lynk.activity.ProfileActivity;
import com.example.redoy.lynk.model.VoiceSearchItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by redoy.ahmed on 04-Mar-2018.
 */

public class RecyclerViewAdapterVoiceSearch extends RecyclerView.Adapter<RecyclerViewAdapterVoiceSearch.RecyclerViewHolderHome> {

    private ArrayList<VoiceSearchItem> itemList;
    private Context context;
    private static FragmentManager fragmentManager;

    public RecyclerViewAdapterVoiceSearch(Context context, ArrayList<VoiceSearchItem> itemList, FragmentManager fragmentManager) {
        this.itemList = itemList;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public RecyclerViewHolderHome onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list_voice_search, parent, false);
        RecyclerViewHolderHome rcv = new RecyclerViewHolderHome(layoutView, itemList, context);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolderHome holder, final int position) {
        holder.itemName.setText(itemList.get(position).getTitle());
        Picasso.get().load(itemList.get(position).getFeature_img()).into(holder.itemPhoto);
        holder.itemThanaName.setText(itemList.get(position).getThana());
        if(itemList.get(position).getThana().isEmpty()){
            holder.itemThanaName.setText("No Thana was added");
        }else {
            holder.itemThanaName.setText(itemList.get(position).getThana());
        }
        if(itemList.get(position).getPhoneNo().isEmpty()){
            holder.itemPhoneNo.setText("No Phone No was added");
        }else {
            holder.itemPhoneNo.setText(itemList.get(position).getPhoneNo());
        }
        holder.phoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", itemList.get(position).getPhoneNo(), null));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public static class RecyclerViewHolderHome extends RecyclerView.ViewHolder {

        @BindView(R.id.list_item_textView)
        public TextView itemName;

        @BindView(R.id.list_item_imageView)
        public ImageView itemPhoto;

        @BindView(R.id.list_item_thana_textView)
        public TextView itemThanaName;

        @BindView(R.id.list_item_phoneNo)
        public TextView itemPhoneNo;

        @BindView(R.id.imageViewPhone)
        public ImageView phoneIcon;

        @BindView(R.id.relativeLayout)
        public RelativeLayout relativeLayout;

        public RecyclerViewHolderHome(final View itemView, final ArrayList<VoiceSearchItem> itemList, final Context context) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("id", itemList.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}