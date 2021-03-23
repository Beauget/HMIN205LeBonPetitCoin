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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

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
    Button buttonMerge;
    Button buttonDeleteDescription;
    Button buttonDeleteNote;

    TextView tLoad;

    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();

    //RCUPERATION OU CREATION DU DOCUMENT MY FIRST NOTE
    private DocumentReference noteReference = firestoreDB.collection("Note").document("My first note");
    ////firestoreDB.document("Note/My first note"); équivalent de la ligne dessus

    private ListenerRegistration noteListener;



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

        /*
        * Grace au onstart/onStop plus besoins de buton load
        * note :  firebase n'a pas vraiment récupérer le nouveau contenu du document.
        *           Il enregistre temporairement en local et le get le récupère donc en local pour etre rapide
        * */
        buttonLoad.setText("ancien boutton load devenu obselète depuis le onstart() (voir code)");
        //LoadNote(view);

        //met a jour la desciption
        buttonMerge= view.findViewById(R.id.buttonChangeDescription);
        updateDescription(view);

        //supprime l'attribue description
        buttonDeleteDescription = view.findViewById(R.id.buttonDeleteDescription);
        deleteDescription(view);

        //supprime My first note (et donc la collection note car elle est maintenant vide)
        buttonDeleteNote = view.findViewById(R.id.buttonDeleteNote);
        deleteNote(view);

        //Champs d'affichage du document
        tLoad= view.findViewById(R.id.Tload);


        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    //SO when the app isn't on the forground we don't want to consume useless data/updates
    @Override
    public void onStart() {
        super.onStart();
        noteListener= noteReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(getContext(), "erreur au chagement", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, error.toString());
                    return;
                }

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
        });
    }

    //il est possible de pas l'utiliser en instanciant dans le onStart :
    //      noteListener= noteReference.addSnapshotListener(this ,new EventListener<DocumentSnapshot>()   (ajout du this)
    // Mais comme c'est un fragement et pas une activité je préfère le faire comme ça
    @Override
    public void onStop() {
        super.onStop();
        noteListener.remove();
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

    public void updateDescription(View v){

        buttonMerge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = eDescription.getText().toString();

                /*Option 1*/
                Map<String , Object> note = new HashMap<>();
                note.put(KEY_DESCRIPTION,description);

                noteReference.set(note, SetOptions.merge());


                //UPDATE ne remarque pas la disparition du fichier quand on le supprime (et donc n'en crée pas un nouveau)

                /*Option 2
                Map<String , Object> note = new HashMap<>();
                note.put(KEY_DESCRIPTION,description);

                noteReference.update(note);
                */

                /*Option 3
                noteReference.update(KEY_DESCRIPTION,description);
                */

            }
        });
    }

    public void deleteDescription(View v){
        buttonDeleteDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteReference.update(KEY_DESCRIPTION, FieldValue.delete())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(),"description supprimé",Toast.LENGTH_SHORT).show();

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

    public void deleteNote(View v){
        buttonDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteReference.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //ce qui engendre la suppression de la collection car elle est maintenant vide
                                Toast.makeText(getContext(),"My first Note supprimé",Toast.LENGTH_SHORT).show();

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

