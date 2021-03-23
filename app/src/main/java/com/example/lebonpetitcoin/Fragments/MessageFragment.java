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
import androidx.fragment.app.Fragment;

import com.example.lebonpetitcoin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.events.Event;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageFragment extends Fragment {
    public static MessageFragment newInstance() {
        return (new MessageFragment());
    }

    TextView test;

    EditText eTitre;
    EditText eDescription;

    Button buttonSend;
    Button buttonLoad;

    TextView tLoad;

    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

    //RCUPERATION OU CREATION DU DOCUMENT MY FIRST NOTE
    private DocumentReference noteReference = firestoreDB.collection("Note").document("My first note");
    ////firestoreDB.document("Note/My first note"); équivalent de la ligne dessus



    //NOM DES ATTRIBUTS
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";

    //NOM DE LA CLASSE QUI SERA ENVOYÉ EN CAS D'ECHEC D'ENVOIE
    private static final String TAG = "MessageFragment";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        //texte simple
        test = view.findViewById(R.id.test);
        test.setText("Ci dessous les champs à remplir qui seront instancié dans le document My first note de la collection Note");

        //Champs d'écritures
        eTitre= view.findViewById(R.id.Etitle);
        eDescription = view.findViewById(R.id.Edescription);

        //boutton d'envoie du document
        buttonSend= view.findViewById(R.id.buttonSend);
        SendNote(view);

        //boutton de chargement du document
        buttonLoad= view.findViewById(R.id.buttonLoad);
        LoadNote(view);

        //Champs d'affichage du document
        tLoad= view.findViewById(R.id.Tload);


        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void SendNote(View v){

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * RECUPERATION DU TEXT A METTRE DANS LE DOCUMENT MY FIRST NOTE
                 */
                String title = eTitre.getText().toString();
                String description = eDescription.getText().toString();

                //Nommage des attributs
                Map<String, Object> note = new HashMap<>();
                note.put(KEY_TITLE, title);
                note.put(KEY_DESCRIPTION, description);

                /*
                 * CREATION DE LA COLLECTION NOTE ET D'UN DOCUMENT MY FIRST NOTE
                 */

                noteReference.set(note)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(),"note enregistré",Toast.LENGTH_SHORT).show();

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
        });

    }

    public void LoadNote(View v){
        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteReference.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()){
                                    String titre = documentSnapshot.getString(KEY_TITLE);
                                    String description = documentSnapshot.getString(KEY_DESCRIPTION);

                                    //Map<String,Object> note = documentSnapshot.getData(); Si on veux récupérer les deux d'un coup

                                    tLoad.setText("Titre : " + titre + "\n" + "Description : " + description);
                                }
                                else {
                                    Toast.makeText(getContext(), "Le document n'existe pas", Toast.LENGTH_SHORT).show();
                                }

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
        });

    }
}

//https://www.zoftino.com/firebase-cloud-firestore-databse-tutorial-android-example

