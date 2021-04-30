package com.example.lebonpetitcoin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.lebonpetitcoin.AddAnnonceActivity;
import com.example.lebonpetitcoin.ClassFirestore.Compte;
import com.example.lebonpetitcoin.GlideApp;
import com.example.lebonpetitcoin.MainActivity;
import com.example.lebonpetitcoin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AccountFragment extends Fragment {

    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cCompte= firestoreDB.collection("Compte");

    public static AccountFragment newInstance() {
        return (new AccountFragment());
    }
    ImageView imageProfile;
    TextView compteTv;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        compteTv=view.findViewById(R.id.compteTv);
        imageProfile=view.findViewById(R.id.imageProfile);

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getCompte(((MainActivity)getActivity()).mAuth.getCurrentUser().getUid());
    }

    public void getCompte(String uid){
        Task<QuerySnapshot> query = cCompte.whereEqualTo("uid", uid).get();
        // future.get() blocks on response
        query.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Compte compte = document.toObject(Compte.class);
                        String text = "Pseudo : " +compte.getPseudo()+ "\n"+
                                "tel : " +compte.getTelephoneContact()+ "\n"+
                                "e-mail: " +compte.getMailContact() +"\n"+
                                "Localisation : " +compte.getLocalisation()+ "\n";
                        if (compte.getSiret()!=null)
                                text += "Siret: " +compte.getSiret()+ "\n";

                        compteTv.setText(text);

                        if (compte.getImageProfile().length()>0){
                            GlideApp.with(getContext())
                                    .load(compte.getImageProfile())
                                    .into(imageProfile);
                        }
                    }
                }
            }
        });
    }

}
