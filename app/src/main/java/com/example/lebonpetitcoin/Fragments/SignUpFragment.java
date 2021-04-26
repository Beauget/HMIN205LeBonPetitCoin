package com.example.lebonpetitcoin.Fragments;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.lebonpetitcoin.ClassFirestore.Annonce;
import com.example.lebonpetitcoin.ClassFirestore.Compte;
import com.example.lebonpetitcoin.CustomToast;
import com.example.lebonpetitcoin.GlideApp;
import com.example.lebonpetitcoin.MainActivity;
import com.example.lebonpetitcoin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.installations.Utils;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class SignUpFragment extends Fragment implements OnClickListener {
    private static View view;
    private static EditText fullName, emailId, mobileNumber, location,
            password, confirmPassword,siret;
    private ImageView mImageView;
    private  Uri mUri;
    private static TextView login;
    private static Button signUpButton;
    private static CheckBox terms_conditions;
    private static TextView already_user;
    private static Fragment signin;

    //NOM DE LA CLASSE QUI SERA ENVOYÉ EN CAS D'ECHEC D'ENVOIE
    private static final String TAG = "SignUpFragment";


    //RECUPERATION DE LA DB
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cCompte = firestoreDB.collection("Compte");

    //Listener afin que la recherce dans la db se fasse pas quand l'application est en arrière plan
    private ListenerRegistration compteListener;

    public static final String regEx = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static final String mdp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,30}$";


    public static SignUpFragment newInstance() {
        return (new SignUpFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        mAuth = FirebaseAuth.getInstance();
        initViews();
        setListeners();
        return view;
    }

    // Initialize all views
    private void initViews() {
        mImageView=  (ImageView) view.findViewById(R.id.imageView);
        fullName = (EditText) view.findViewById(R.id.fullName);
        siret = (EditText) view.findViewById(R.id.pro);
        emailId = (EditText) view.findViewById(R.id.userEmailId);
        mobileNumber = (EditText) view.findViewById(R.id.mobileNumber);
        location = (EditText) view.findViewById(R.id.location);
        password = (EditText) view.findViewById(R.id.password);
        confirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
        signUpButton = (Button) view.findViewById(R.id.signUpBtn);
        login = (TextView) view.findViewById(R.id.already_user);
        terms_conditions = (CheckBox) view.findViewById(R.id.terms_conditions);
        already_user = (TextView) view.findViewById(R.id.already_user);
        signin = SignInFragment.newInstance();

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
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
        mImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:

                // Call checkValidation method
                checkValidation();
                break;

            case R.id.already_user:

                // Replace login fragment
               FragmentManager x = getFragmentManager();
                x.beginTransaction().replace(R.id.activity_main_frame_layout,signin).commit();
                break;
        }

    }

    private void inscription(String email,String password,String pseudo, boolean estProfessionnel, String telephoneContact, String siret, String localisation){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity)getContext(),new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "Inscription en cours...");
                            FirebaseUser user = mAuth.getCurrentUser();
                            /*
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(uri)
                                    .build();

                            user.updateProfile(profileChangeRequest)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                addCompte(user.getUid()," ",estProfessionnel,telephoneContact,mailContact,siret,localisation);
                                            }
                                        }
                                    });*/
                            addCompte(user.getUid(),pseudo," ",estProfessionnel,telephoneContact,email,siret,localisation);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Mail déja utilisé ou mot de passe pas assez fort", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addCompte(String uid,String pseudo, String imageProfile, boolean estProfessionnel, String telephoneContact, String mailContact, String siret, String localisation){
        //Compte compte = new Compte(uid,pseudo,imageProfile,estProfessionnel,telephoneContact,mailContact,siret,localisation);
        Map<String, Object> compte = new HashMap<>();
        compte.put("uid", uid);
        compte.put("pseudo", pseudo);
        compte.put("localisation", localisation);
        compte.put("imageProfile", imageProfile);
        compte.put("estProfessionnel", estProfessionnel);
        compte.put("siret", siret);
        compte.put("telephoneContact", telephoneContact);
        compte.put("mailContact", mailContact);

        cCompte.document(pseudo).set(compte)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(),"Compte ajouté !",Toast.LENGTH_SHORT).show();
                        ((MainActivity)getActivity()).updateUI( mAuth.getCurrentUser());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"erreur",Toast.LENGTH_SHORT).show();
                        Log.d(TAG,e.toString());
                    }
                });
    }

    // Check Validation Method
    private void checkValidation() {

        // Get all edittext texts
        String getFullName = fullName.getText().toString();
        String getEmailId = emailId.getText().toString();
        String getMobileNumber = mobileNumber.getText().toString();
        String getLocation = location.getText().toString();
        String getPassword = password.getText().toString();
        String getConfirmPassword = confirmPassword.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(getEmailId);

        Pattern pMdp = Pattern.compile(mdp);
        Matcher mMdp = p.matcher(getPassword);

        // Check if all strings are null or not
        if (getFullName.equals("") || getFullName.length() == 0
                || getEmailId.equals("") || getEmailId.length() == 0
                || getMobileNumber.equals("") || getMobileNumber.length() == 0
                || getLocation.equals("") || getLocation.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                || getConfirmPassword.equals("")
                || getConfirmPassword.length() == 0)

            new CustomToast().Show_Toast(getActivity(), view,
                    "Remplir les champs obligatoires");

            // Check if email id valid or not
        else if (!m.find())
            new CustomToast().Show_Toast(getActivity(), view,
                    "Email invalide.");

            // Check if both password should be equal
        else if (!getConfirmPassword.equals(getPassword))
            new CustomToast().Show_Toast(getActivity(), view,
                    "Les  deux mots de passes ne matchent pas");

       /* else if (!mMdp.find())
            new CustomToast().Show_Toast(getActivity(), view,
                    "8 caracteres/majuscule/digit/Special");

        */

            // Make sure user should check Terms and Conditions checkbox
        else if (!terms_conditions.isChecked())
            new CustomToast().Show_Toast(getActivity(), view,
                    "acceptez les conditions.");

        else if(getFullName.length() >0) {
            Task<DocumentSnapshot> tCompte = cCompte.document(getFullName).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Compte compte = documentSnapshot.toObject(Compte.class);

                    if (compte != null) {
                        new CustomToast().Show_Toast(getActivity(), view,"Pseudo déja pris.");
                    }
                    else{
                        String siretString = siret.getText().toString();
                        if(siretString.length()==0) {
                            inscription(getEmailId, getPassword,getFullName, false, getMobileNumber, null, getLocation);
                        }

                        else{
                            inscription(getEmailId,getPassword,getFullName,true,getMobileNumber,siretString,getLocation);
                        }
                    }
                }
            });
        }

            // Else do signup or do your stuff
        else {
            Toast.makeText(getActivity(), "Do SignUp.", Toast.LENGTH_SHORT).show();
        }

    }
}