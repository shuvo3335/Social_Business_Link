package com.example.redoy.lynk.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.redoy.lynk.R;
import com.example.redoy.lynk.application.ApiClient;
import com.example.redoy.lynk.application.RetrofitLynk;
import com.example.redoy.lynk.model.DataForDeals;
import com.example.redoy.lynk.model.DealsResponse;
import com.example.redoy.lynk.util.CustomSweetAlertDialog;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

@SuppressLint("ValidFragment")
public class DealsFragment extends Fragment {

    @BindView(R.id.deals_textView)
    TextView mDealsTextView;

    @BindView(R.id.deals_imageView)
    ImageView mDealsImageView;

    private String id;
    private static final String TAG = DealsFragment.class.getSimpleName();

    View rootView;

    @SuppressLint("ValidFragment")
    public DealsFragment(String id) {
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_deals, container, false);
        ButterKnife.bind(this, rootView);
        initializeData();
        return rootView;
    }

    private void initializeData() {
        CustomSweetAlertDialog customSweetAlertDialog = new CustomSweetAlertDialog();
        final SweetAlertDialog dialog = customSweetAlertDialog.getProgressDialog(rootView.getContext(), "Loading Deals...");
        dialog.show();

        RetrofitLynk apiService = ApiClient.getLynkClient().create(RetrofitLynk.class);

        Call<DealsResponse> call = apiService.getDealsOutput(id);
        call.enqueue(new Callback<DealsResponse>() {
            @Override
            public void onResponse(final Response<DealsResponse> response, Retrofit retrofit) {
                if (response.body() != null) {
                    final DealsResponse dealsResponse = response.body();
                    final Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (dealsResponse != null) {
                                dialog.dismiss();
                                DataForDeals dataForDeals = dealsResponse.getDataForDeals();
                                mDealsTextView.setText(dataForDeals.getTitle());
                                Picasso.get().load(dataForDeals.getDeal_img_url()).into(mDealsImageView);
                                handler.removeCallbacksAndMessages(true);
                            } else {
                                handler.postDelayed(this, 100);
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
}