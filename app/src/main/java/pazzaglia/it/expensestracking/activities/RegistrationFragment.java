package pazzaglia.it.expensestracking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import pazzaglia.it.expensestracking.R;
import pazzaglia.it.expensestracking.models.RegistrationPOJO;
import pazzaglia.it.expensestracking.network.AbstractApiCaller;
import pazzaglia.it.expensestracking.network.RegistrationCaller;
import pazzaglia.it.expensestracking.shared.Validator;

;

/**
 * A placeholder fragment containing a simple view.
 */
public class RegistrationFragment extends Fragment {
    private static final String TAG = "RegistrationFragment";

    @Bind(R.id.input_name) EditText _nameText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_signup) Button _signUpButton;
    @Bind(R.id.link_login) TextView _loginLink;

    public RegistrationFragment() {
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

    private boolean validate(){
        boolean valid = true;

        //check name
        String name = _nameText.getText().toString();
        valid &= Validator.isStringValid(name,_nameText);

        //check email
        String email = _emailText.getText().toString();
        valid &= Validator.isEmailValid(email,_emailText);

        //check password
        String password = _passwordText.getText().toString();
        valid &= Validator.isPasswordValid(password,_passwordText);

        return valid;
    }

    private void signUp(){

        if (!validate()) {
            //If there are wrong parameters we stop the signup
            onSignupFailed("");
            return;
        }

        //No error, we can procede
        _signUpButton.setEnabled(false);

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        //Retrofit signUp
        RegistrationCaller registrationCaller = new RegistrationCaller(getActivity(), name, email, password);
        registrationCaller.doApiCall(getActivity(),  "Registering...", new AbstractApiCaller.MyCallbackInterface<RegistrationPOJO>() {
            @Override
            public void onDownloadFinishedOK(RegistrationPOJO result) {
                onSignupSuccess(result.getMessage());
            }
            @Override
            public void onDownloadFinishedKO(RegistrationPOJO result) {
                onSignupFailed((result!=null)? result.getMessage(): "");
            }

            @Override
            public void doApiCallOnFailure() {
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

    private void onSignupFailed(String message) {
        if(message!="")
            Toast.makeText(getActivity().getBaseContext(), message, Toast.LENGTH_LONG).show();
        _signUpButton.setEnabled(true);
    }



}
