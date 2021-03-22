package com.example.lebonpetitcoin.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lebonpetitcoin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.events.Event;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {
    public static MessageFragment newInstance() {
        return (new MessageFragment());
    }

    TextView test;
    private FirebaseFirestore firestoreDB;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        test= view.findViewById(R.id.collection);
        firestoreDB = FirebaseFirestore.getInstance();

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        //Gets a CollectionReference instance that refers to the collection at the specified path within the database.
        //CollectionReference categorieRef = FirebaseFirestore.getInstance().collection("Categorie");


        test.setText(firestoreDB.collection("Categorie").getId());
        super.onActivityCreated(savedInstanceState);
    }
}

//https://www.zoftino.com/firebase-cloud-firestore-databse-tutorial-android-example

