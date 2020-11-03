package com.example.redoy.lynk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.redoy.lynk.R;
import com.example.redoy.lynk.application.ApiClient;
import com.example.redoy.lynk.application.RetrofitLynk;
import com.example.redoy.lynk.model.SignUp;
import com.example.redoy.lynk.model.SignUpResponse;
import com.example.redoy.lynk.util.ConnectionStatus;
import com.example.redoy.lynk.util.CustomSweetAlertDialog;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SignUpActivity extends AppCompatActivity implements Validator.ValidationListener {

    @BindView(R.id.sign_up_button)
    Button mButtonSignUp;

    @BindView(R.id.sign_up_edit_text_widget_name)
    EditText mEditTextName;

    @NotEmpty
    @BindView(R.id.sign_up_edit_text_widget_mobile)
    EditText mEditTextMobile;

    @Email(message = "Please Check and Enter a valid Email Address")
    @NotEmpty
    @BindView(R.id.sign_up_edit_text_widget_email)
    EditText mEditTextEmail;

    @Password(scheme = Password.Scheme.ANY, message = "Enter a Valid Password")
    @NotEmpty
    @BindView(R.id.sign_up_edit_text_widget_password)
    EditText mEditTextPassword;

    @ConfirmPassword
    @BindView(R.id.sign_up_edit_text_widget_confirm_password)
    EditText mEditTextConfirmPassword;

    String strName, strMobile, strEmail, strPassword, strConfirmPassword;
    private Validator validator;
    private static final String TAG = SignUpActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initializeWidgets();
    }

    private void initializeWidgets() {
        ButterKnife.bind(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        validator = new Validator(this);
        validator.setValidationListener(this);

        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpActivity.this.validator.validate();
            }
        });
    }

    private void getSignUp() {
        CustomSweetAlertDialog customSweetAlertDialog = new CustomSweetAlertDialog();
        final SweetAlertDialog dialog = customSweetAlertDialog.getProgressDialog(this, "Logging In...");
        dialog.show();

        RetrofitLynk apiService = ApiClient.getLynkClient().create(RetrofitLynk.class);

        final SignUp signUp = new SignUp(strName, strEmail, strMobile, strPassword, strConfirmPassword);

        Call<SignUpResponse> call = apiService.getSignUpOutput(signUp);
        call.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(final Response<SignUpResponse> response, Retrofit retrofit) {
                if (response.body() != null) {
                    final SignUpResponse signUpResponse = response.body();
                    final Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), signUpResponse.getMessage(), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                            handler.removeCallbacksAndMessages(true);
                        }
                    };
                    handler.postDelayed(runnable, 100);
                } else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Email or Mobile Already Taken", Toast.LENGTH_LONG).show();
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

    @Override
    public void onValidationSucceeded() {
        strName = mEditTextName.getText().toString();
        strMobile = mEditTextMobile.getText().toString();
        strEmail = mEditTextEmail.getText().toString();
        strPassword = mEditTextPassword.getText().toString();
        strConfirmPassword = mEditTextConfirmPassword.getText().toString();

        if (ConnectionStatus.getInstance(this).isOnline()) {
            getSignUp();
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
}
