package pazzaglia.it.expensestracking.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Bind;
import pazzaglia.it.expensestracking.R;
import pazzaglia.it.expensestracking.network.ApiInterface;
import pazzaglia.it.expensestracking.models.RegistrationPOJO;
import pazzaglia.it.expensestracking.network.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
;

/**
 * A placeholder fragment containing a simple view.
 */
public class RegistrationActivityFragment extends Fragment {

    private static final String TAG = "RegitratinActivityFragment";

    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_signup) Button _signUpButton;
    @Bind(R.id.link_login) TextView _loginLink;

    public RegistrationActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        ButterKnife.bind(this, view);
        _signUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //try to sign up
                signUp();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //finish current activity
                getActivity().finish();
            }
        });
        return view;
    }

    private void signUp(){


        if (!validate()) {
            //If there are wrong parameters we stop the signup
            onSignupFailed("");
            return;
        }

        //No error, we can procede
        _signUpButton.setEnabled(false);

        //Show the spinner
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registering...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        //Retrofit signUp
        ApiInterface mApiService = Utils.getInterfaceService(false,"");
        Call<RegistrationPOJO> mService = mApiService.registrationPost(name, email, password);
        mService.enqueue(new Callback<RegistrationPOJO>() {
            @Override
            public void onResponse(Call<RegistrationPOJO> call, Response<RegistrationPOJO> response) {
                RegistrationPOJO mregistrationnObject = response.body();
                boolean registrationKo = mregistrationnObject.getError();
                //showProgress(false);
                if(!registrationKo){
                    onSignupSuccess(mregistrationnObject.getMessage());
                }else {
                    onSignupFailed(mregistrationnObject.getMessage());
                }
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<RegistrationPOJO> call, Throwable t) {
                call.cancel();
                onSignupFailed("Please check your network connection and internet permission");
            }
        });
    }


    public void onSignupSuccess(String message) {
        _signUpButton.setEnabled(true);
        Intent output = new Intent();
        output.putExtra(LoginActivity.EMAIL,  _emailText.getText().toString());
        output.putExtra(LoginActivity.REGISTRATION_MESSAGE,  message);
        getActivity().setResult(getActivity().RESULT_OK, output);
        getActivity().finish();
    }

    private boolean validate(){
        boolean valid = true;

        String name = _nameText.getText().toString();

        //check name
        if (name.isEmpty() ||name.length() < 3){
            _nameText.setError("At least three characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        //check email
        String email = _emailText.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        //check password
        String password = _passwordText.getText().toString();
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private void onSignupFailed(String message) {
        if(message!="")
            Toast.makeText(getActivity().getBaseContext(), message, Toast.LENGTH_LONG).show();
        _signUpButton.setEnabled(true);
    }



}
