package com.example.lebonpetitcoin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebonpetitcoin.Adapter.AdapterCategorie;
import com.example.lebonpetitcoin.ClassFirestore.Categorie;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class MiseAJourAccountFragment extends Fragment {
    private static final String TAG = "MiseAJourAccountFragment";
    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cContacte = firestoreDB.collection("Contacte");

    ImageView imageProfile;
    ArrayList<String> contacte;
    private RecyclerView recyclerViewContacte;
    private FirestoreRecyclerAdapter adapterContacte;
    Button valider;

    public static MiseAJourAccountFragment newInstance() {
        return (new MiseAJourAccountFragment());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maj_account, container, false);
        contacte= new ArrayList<>();
        recyclerViewContacte= view.findViewById(R.id.LContacte);
        recyclerViewContacte.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Query query = cContacte.orderBy("intitule", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Categorie> optionsC = new FirestoreRecyclerOptions.Builder<Categorie>()
                .setQuery(query, Categorie.class)
                .setLifecycleOwner(this)
                .build();
        adapterContacte = new AdapterCategorie(optionsC,getContext(),contacte);
        recyclerViewContacte.setAdapter(adapterContacte);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
