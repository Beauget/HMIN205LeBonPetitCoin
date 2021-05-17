package com.example.lebonpetitcoin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebonpetitcoin.Adapter.AdapterMesAnnonces;
import com.example.lebonpetitcoin.Adapter.RecycleViewAnnonce;
import com.example.lebonpetitcoin.ClassFirestore.Annonce;
import com.example.lebonpetitcoin.MainActivity;
import com.example.lebonpetitcoin.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ModifierAnnoncesFragment extends Fragment {
    private static final String TAG = "ModifierAnnoncesFragment";

    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView mRecyclerView ;
    private String uid = "";
    String lecteur = "";

    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cAnnonces = firestoreDB.collection("Annonce");

    public static Fragment newInstance() {
        return (new ModifierAnnoncesFragment());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modifier_annonces, container, false);
        mRecyclerView = view.findViewById(R.id.LAnnonces);
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lecteur =  ((MainActivity)getActivity()).lecteur ;


        ArrayList<Annonce> mAnnonces =new ArrayList<>();
        ArrayList<String> mIds =new ArrayList<>();
        cAnnonces.orderBy("datePoste", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    Annonce annonce = documentSnapshot.toObject(Annonce.class);
                    if(annonce.getAuteur().equals(lecteur)){
                        mAnnonces.add(annonce);
                        mIds.add(documentSnapshot.getId());
                    }
                }
                mAdapter = new AdapterMesAnnonces(getContext(),mAnnonces,mIds,uid,"modifierAnonce");
                mRecyclerView.setAdapter(mAdapter);
                if (mRecyclerView.getAdapter().getItemCount() > 0) {
                    mRecyclerView.smoothScrollToPosition(0);
                }

            }
        });
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