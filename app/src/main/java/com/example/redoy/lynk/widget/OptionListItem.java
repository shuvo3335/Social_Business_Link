package com.example.redoy.lynk.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.redoy.lynk.R;


public class OptionListItem {
    public View view;

    @SuppressLint({"InflateParams"})
    public OptionListItem(Context context, String str, int image, OnClickListener onClickListener) {
        view = LayoutInflater.from(context).inflate(R.layout.widget_option_item, null);
        TextView textView = this.view.findViewById(R.id.option_item_widget_title);
        ImageView imageView = this.view.findViewById(R.id.option_item_widget_image_view_logo);
        textView.setText(str);
        imageView.setImageResource(image);
        view.setOnClickListener(onClickListener);
    }
}
