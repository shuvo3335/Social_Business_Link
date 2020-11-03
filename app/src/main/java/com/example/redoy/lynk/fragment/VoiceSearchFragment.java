package com.example.redoy.lynk.fragment;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.redoy.lynk.R;
import com.example.redoy.lynk.activity.BusinessInformationRegistrationActivity;
import com.example.redoy.lynk.adapter.AutoFitGridLayoutManager;
import com.example.redoy.lynk.adapter.RecyclerViewAdapterVoiceSearch;
import com.example.redoy.lynk.application.ApiClient;
import com.example.redoy.lynk.application.RetrofitLynk;
import com.example.redoy.lynk.model.Data;
import com.example.redoy.lynk.model.SearchResponse;
import com.example.redoy.lynk.model.VoiceSearchItem;
import com.example.redoy.lynk.util.ConnectionStatus;
import com.example.redoy.lynk.util.CustomSweetAlertDialog;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Redoy on 3/30/2018.
 */

public class VoiceSearchFragment extends Fragment {

    View rootView;
    @BindView(R.id.microphone_logo_imageView)
    public ImageView microPhoneImageView;

    @BindView(R.id.hint_textView)
    public TextView hintTextView;


    @BindView(R.id.title)
    public TextView title;

    @BindView(R.id.empty_textView)
    public TextView emptyTextView;

    @BindView(R.id.quick_search_textView)
    public TextView quickSearchTextView;

    @BindView(R.id.button2)
    public Button quickSearch;

    @BindView(R.id.recycler_view_voice_search)
    public RecyclerView voiceSearchRecyclerView;

    public final int REQ_CODE_SPEECH_INPUT = 100;
    public String resultText;
    private static final String TAG = VoiceSearchFragment.class.getSimpleName();
    private ArrayList<VoiceSearchItem> searchResponses = new ArrayList<>();

    public RecyclerViewAdapterVoiceSearch recyclerViewAdapterVoiceSearch;
    public AutoFitGridLayoutManager layoutManager;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_voice_search, container, false);
        ButterKnife.bind(this, rootView);
        initializeWidgets();

        return rootView;
    }

    private void initializeWidgets() {
        title.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.Flash)
                .duration(2000)
                .repeat(4)
                .playOn(title);
        setHasOptionsMenu(true);
        microPhoneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectionStatus.getInstance(rootView.getContext()).isOnline()) {
                    showSearchDialog();
                } else {
                    showToast(getString(R.string.connection_msg1));
                }
            }
        });

        emptyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emptyTextView.setVisibility(View.GONE);
                quickSearchTextView.setVisibility(View.GONE);
                showSearchDialog();
            }
        });

        quickSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyTextView.setVisibility(View.GONE);
                quickSearch.setVisibility(View.VISIBLE);
                showQuickSearchDialog();
            }
        });

        quickSearchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyTextView.setVisibility(View.GONE);
                quickSearchTextView.setVisibility(View.GONE);
                showQuickSearchDialog();
            }
        });
    }

    private void showSearchDialog() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(rootView.getContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    public void showToast(String msg) {
        Toast.makeText(rootView.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    resultText = result.get(0);
                    Toast.makeText(rootView.getContext(), resultText, Toast.LENGTH_LONG).show();
                    microPhoneImageView.setVisibility(View.GONE);
                    hintTextView.setVisibility(View.GONE);
                    getSearchResult(resultText);
                }
                break;
            }
        }
    }

    private void getSearchResult(String resultText) {
        CustomSweetAlertDialog customSweetAlertDialog = new CustomSweetAlertDialog();
        final SweetAlertDialog dialog = customSweetAlertDialog.getProgressDialog(rootView.getContext(), "Searching...");
        dialog.show();

        RetrofitLynk apiService = ApiClient.getLynkClient().create(RetrofitLynk.class);

        Call<SearchResponse> call = apiService.getSearchOutput(resultText);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(final Response<SearchResponse> response, Retrofit retrofit) {
                if (response.body() != null) {
                    final SearchResponse searchResponse = response.body();
                    final Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (!searchResponse.getData().toString().equals("")) {
                                dialog.dismiss();

                                Data[] data = searchResponse.getData();
                                for (int i = 0; i < data.length; i++) {
                                    searchResponses.add(new VoiceSearchItem(data[i].getId(), data[i].getTitle(), data[i].getFeature_img(), data[i].getThana(), data[i].getVerified_phone()));
                                }
                                initializeData();
                                handler.removeCallbacksAndMessages(true);
                            } else if (searchResponse.getData().toString().equals("")) {
                                initializeData();
                                handler.removeCallbacksAndMessages(true);
                            } else {
                                handler.postDelayed(this, 100);
                            }
                        }
                    };
                    handler.postDelayed(runnable, 100);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void initializeData() {
        recyclerViewAdapterVoiceSearch = new RecyclerViewAdapterVoiceSearch(rootView.getContext(), searchResponses, getFragmentManager());
        voiceSearchRecyclerView.setAdapter(recyclerViewAdapterVoiceSearch);
        layoutManager = new AutoFitGridLayoutManager(rootView.getContext(), 1000);
        voiceSearchRecyclerView.setLayoutManager(layoutManager);

        if (recyclerViewAdapterVoiceSearch.getItemCount() != 0) {
            voiceSearchRecyclerView.setVisibility(View.VISIBLE);
            hintTextView.setVisibility(View.GONE);
            voiceSearchRecyclerView.setVisibility(View.VISIBLE);
        } else {
            title.setVisibility(View.INVISIBLE);
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText("No Data Found\nTap Here to Retry!");
            quickSearchTextView.setVisibility(View.VISIBLE);
        }
    }

    private void showQuickSearchDialog() {

        LayoutInflater li = LayoutInflater.from(rootView.getContext());
        View promptsView = li.inflate(R.layout.quick_search_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(rootView.getContext());
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = promptsView.findViewById(R.id.editTextDialogQuickSearch);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Search",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                getSearchResult(userInput.getText().toString());
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.voice_search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.voice_search_menu_add:
                startActivity(new Intent(rootView.getContext(), BusinessInformationRegistrationActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
