package com.example.lebonpetitcoin.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.lebonpetitcoin.CustomToast;
import com.example.lebonpetitcoin.MainActivity;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class SignInFragment extends Fragment {
    private static final int RC_SIGN_IN = 123;
    private static final int RESULT_OK = 1;
    private static View view;
    private static EditText emailId, password;
    private static TextView signUp,forgot;
    private static Button signInButton;
    private String TAG = "SignInFragment" ;

    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build());


    public static final String regEx = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

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
        signUp = (TextView) view.findViewById(R.id.signUp);
        forgot= (TextView) view.findViewById(R.id.forgot);


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
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpFragment();
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                final View formElementsView = inflater.inflate(R.layout.form_mot_de_passe_oublie,
                        null, false);

                final EditText emailEditText = (EditText) formElementsView
                        .findViewById(R.id.email_oublie);

                // the alert dialog
                new AlertDialog.Builder(getContext()).setView(formElementsView)
                        .setTitle(R.string.remplir_les_champs_obligatoires)
                        .setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton(R.string.valider, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int id) {
                                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                mAuth.setLanguageCode("fr");
                                String email = emailEditText.getText().toString();
                                if(email.length()>0){
                                    sendMail(mAuth,email,dialogInterface);
                                }
                                else
                                    new CustomToast().Show_Toast(getActivity(), getView(), getString(R.string.email_invalide));
                            }
                        }).show();
            }
        });

    }

    private void sendMail(FirebaseAuth mAuth, String email,DialogInterface dialogInterface) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dialogInterface.cancel();
                            new CustomToast().Show_Toast(getActivity(), getView(), getString(R.string.checkMail));

                        } else {
                            dialogInterface.cancel();
                            new CustomToast().Show_Toast(getActivity(), getView(), getString(R.string.echec));
                        }
                    }
                });
    }

    private void connexion(String email, String password){
        ((MainActivity)getActivity()).mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity)getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = ((MainActivity)getActivity()).mAuth.getCurrentUser();
                            ((MainActivity)getActivity()).updateUI(user);
                            new CustomToast().Show_Toast(getActivity(), getView(), getString(R.string.connected));
                            ((MainActivity)getActivity()).showAccueilFragment();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            new CustomToast().Show_Toast(getActivity(), getView(), getString(R.string.failConnect));
                            ((MainActivity)getActivity()).updateUI(null);
                        }
                    }
                });
    }


    private void checkValidation() {

        ((MainActivity)getActivity()).hideKeyboard(getActivity());
        String getEmailId = emailId.getText().toString();
        String getPassword = password.getText().toString();
        password.setText("");

        // Pattern match
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(getEmailId);

        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
            )

            new CustomToast().Show_Toast(getActivity(), view,
                    getString(R.string.champ_vide));

        else if (!m.find())
            new CustomToast().Show_Toast(getActivity(), view,
                    getString(R.string.badSignIn));


            // Else do signup or do your stuff
        else
            connexion(getEmailId,getPassword);
    }

    private  void signUpFragment(){
        ((MainActivity)getActivity()).showSignUpFragment();
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