package com.example.lebonpetitcoin.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lebonpetitcoin.ClassFirestore.Compte;
import com.example.lebonpetitcoin.ClassFirestore.Statistique;
import com.example.lebonpetitcoin.MainActivity;
import com.example.lebonpetitcoin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class StatsFragment extends Fragment {

    private static final String TAG = "StatsFragment";
    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cStatistique = firestoreDB.collection("Statistique");
    private CollectionReference cCompte = firestoreDB.collection("Compte");

    TextView textView;
    int nbVisualisation = 0;


    public static StatsFragment newInstance() {
        return (new StatsFragment());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        textView = view.findViewById(R.id.textView);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCompte(((MainActivity)getActivity()).mAuth.getCurrentUser().getUid());
    }

    public void getStatistique(String pseudo){
        nbVisualisation = 0;

        Task<QuerySnapshot> query = cStatistique.whereEqualTo("idAuteur", pseudo).get();
        // future.get() blocks on response
        query.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        nbVisualisation++;
                        //Statistique statistique = document.toObject(Statistique.class);
                    }
                    textView.setText("Vos annonces ont étais visualisé : " +Integer.valueOf(nbVisualisation).toString() + " fois!");
                }
            }
        });
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
                        getStatistique(compte.getPseudo());

                    }
                }
            }
        });
    }
}
