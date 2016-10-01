package pazzaglia.it.expensestracking.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import pazzaglia.it.expensestracking.R;
import pazzaglia.it.expensestracking.models.LoginPOJO;
import pazzaglia.it.expensestracking.network.ApiInterface;
import pazzaglia.it.expensestracking.network.Utils;
import pazzaglia.it.expensestracking.shared.Common;
import pazzaglia.it.expensestracking.shared.Validator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    public static final String EMAIL = "EMAIL";
    public static final String REGISTRATION_MESSAGE = "REGISTRATION_MESSAGE";
    private static final int REQUEST_SIGNUP = 0;

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //check if killed or logout
        String name = Common.getName(this);
        String apiKey = Common.getApiKey(this);
        if (!name.equals("") && !apiKey.equals("")){
            //navigate to LandingLoginPage
            navigateToLandingPage();
        }

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the RegistrationPOJO activity
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }


    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed("");
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = Common.showProgressDialog(LoginActivity.this, "Authenticating...");

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        //Retrofit login
        ApiInterface mApiService = Utils.getInterfaceService(this, false);
        Call<LoginPOJO> mService = mApiService.loginPost(email, password);
        mService.enqueue(new Callback<LoginPOJO>() {
            @Override
            public void onResponse(Call<LoginPOJO> call, Response<LoginPOJO> response) {
                LoginPOJO mLoginObject = response.body();
                boolean loginKo = mLoginObject != null && mLoginObject.getError();
                if(!loginKo){
                    onLoginSuccess(mLoginObject);
                }else {
                    onLoginFailed((mLoginObject != null)?mLoginObject.getMessage():"");
                }
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<LoginPOJO> call, Throwable t) {
                call.cancel();
                onLoginFailed("Please check your network connection and internet permission");
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                _emailText.setText(data.getStringExtra(EMAIL));
                Toast.makeText(getBaseContext(), data.getStringExtra(REGISTRATION_MESSAGE), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(LoginPOJO mLoginObject) {
        _loginButton.setEnabled(true);
        _passwordText.setText("");

        //save API Key and name
        Common.setApiKeyAndName(this, mLoginObject.getApiKey(), mLoginObject.getName());

        //open landing page
        navigateToLandingPage();
    }

    public void onLoginFailed(String message) {
        if(message!="")
            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        //check email
        String email = _emailText.getText().toString();
        valid &= Validator.isEmailValid(email,_emailText);

        //check password
        String password = _passwordText.getText().toString();
        valid &= Validator.isPasswordValid(password,_passwordText);

        return valid;
    }

    private void navigateToLandingPage(){
        Intent intent = new Intent(getApplicationContext(), LandingPageActivity.class);
        startActivity(intent);
    }
}

