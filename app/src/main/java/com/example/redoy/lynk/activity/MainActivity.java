package com.example.redoy.lynk.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.redoy.lynk.R;
import com.example.redoy.lynk.application.ApiClient;
import com.example.redoy.lynk.application.LynkApplication;
import com.example.redoy.lynk.application.RetrofitLynk;
import com.example.redoy.lynk.fragment.VoiceSearchFragment;
import com.example.redoy.lynk.model.AllDealsResponse;
import com.example.redoy.lynk.model.DataForNotification;
import com.example.redoy.lynk.service.CustomSharedPreference;
import com.example.redoy.lynk.service.LocationAlertIntentService;
import com.example.redoy.lynk.util.CustomSweetAlertDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static com.example.redoy.lynk.fragment.GoogleMapSearchFragment.MY_PERMISSIONS_REQUEST_LOCATION;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    private CustomSharedPreference shared;
    private Context context;
    private String mDealsTitleString;
    private String mDealsLatString;
    private String mDealsLngString;
    private static final String TAG = MainActivity.class.getSimpleName();
    private GeofencingClient geofencingClient;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleApiClient mGoogleApiClient;

    //meters
    private static final int GEOFENCE_RADIUS = 500;
    protected static long MIN_UPDATE_INTERVAL = 30 * 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        initializeWidgets();
        initializeNotifications();
    }

    private void initializeWidgets() {
        ButterKnife.bind(this);

        getSupportActionBar().setTitle(R.string.title_fragment_voice_search);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new VoiceSearchFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment mFragment = null;

                switch (item.getItemId()) {
                    case R.id.navigation_voice_search: {
                        mFragment = new VoiceSearchFragment();
                        getSupportActionBar().setTitle(R.string.title_fragment_voice_search);
                        break;
                    }
                    case R.id.navigation_map_search: {
                        mFragment = new Fragment();
                        getSupportActionBar().setTitle(R.string.title_fragment_google_map_search);
                        break;
                    }
                    case R.id.navigation_settings: {
                        mFragment = new Fragment();
                        getSupportActionBar().setTitle(R.string.title_fragment_settings);
                        break;
                    }
                }
                FragmentManager mFragmentManager = getSupportFragmentManager();
                if (mFragment != null) {
                    mFragmentManager.beginTransaction().replace(R.id.frame_layout, mFragment).commit();
                }
                return true;
            }
        });
    }

    private void initializeNotifications() {
        context = getApplicationContext();
        shared = LynkApplication.getShared(context);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        } else {
            if (mGoogleApiClient == null) {
                buildGoogleApiClient();
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void getNotifications() {

        geofencingClient = LocationServices.getGeofencingClient(this);

        CustomSweetAlertDialog customSweetAlertDialog = new CustomSweetAlertDialog();
        final SweetAlertDialog dialog = customSweetAlertDialog.getProgressDialog(this, "Please Wait...");
        dialog.show();

        RetrofitLynk apiService = ApiClient.getLynkClient().create(RetrofitLynk.class);

        Call<AllDealsResponse> call = apiService.getAllNotifications();
        call.enqueue(new Callback<AllDealsResponse>() {
            @Override
            public void onResponse(final Response<AllDealsResponse> response, Retrofit retrofit) {
                if (response.body() != null) {
                    final AllDealsResponse allDealsResponse = response.body();
                    final Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (allDealsResponse != null && allDealsResponse.getDataForNotification().length > 0) {
                                dialog.dismiss();
                                DataForNotification[] dataForNotifications = allDealsResponse.getDataForNotification();
                                for (int i = 0; i < dataForNotifications.length; i++) {
                                    mDealsTitleString = dataForNotifications[i].getTitle();
                                    mDealsLatString = dataForNotifications[i].getLat();
                                    mDealsLngString = dataForNotifications[i].getLon();

                                    if (mDealsLatString.length() > 0 && mDealsLngString.length() > 0) {
                                        addLocationAlert(mDealsTitleString, Double.valueOf(mDealsLatString), Double.valueOf(mDealsLngString));
                                    }
                                }
                                handler.removeCallbacksAndMessages(true);
                            } else {
                                handler.postDelayed(this, 100);
                            }
                        }
                    };
                    handler.postDelayed(runnable, 100);
                } else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error Initializing Deals", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void addLocationAlert(String mDealsTitleString, final double lat, final double lng) {

        String key = "" + lat + "-" + lng;
        Geofence geofence = getGeoFence(lat, lng, key);
        geofencingClient.addGeofences(getGeoFencingRequest(geofence), getGeoFencePendingIntent(mDealsTitleString)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.e("Added ", lat + " " + lng + "");
                } else {
                    Log.e("Not Added", "Location alter could not be added");
                }
            }
        });
    }

    private Geofence getGeoFence(double lat, double lang, String key) {
        return new Geofence.Builder()
                .setRequestId(key)
                .setCircularRegion(lat, lang, GEOFENCE_RADIUS)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL)
                .setLoiteringDelay(10000)
                .build();
    }

    private GeofencingRequest getGeoFencingRequest(Geofence geofence) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofence(geofence);

        return builder.build();
    }

    private PendingIntent getGeoFencePendingIntent(String mDealsTitleString) {
        Intent intent = new Intent(context, LocationAlertIntentService.class);
        intent.putExtra("title", mDealsTitleString);

        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Exit")
                .setContentText("Do you want to close the App?")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                        finishAffinity();
                    }
                })
                .show();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(MIN_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Log.e("my location", locationResult.toString());
                }
            }, Looper.myLooper());
        }
        getNotifications();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            buildGoogleApiClient();
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                    }
                } else {
                    Toast.makeText(context, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
