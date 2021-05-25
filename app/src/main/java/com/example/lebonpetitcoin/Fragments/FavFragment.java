package com.example.lebonpetitcoin.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebonpetitcoin.Adapter.AdapterAnnonce;
import com.example.lebonpetitcoin.Adapter.AdapterConversation;
import com.example.lebonpetitcoin.Adapter.AdapterFavoris;
import com.example.lebonpetitcoin.ClassFirestore.Favoris;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FavFragment extends Fragment {

    private static final String TAG = "FavFragment";

    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cFavoris= firestoreDB.collection("Favoris");

    private RecyclerView recyclerView;
    private AdapterFavoris adapter;
    public FirebaseAuth mAuth;



    public static FavFragment newInstance() {
        return (new FavFragment());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav, container, false);
        recyclerView = view.findViewById(R.id.reyclerview_fav_list);
        mAuth = FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String uid = "";

        if(mAuth.getCurrentUser()!=null)
            uid = mAuth.getCurrentUser().getUid();

        Query query = cFavoris.orderBy("date", Query.Direction.DESCENDING).whereEqualTo("uid",uid);
        FirestoreRecyclerOptions<Favoris> options = new FirestoreRecyclerOptions.Builder<Favoris>()
                .setQuery(query, Favoris.class)
                .build();
        adapter = new AdapterFavoris(options,getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                //adapter.notifyItemRemoved(position);
                //Favoris favoris = adapter.getItem(position);
                DocumentSnapshot snapshot = adapter.getSnapshots().getSnapshot(position);
                String idFavoris = snapshot.getId();
                delete(idFavoris);

            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    public void delete(String idFavoris){
        //Toast.makeText(getContext(),idFavoris, Toast.LENGTH_SHORT).show();
        cFavoris.document(idFavoris).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Favoris "+idFavoris+" successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }


}
