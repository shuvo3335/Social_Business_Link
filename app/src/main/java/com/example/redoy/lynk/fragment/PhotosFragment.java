package com.example.redoy.lynk.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redoy.lynk.R;
import com.example.redoy.lynk.adapter.AutoFitGridLayoutManager;
import com.example.redoy.lynk.adapter.RecyclerViewAdapterPhotos;
import com.example.redoy.lynk.application.ApiClient;
import com.example.redoy.lynk.application.RetrofitLynk;
import com.example.redoy.lynk.model.DataForPhotos;
import com.example.redoy.lynk.model.PhotosResponse;
import com.example.redoy.lynk.util.CustomSweetAlertDialog;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

@SuppressLint("ValidFragment")
public class PhotosFragment extends Fragment {

    @BindView(R.id.recycler_view_photos)
    RecyclerView mPhotosRecyclerView;

    @BindView(R.id.empty_textView)
    TextView mEmptyTextView;

    RecyclerViewAdapterPhotos recyclerViewAdapterPhotos;
    public AutoFitGridLayoutManager layoutManager;

    private ArrayList<String> photos;
    String id;
    private static final String TAG = PhotosFragment.class.getSimpleName();

    View rootView;

    @SuppressLint("ValidFragment")
    public PhotosFragment(String id) {
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_photos, container, false);
        ButterKnife.bind(this, rootView);
        initializeData();
        return rootView;
    }

    private void initializeData() {
        photos = new ArrayList<>();

        CustomSweetAlertDialog customSweetAlertDialog = new CustomSweetAlertDialog();
        final SweetAlertDialog dialog = customSweetAlertDialog.getProgressDialog(rootView.getContext(), "Loading Photos...");
        dialog.show();

        RetrofitLynk apiService = ApiClient.getLynkClient().create(RetrofitLynk.class);

        Call<PhotosResponse> call = apiService.getPhotosOutput(id);
        call.enqueue(new Callback<PhotosResponse>() {
            @Override
            public void onResponse(final Response<PhotosResponse> response, Retrofit retrofit) {
                if (response.body() != null) {
                    final PhotosResponse photosResponse = response.body();
                    final Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (photosResponse != null && photosResponse.getDataForPhotos().length > 0) {
                                DataForPhotos[] dataForPhotos = photosResponse.getDataForPhotos();
                                for (int i = 0; i < dataForPhotos.length; i++) {
                                    photos.add(dataForPhotos[i].getImg_url());
                                }
                                dialog.dismiss();
                                initializeWidgets();
                                handler.removeCallbacksAndMessages(true);
                            } else {
                                dialog.dismiss();
                                mEmptyTextView.setVisibility(View.VISIBLE);
                                handler.removeCallbacksAndMessages(true);
                            }
                        }
                    };
                    handler.postDelayed(runnable, 100);
                } else {
                    dialog.dismiss();
                    Toast.makeText(rootView.getContext(), "Server Error.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void initializeWidgets() {
        recyclerViewAdapterPhotos = new RecyclerViewAdapterPhotos(rootView.getContext(), photos);
        mPhotosRecyclerView.setAdapter(recyclerViewAdapterPhotos);

        layoutManager = new AutoFitGridLayoutManager(rootView.getContext(), 500);
        mPhotosRecyclerView.setLayoutManager(layoutManager);
    }
}