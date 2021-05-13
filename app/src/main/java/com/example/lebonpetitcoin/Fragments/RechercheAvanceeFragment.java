package com.example.lebonpetitcoin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class RechercheAvanceeFragment extends Fragment {
    private static final String TAG = "RechercheAvanceeragment";

    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cCategorie = firestoreDB.collection("Categorie");

    private FirestoreRecyclerAdapter adapterCategorie;
    RecyclerView recyclerViewCategorie;
    Button rechercheButton;
    EditText rechercheText;

    public static Fragment newInstance() {
        return (new RechercheAvanceeFragment());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recherche_avancee, container, false);
        recyclerViewCategorie = view.findViewById(R.id.LCategorie);
        recyclerViewCategorie.setLayoutManager(new LinearLayoutManager(getContext()));
        rechercheButton = view.findViewById(R.id.rechercheButton);
        rechercheText= view.findViewById(R.id.rechercheEditText);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Query queryC = cCategorie.orderBy("intitule", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Categorie> optionsC = new FirestoreRecyclerOptions.Builder<Categorie>()
                .setQuery(queryC, Categorie.class)
                .setLifecycleOwner(this)
                .build();
        ArrayList<String> arrayListCategorie = new ArrayList<>();
        adapterCategorie = new AdapterCategorie(optionsC,getContext(),arrayListCategorie);
        recyclerViewCategorie.setAdapter(adapterCategorie);

        rechercheButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Fragment fragment = ResultatFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putString("recherche", rechercheText.getText().toString());
                bundle.putStringArrayList("categories", arrayListCategorie);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_main_frame_layout, fragment).commit();
            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        adapterCategorie.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterCategorie.stopListening();
    }
}
