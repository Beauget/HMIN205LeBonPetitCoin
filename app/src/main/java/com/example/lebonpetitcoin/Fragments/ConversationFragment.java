package com.example.lebonpetitcoin.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebonpetitcoin.Adapter.AdapterConversation;
import com.example.lebonpetitcoin.Adapter.AdapterMessage;
import com.example.lebonpetitcoin.ClassFirestore.Conversation;
import com.example.lebonpetitcoin.ClassFirestore.Message;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;


public class ConversationFragment extends Fragment {

    private static final String TAG = "ConversationFragment";

    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cMessage = firestoreDB.collection("Message");

    private ListenerRegistration conversationListener;



    private String id;
    private String lecteur;
    private RecyclerView recyclerView;
    private EditText messageAEnvoyer;
    private Button envoyerMessage;
    private AdapterMessage adapter;


    public static ConversationFragment newInstance() {
        return (new ConversationFragment());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);

        recyclerView =  view.findViewById(R.id.reyclerview_message_list);
        messageAEnvoyer =  view.findViewById(R.id.edittext_message);
        envoyerMessage = view.findViewById(R.id.button_message);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            id = bundle.getString("idConversation","");
            lecteur = bundle.getString("lecteur","");
        }

        Query query = cMessage.orderBy("date", Query.Direction.ASCENDING).whereEqualTo("idConversation",id);
        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();
        adapter = new AdapterMessage(options,getContext(),lecteur,"");
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        envoyerMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                envoyerMessage(id,lecteur);
            }
        });
    }

    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    void envoyerMessage(String idConversation, String auteur){
        if (messageAEnvoyer.getText().toString().length()>0)
        {
            Message message = new Message(idConversation,auteur, messageAEnvoyer.getText().toString(), "");

            cMessage.add(message)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getContext(),"msg envoyé",Toast.LENGTH_SHORT).show();

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
        messageAEnvoyer.setText("");
    }
}
