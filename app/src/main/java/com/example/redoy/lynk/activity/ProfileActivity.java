package com.example.redoy.lynk.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;

import androidx.core.content.ContextCompat;

import androidx.palette.graphics.Palette;

import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.redoy.lynk.R;
import com.example.redoy.lynk.application.ApiClient;
import com.example.redoy.lynk.application.RetrofitLynk;
import com.example.redoy.lynk.fragment.DealsFragment;
import com.example.redoy.lynk.fragment.HighlightsFragment;
import com.example.redoy.lynk.fragment.PhotosFragment;
import com.example.redoy.lynk.fragment.ReviewsFragment;
import com.example.redoy.lynk.model.ProfileResponse;
import com.example.redoy.lynk.util.CustomSweetAlertDialog;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.htab_toolbar)
    Toolbar toolbar;

    @BindView(R.id.htab_viewpager)
    ViewPager viewPager;

    @BindView(R.id.htab_tabs)
    TabLayout tabLayout;

    @BindView(R.id.htab_collapse_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.htab_headerImageView)
    ImageView headerImageView;

    ArrayList<ProfileResponse> profileResponses;
    private String id;

    private static final String TAG = ProfileActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initializeData();
    }

    private void initializeData() {
        id = getIntent().getStringExtra("id");
        profileResponses = new ArrayList<>();

        CustomSweetAlertDialog customSweetAlertDialog = new CustomSweetAlertDialog();
        final SweetAlertDialog dialog = customSweetAlertDialog.getProgressDialog(this, "Loading...");
        dialog.show();

        RetrofitLynk apiService = ApiClient.getLynkClient().create(RetrofitLynk.class);

        Call<ProfileResponse> call = apiService.getProfileOutput(id);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(final Response<ProfileResponse> response, Retrofit retrofit) {
                if (response.body() != null) {
                    final ProfileResponse profileResponse = response.body();
                    final Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            profileResponses.add(profileResponse);
                            handler.removeCallbacksAndMessages(true);
                            initializeWidgets();
                        }
                    };
                    handler.postDelayed(runnable, 100);
                } else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Sorry, no data found.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void initializeWidgets() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(profileResponses.get(0).getData().getTitle());
        Picasso.get().load(profileResponses.get(0).getData().getFeature_img()).into(headerImageView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        try {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @SuppressWarnings("ResourceType")
                @Override
                public void onGenerated(Palette palette) {
                    int vibrantColor = palette.getVibrantColor(R.color.colorPrimary);
                    int vibrantDarkColor = palette.getDarkVibrantColor(R.color.colorPrimaryDark);
                    collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                    collapsingToolbarLayout.setStatusBarScrimColor(vibrantDarkColor);
                }
            });
        } catch (Exception e) {
            collapsingToolbarLayout.setContentScrimColor(
                    ContextCompat.getColor(this, R.color.colorPrimary)
            );
            collapsingToolbarLayout.setStatusBarScrimColor(
                    ContextCompat.getColor(this, R.color.colorPrimaryDark)
            );
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new HighlightsFragment(profileResponses), "Highlights");
        adapter.addFrag(new DealsFragment(id), "Deal");
        adapter.addFrag(new PhotosFragment(id), "Photo");
        adapter.addFrag(new ReviewsFragment(id), "Reviews");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
