package com.example.redoy.lynk.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.redoy.lynk.R;
import com.example.redoy.lynk.model.ProfileResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class HighlightsFragment extends Fragment {

    @BindView(R.id.email_textView)
    TextView mEmailTextView;

    @BindView(R.id.category_textView)
    TextView mCategoryTextView;

    @BindView(R.id.website_textView)
    TextView mWebsiteTextView;

    @BindView(R.id.description_TextView)
    TextView mDescriptionTextView;

    @BindView(R.id.location_textView)
    TextView mLocationTextView;

    private ArrayList<ProfileResponse> profileResponses;
    View rootView;

    @SuppressLint("ValidFragment")
    public HighlightsFragment(ArrayList<ProfileResponse> profileResponses) {
        this.profileResponses = profileResponses;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_hightlights, container, false);
        ButterKnife.bind(this, rootView);
        initializeWidgets();
        return rootView;
    }

    private void initializeWidgets() {
        mEmailTextView.setText(profileResponses.get(0).getData().getEmail());
        mDescriptionTextView.setText(profileResponses.get(0).getData().getBiz_description());
        mLocationTextView.setText(profileResponses.get(0).getData().getAddress());
        mCategoryTextView.setText(profileResponses.get(0).getData().getCategory());
        mWebsiteTextView.setText(profileResponses.get(0).getData().getWebsite_url());
    }
}