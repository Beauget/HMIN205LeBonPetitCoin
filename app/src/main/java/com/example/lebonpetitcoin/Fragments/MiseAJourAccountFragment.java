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
import com.example.lebonpetitcoin.ClassFirestore.Compte;
import com.example.lebonpetitcoin.GlideApp;
import com.example.lebonpetitcoin.MainActivity;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MiseAJourAccountFragment extends Fragment {
    private static final String TAG = "MiseAJourAccountFragment";
    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cContacte = firestoreDB.collection("Contacte");
    private CollectionReference cCompte= firestoreDB.collection("Compte");

    ImageView imageProfile;
    ArrayList<String> contacte;
    private RecyclerView recyclerViewContacte;
    private FirestoreRecyclerAdapter adapterContacte;
    Button valider;
    String uid = "";

    public static MiseAJourAccountFragment newInstance() {
        return (new MiseAJourAccountFragment());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maj_account, container, false);
        valider= view.findViewById(R.id.miseAjourButton);
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

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCompte();
            }
        });
    }

    public  void getCompte(){
        uid = ((MainActivity)getActivity()).mAuth.getCurrentUser().getUid();
        Task<QuerySnapshot> query = cCompte.whereEqualTo("uid", uid).get();
        // future.get() blocks on response
        query.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();
                        Compte compte = document.toObject(Compte.class);
                        Map<String, Object> updates = new HashMap<>();
                        if(contacte.size()>0){
                            updates.put("contacte", contacte);
                        }
                        cCompte.document(id).update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                miseAjourInterface();
                            }});

                    }
                }
            }
        });
    }
    public void miseAjourInterface(){}
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
