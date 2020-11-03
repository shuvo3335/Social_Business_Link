package com.example.redoy.lynk.fragment;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redoy.lynk.R;
import com.example.redoy.lynk.activity.LogInActivity;
import com.example.redoy.lynk.adapter.ReviewAdapter;
import com.example.redoy.lynk.application.ApiClient;
import com.example.redoy.lynk.application.RetrofitLynk;
import com.example.redoy.lynk.model.ProfileReviewData;
import com.example.redoy.lynk.model.ProfileReviewResponse;
import com.example.redoy.lynk.model.ReviewBody;
import com.example.redoy.lynk.model.ReviewItem;
import com.example.redoy.lynk.model.SubmitReviewResponse;
import com.example.redoy.lynk.service.CustomSharedPreference;
import com.example.redoy.lynk.util.CustomSweetAlertDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.app.Activity.RESULT_OK;

@SuppressLint("ValidFragment")
public class ReviewsFragment extends Fragment {

    @BindView(R.id.recycler_view_reviews)
    RecyclerView mReviewsRecyclerView;

    @BindView(R.id.empty_textView)
    TextView mEmptyTextView;

    @BindView(R.id.add_review_button)
    FloatingActionButton addReviewButton;

    ArrayList<ReviewItem> reviewItems;
    public final int REQ_CODE_SPEECH_INPUT = 100;
    String resultString;
    View rootView;

    private String id;
    private float rating = 0;
    private static final String TAG = ReviewsFragment.class.getSimpleName();

    @SuppressLint("ValidFragment")
    public ReviewsFragment(String id) {
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_reviews, container, false);
        ButterKnife.bind(this, rootView);
        initializeWidgets();
        return rootView;
    }

    private void initializeWidgets() {
        reviewItems = new ArrayList<>();
        getReviewItems();
    }

    private void getReviewItems() {
        CustomSweetAlertDialog customSweetAlertDialog = new CustomSweetAlertDialog();
        final SweetAlertDialog dialog = customSweetAlertDialog.getProgressDialog(rootView.getContext(), "Loading...");
        dialog.show();

        RetrofitLynk apiService = ApiClient.getLynkClient().create(RetrofitLynk.class);

        Call<ProfileReviewResponse> call = apiService.getProfileReviews(id);
        call.enqueue(new Callback<ProfileReviewResponse>() {
            @Override
            public void onResponse(final Response<ProfileReviewResponse> response, Retrofit retrofit) {
                if (response.body() != null) {
                    final ProfileReviewResponse profileReviewResponse = response.body();
                    final Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            ProfileReviewData[] profileReviewData = profileReviewResponse.getProfileReviewData();
                            for (int i = 0; i < profileReviewData.length; i++) {
                                reviewItems.add(new ReviewItem(profileReviewData[i].getReview_by(), profileReviewData[i].getCreated_at().getDate(), profileReviewData[i].getReview_body(), profileReviewData[i].getRated_by_user()));
                            }
                            initializeData();
                            handler.removeCallbacksAndMessages(true);
                        }
                    };
                    handler.postDelayed(runnable, 100);
                } else {
                    dialog.dismiss();
                    Toast.makeText(rootView.getContext(), "Sorry, no data found.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void initializeData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        mReviewsRecyclerView.setLayoutManager(linearLayoutManager);
        mReviewsRecyclerView.setHasFixedSize(true);
        Collections.reverse(reviewItems);
        ReviewAdapter adapter = new ReviewAdapter(getContext(), reviewItems);
        mReviewsRecyclerView.setAdapter(adapter);

        if (adapter.getItemCount() == 0) {
            mEmptyTextView.setVisibility(View.VISIBLE);
        } else {
            mEmptyTextView.setVisibility(View.GONE);
        }
        addReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVoiceDialog();
            }
        });
    }

    private void showVoiceDialog() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(rootView.getContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {

                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    resultString = result.get(0);
                    addReviewDialog(resultString);
                }
                break;
            }
        }
    }

    private void addReviewDialog(final String resultString) {
        LayoutInflater li = LayoutInflater.from(rootView.getContext());
        View promptsView = li.inflate(R.layout.review_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(rootView.getContext());
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = promptsView.findViewById(R.id.editTextDialogReviewMessage);
        final RatingBar mRatingBar = promptsView.findViewById(R.id.ratingBar);
        userInput.setText(resultString);

        mRatingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating = mRatingBar.getRating();
            }
        });

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Post",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                postReview(userInput.getText().toString());
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void postReview(String resultString) {

        CustomSweetAlertDialog customSweetAlertDialog = new CustomSweetAlertDialog();
        final SweetAlertDialog dialog = customSweetAlertDialog.getProgressDialog(rootView.getContext(), "Loading Reviews...");
        dialog.show();

        CustomSharedPreference customSharedPreference = new CustomSharedPreference(rootView.getContext());
        String token = customSharedPreference.getAccessToken();

        if (token.length() > 0) {
            RetrofitLynk apiService = ApiClient.getLynkClient().create(RetrofitLynk.class);
            ReviewBody reviewBody = new ReviewBody(resultString);

            Call<SubmitReviewResponse> call = apiService.submitReview(id, token, reviewBody);
            call.enqueue(new Callback<SubmitReviewResponse>() {
                @Override
                public void onResponse(final Response<SubmitReviewResponse> response, Retrofit retrofit) {
                    final SubmitReviewResponse submitReviewResponse = response.body();
                    if (submitReviewResponse.getSuccess() == true) {
                        final Handler handler = new Handler();
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                Toast.makeText(rootView.getContext(), submitReviewResponse.getMessage(), Toast.LENGTH_LONG).show();
                                handler.removeCallbacksAndMessages(true);
                                getReviewItems();
                            }
                        };
                        handler.postDelayed(runnable, 100);
                    } else {
                        dialog.dismiss();
                        Toast.makeText(rootView.getContext(), "Review not Posted", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(TAG, t.toString());
                }
            });
        } else {
            dialog.dismiss();
            new SweetAlertDialog(rootView.getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Unauthorized")
                    .setContentText("You Are not Logged In Please Log In to Post a Review")
                    .setConfirmText("OK")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.cancel();
                        }
                    })
                    .show();
        }
    }
}