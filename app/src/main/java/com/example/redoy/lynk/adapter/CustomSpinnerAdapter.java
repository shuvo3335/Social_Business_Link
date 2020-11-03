package com.example.redoy.lynk.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.redoy.lynk.R;

import java.util.ArrayList;

public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
    private final Context activity;
    private ArrayList<String> asr;

    public CustomSpinnerAdapter(Context context, ArrayList<String> asr) {
        this.asr = asr;
        this.activity = context;
    }

    public int getCount() {
        return this.asr.size();
    }

    public Object getItem(int i) {
        return this.asr.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(this.activity);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(18.0f);
        txt.setGravity(16);
        txt.setText(this.asr.get(position));
        txt.setTextColor(Color.parseColor("#000000"));
        return txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(this.activity);
        txt.setGravity(17);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(16.0f);
        txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_circle, 0);
        txt.setText((CharSequence) this.asr.get(i));
        txt.setTextColor(Color.parseColor("#000000"));
        return txt;
    }
}
