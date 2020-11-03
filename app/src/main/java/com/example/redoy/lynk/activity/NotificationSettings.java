package com.example.redoy.lynk.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.example.redoy.lynk.R;
import com.example.redoy.lynk.adapter.CustomSpinnerAdapter;
import com.example.redoy.lynk.application.LynkApplication;
import com.example.redoy.lynk.service.CustomSharedPreference;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationSettings extends AppCompatActivity {

    @BindView(R.id.language_spinner)
    public Spinner languageSpinner;

    @BindView(R.id.notification_switch)
    public SwitchCompat notificationSwitch;

    private CustomSharedPreference shared;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);
        ButterKnife.bind(this);
        initializeWidgets();
        initializeData();
    }

    private void initializeWidgets() {
        context = getApplicationContext();
        notificationSwitch.setOnCheckedChangeListener(new ChangeNotification());
    }

    private void initializeData() {
        shared = LynkApplication.getShared(context);

        ArrayList<String> languages = new ArrayList();
        languages.add("English");
        languageSpinner.setAdapter(new CustomSpinnerAdapter(context, languages));

        if (shared.getSavedNotification()) {
            notificationSwitch.setChecked(true);
        } else {
            notificationSwitch.setChecked(false);
        }
    }

    class ChangeNotification implements CompoundButton.OnCheckedChangeListener {
        ChangeNotification() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                shared.saveNotification(true);
            } else {
                shared.saveNotification(false);
            }
        }
    }
}
