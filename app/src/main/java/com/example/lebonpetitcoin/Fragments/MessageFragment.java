package com.example.lebonpetitcoin.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebonpetitcoin.Adapter.AdapterConversation;
import com.example.lebonpetitcoin.ClassFirestore.Annonce;
import com.example.lebonpetitcoin.ClassFirestore.Compte;
import com.example.lebonpetitcoin.ClassFirestore.Conversation;
import com.example.lebonpetitcoin.GlideApp;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.events.Event;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.util.Listener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageFragment extends Fragment {
    private static final String TAG = "MessageFragment";

    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cConversation = firestoreDB.collection("Conversation");
    private CollectionReference cCompte= firestoreDB.collection("Compte");

    private RecyclerView recyclerViewMesAnnonces;
    private RecyclerView recyclerViewAnnonces;
    public FirebaseAuth mAuth;
    private Listener conversationListener;
    private AdapterConversation adapterMesAnnonce;
    private AdapterConversation adapterAnnonce;
    private String lecteur;

    public static MessageFragment newInstance() {
        return (new MessageFragment());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        recyclerViewAnnonces = view.findViewById(R.id.reyclerview_annonces);
        recyclerViewMesAnnonces = view.findViewById(R.id.reyclerview_mes_annonces);
        mAuth = FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String uid = mAuth.getCurrentUser().getUid();
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            lecteur = bundle.getString("lecteur","");
            //Toast.makeText(getContext(),lecteur,Toast.LENGTH_SHORT).show();
        }

        Query query = cConversation.orderBy("date", Query.Direction.DESCENDING).whereEqualTo("compte2",lecteur);
        FirestoreRecyclerOptions<Conversation> options = new FirestoreRecyclerOptions.Builder<Conversation>()
                .setQuery(query, Conversation.class)
                .build();
        adapterAnnonce = new AdapterConversation(options,getContext(),lecteur);
        recyclerViewAnnonces.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewAnnonces.setAdapter(adapterAnnonce);


        Query query2 = cConversation.orderBy("date", Query.Direction.DESCENDING).whereEqualTo("compte1",lecteur);
        FirestoreRecyclerOptions<Conversation> options2 = new FirestoreRecyclerOptions.Builder<Conversation>()
                .setQuery(query2, Conversation.class)
                .build();
        adapterMesAnnonce = new AdapterConversation(options2,getContext(),lecteur);
        recyclerViewMesAnnonces.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewMesAnnonces.setAdapter(adapterMesAnnonce);

    }

    @Override
    public void onStart() {
        super.onStart();
        adapterMesAnnonce.startListening();
        adapterAnnonce.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        adapterMesAnnonce.stopListening();
        adapterAnnonce.stopListening();
    }

    public void getConversationAuteur(String uid){
        Task<QuerySnapshot> query = cCompte.whereEqualTo("uid", uid).get();
        query.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Compte compte = document.toObject(Compte.class);
                        lecteur = compte.getPseudo();
                        Toast.makeText(getContext(),lecteur,Toast.LENGTH_SHORT).show();
                        Query query = cConversation.orderBy("date", Query.Direction.DESCENDING).whereEqualTo("compte1",lecteur);
                        FirestoreRecyclerOptions<Conversation> options = new FirestoreRecyclerOptions.Builder<Conversation>()
                                .setQuery(query, Conversation.class)
                                .build();
                        //adapterMesAnnonce = new AdapterConversation(options,getContext(),lecteur);
                        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        //recyclerViewMesAnnonces.setAdapter(adapterMesAnnonce);
                    }
                }
            }
        });
    }

}

//https://www.zoftino.com/firebase-cloud-firestore-databse-tutorial-android-example

