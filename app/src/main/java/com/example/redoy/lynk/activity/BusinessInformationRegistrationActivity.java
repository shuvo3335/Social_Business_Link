package com.example.redoy.lynk.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redoy.lynk.R;
import com.example.redoy.lynk.adapter.PlaceArrayAdapter;
import com.example.redoy.lynk.application.ApiClient;
import com.example.redoy.lynk.application.RetrofitLynk;
import com.example.redoy.lynk.model.BusinessRegistration;
import com.example.redoy.lynk.model.BusinessRegistrationResponse;
import com.example.redoy.lynk.model.CategoryData;
import com.example.redoy.lynk.model.CategoryResponse;
import com.example.redoy.lynk.model.ThanaData;
import com.example.redoy.lynk.model.ThanaResponse;
import com.example.redoy.lynk.service.CustomSharedPreference;
import com.example.redoy.lynk.util.ConnectionStatus;
import com.example.redoy.lynk.util.CustomSweetAlertDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class BusinessInformationRegistrationActivity extends AppCompatActivity implements Validator.ValidationListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    @NotEmpty
    @BindView(R.id.registration_edit_text_widget_full_name)
    EditText mEditTextFullName;

    @NotEmpty
    @BindView(R.id.registration_edit_text_widget_password)
    TextView mTextViewPassword;

    @NotEmpty
    @BindView(R.id.registration_edit_text_widget_confirm_password)
    TextView mTextViewConfirmPassword;

    @NotEmpty
    @BindView(R.id.registration_edit_text_widget_email)
    EditText mEditTextEmail;

    @NotEmpty
    @BindView(R.id.registration_edit_text_widget_verified_phone)
    TextView mTextViewVerifiedPhone;

    @NotEmpty
    @BindView(R.id.registration_edit_text_widget_business_name)
    TextView mTextViewBusinessName;

    @BindView(R.id.registration_edit_text_widget_business_description)
    TextView mTextViewBusinessDescription;

    @NotEmpty
    @BindView(R.id.registration_edit_text_widget_business_location)
    TextView mTextViewBusinessLocation;

    @NotEmpty
    @BindView(R.id.registration_edit_text_widget_business_category)
    AutoCompleteTextView mTextViewBusinessCategory;

    @NotEmpty
    @BindView(R.id.registration_edit_text_widget_thana)
    AutoCompleteTextView mTextViewThana;

    @NotEmpty
    @BindView(R.id.registration_edit_text_widget_google_location)
    AutoCompleteTextView mTextViewGoogleLocation;

    @BindView(R.id.registration_edit_text_widget_deals)
    TextView mTextViewDeals;

    @BindView(R.id.submit_button)
    Button mButtonSubmit;

    private Validator validator;

    private String mBusinessNameString, mBusinessCategoryString, mBusinessLocationString, mBusinessVerifiedPhoneString, mBusinessThanaString, mBusinessDealsString;
    private String mFullNameString, mBusinessDescriptionString, mEmailString, mPasswordString, mConfirmPasswordStrring, mGoogleLocationString;
    private String mLatString = "", mLngString = "";
    private String token;

    String[] thanaArray;
    String[] categoryArray;

    LocationRequest mLocationRequest;
    protected static long MIN_UPDATE_INTERVAL = 30 * 1000;
    FusedLocationProviderClient mFusedLocationClient;
    Location mLastLocation;
    double latitude;
    double longitude;

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    private static final String TAG = BusinessRegistration.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_information_registration);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        initializeWidgets();
    }

    private void getGoogleLocation() {
        mTextViewGoogleLocation.setThreshold(3);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, null);
        mTextViewGoogleLocation.setAdapter(mPlaceArrayAdapter);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();

    }

    private void initializeWidgets() {
        ButterKnife.bind(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (ConnectionStatus.getInstance(this).isOnline()) {
            getMyLocation();
            getCategory();
            getThana();
            getGoogleLocation();
        } else {
            showToast(getString(R.string.connection_msg1));
        }

        validator = new Validator(this);
        validator.setValidationListener(this);

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusinessInformationRegistrationActivity.this.validator.validate();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void getMyLocation() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(MIN_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location currentLocation = locationResult.getLastLocation();
                    mLastLocation = currentLocation;
                    latitude = currentLocation.getLatitude();
                    longitude = currentLocation.getLongitude();
                    mLatString = String.valueOf(latitude);
                    mLngString = String.valueOf(longitude);
                }
            }, Looper.myLooper());
        }
    }

    @Override
    public void onValidationSucceeded() {
        if (ConnectionStatus.getInstance(this).isOnline()) {
            submitData();
        } else {
            showToast(getString(R.string.connection_msg1));
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void submitData() {
        CustomSweetAlertDialog customSweetAlertDialog = new CustomSweetAlertDialog();
        final SweetAlertDialog dialog = customSweetAlertDialog.getProgressDialog(this, "Registering...");
        dialog.show();

        CustomSharedPreference customSharedPreference = new CustomSharedPreference(getApplicationContext());
        token = customSharedPreference.getAccessToken();

        mFullNameString = mEditTextFullName.getText().toString();
        mPasswordString = mTextViewPassword.getText().toString();
        mConfirmPasswordStrring = mTextViewConfirmPassword.getText().toString();
        mEmailString = mEditTextEmail.getText().toString();
        mBusinessVerifiedPhoneString = mTextViewVerifiedPhone.getText().toString();
        mBusinessNameString = mTextViewBusinessName.getText().toString();
        mBusinessDescriptionString = mTextViewBusinessDescription.getText().toString();
        mBusinessLocationString = mTextViewBusinessLocation.getText().toString();
        mBusinessCategoryString = mTextViewBusinessCategory.getText().toString();
        mBusinessThanaString = mTextViewThana.getText().toString();
        mGoogleLocationString = mTextViewGoogleLocation.getText().toString();
        mBusinessDealsString = mTextViewDeals.getText().toString();

        BusinessRegistration businessRegistration = new BusinessRegistration(mFullNameString, mPasswordString, mConfirmPasswordStrring, mEmailString,
                mBusinessVerifiedPhoneString, mBusinessNameString, mBusinessDescriptionString, mBusinessLocationString, mBusinessCategoryString,
                mBusinessThanaString, "", "", "", mGoogleLocationString, mLatString, mLngString, mBusinessDealsString);

        if (token.length() > 0) {
            RetrofitLynk apiService = ApiClient.getLynkClient().create(RetrofitLynk.class);
            Call<BusinessRegistrationResponse> call = apiService.submitBusinessRegistration(token, businessRegistration);
            call.enqueue(new Callback<BusinessRegistrationResponse>() {
                @Override
                public void onResponse(final Response<BusinessRegistrationResponse> response, Retrofit retrofit) {
                    if (response.body() != null) {
                        final BusinessRegistrationResponse businessRegistrationResponse = response.body();
                        if (businessRegistrationResponse.getSuccess() == true) {
                            final Handler handler = new Handler();
                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), businessRegistrationResponse.getMessage(), Toast.LENGTH_LONG).show();
                                    handler.removeCallbacksAndMessages(true);
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                            };
                            handler.postDelayed(runnable, 100);
                        } else {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Review not Posted", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Email or Password Already in Use", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(TAG, t.toString());
                }
            });
        } else {
            dialog.dismiss();
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Unauthorized")
                    .setContentText("You Are not Logged In Please Log In to Register a Business")
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

    private void getCategory() {
        CustomSweetAlertDialog customSweetAlertDialog = new CustomSweetAlertDialog();
        final SweetAlertDialog dialog = customSweetAlertDialog.getProgressDialog(this, "Loading...");
        dialog.show();

        RetrofitLynk apiService = ApiClient.getLynkClientForThanaAndCategory().create(RetrofitLynk.class);
        Call<CategoryResponse> call = apiService.getCategoryResponse();
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(final Response<CategoryResponse> response, Retrofit retrofit) {
                final CategoryResponse categoryResponse = response.body();
                final CategoryData[] categoryData = categoryResponse.getCategoryData();
                if (categoryData.length > 0) {
                    categoryArray = new String[categoryData.length];
                    final Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            for (int i = 0; i < categoryData.length; i++)
                                categoryArray[i] = categoryData[i].getCat_name();
                            handler.removeCallbacksAndMessages(true);
                            mTextViewBusinessCategory.setThreshold(1);
                            mTextViewBusinessCategory.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, categoryArray));
                        }
                    };
                    handler.postDelayed(runnable, 100);
                } else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Review not Posted", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void getThana() {
        CustomSweetAlertDialog customSweetAlertDialog = new CustomSweetAlertDialog();
        final SweetAlertDialog dialog = customSweetAlertDialog.getProgressDialog(this, "Loading...");
        dialog.show();

        RetrofitLynk apiService = ApiClient.getLynkClientForThanaAndCategory().create(RetrofitLynk.class);
        Call<ThanaResponse> call = apiService.getThanaResponse();
        call.enqueue(new Callback<ThanaResponse>() {
            @Override
            public void onResponse(final Response<ThanaResponse> response, Retrofit retrofit) {
                final ThanaResponse thanaResponse = response.body();
                final ThanaData[] thanaData = thanaResponse.getThanaData();
                if (thanaData.length > 0) {
                    thanaArray = new String[thanaData.length];
                    final Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            for (int i = 0; i < thanaData.length; i++)
                                thanaArray[i] = thanaData[i].getThana();
                            handler.removeCallbacksAndMessages(true);
                            mTextViewThana.setThreshold(1);
                            mTextViewThana.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, thanaArray));
                        }
                    };
                    handler.postDelayed(runnable, 100);
                } else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Review not Posted", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
