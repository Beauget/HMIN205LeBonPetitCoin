package com.example.lebonpetitcoin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.lebonpetitcoin.CustomToast;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class SignInFragment extends Fragment implements View.OnClickListener {
    private static final int RC_SIGN_IN = 123;
    private static final int RESULT_OK = 1;
    private static View view;
    private static EditText emailId, password;
    private static TextView login;
    private static Button signInButton;

    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build());


    public static final String regEx = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    public SignInFragment() {

    }

    public static SignInFragment newInstance() {
        return (new SignInFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signin, container, false);
        initViews();
        setListeners();
        return view;
    }

    // Initialize all views
    private void initViews() {

        emailId = (EditText) view.findViewById(R.id.userEmailId);
        password = (EditText) view.findViewById(R.id.password);
        signInButton = (Button) view.findViewById(R.id.signInBtn);


        /*Setting text selector over textviews
        XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(), xrp);

            login.setTextColor(csl);
            terms_conditions.setTextColor(csl);
        } catch (Exception e) {
        }*/
    }

    // Set Listeners
    private void setListeners() {
        signInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signInBtn:
                // Call checkValidation method
                checkValidation();
                break;
        }

    }

    // Check Validation Method
    private void checkValidation() {

        // Get all edittext texts
        String getEmailId = emailId.getText().toString();
        String getPassword = password.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
            )

            new CustomToast().Show_Toast(getActivity(), view,
                    "All fields are required.");

            // Check if email id valid or not
        else if (!m.find())
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");


            // Else do signup or do your stuff
        else
            Toast.makeText(getActivity(), "Do SignUp.", Toast.LENGTH_SHORT)
                    .show();

    }



    // [START auth_fui_result]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }


}