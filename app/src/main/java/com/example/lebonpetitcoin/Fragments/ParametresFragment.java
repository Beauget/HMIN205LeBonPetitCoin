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

import com.example.lebonpetitcoin.Adapter.AdapterCategorie;
import com.example.lebonpetitcoin.ClassFirestore.Categorie;
import com.example.lebonpetitcoin.ClassFirestore.MoyenDePaiement;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/*
 * Ici sera l'ajout des catégories et des moyen de paiement
 *
 * */
public class ParametresFragment extends Fragment {

    //NOM DE LA CLASSE QUI SERA ENVOYÉ EN CAS D'ECHEC D'ENVOIE
    private static final String TAG = "ParametresFragment";

    //Zone contenant les intitulés
    EditText eCategorie;
    EditText eMoyenDePaiement;

    //Boutton d'envoie
    Button bCategorie;
    Button bMoyenDePaiement;

    //Zone affichage des collections
    TextView tCategorie;
    TextView tMoyenDePaiement;
    TextView test;

    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cCategorie = firestoreDB.collection("Categorie");
    private CollectionReference cMoyenDePaiement = firestoreDB.collection("MoyenDePaiement");

    //Listener afin que la recherce dans la db se fasse pas quand l'application est en arrière plan
    private ListenerRegistration categorieListener;
    private ListenerRegistration moyenDePaiementListener;
    private FirestoreRecyclerAdapter adapter;
    RecyclerView recyclerView ;

    public static ParametresFragment newInstance() {
        return (new ParametresFragment());
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parametres, container, false);

        //Champs d'écritures
        eCategorie= view.findViewById(R.id.ETCategorie);
        eMoyenDePaiement = view.findViewById(R.id.ETMoyenDePaiement);

        test=view.findViewById(R.id.titreeeee);

        //boutton d'envoie des documents
        bCategorie= view.findViewById(R.id.BtnCategorie);
        bMoyenDePaiement =view.findViewById(R.id.BtnMoyenDePaiement);

        //Champs d'affichage du des collections
        tCategorie = view.findViewById(R.id.LC2);
        tMoyenDePaiement = view.findViewById(R.id.LMoyenDePaiement);

        //recyclerView = (RecyclerView) view.findViewById(R.id.LCategorie);
        //recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));



        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        confBtnCategorie();
        confBtnMoyenDePaiement();
        //setUpRecyclerView();

        Query query = cCategorie.orderBy("intitule", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Categorie> options = new FirestoreRecyclerOptions.Builder<Categorie>()
                .setQuery(query, Categorie.class)
                .setLifecycleOwner(this)
                .build();


        //adapter = new AdapterCategorie(options,getContext(),new ArrayList<String>());
        //recyclerView.setAdapter(adapter);



    }

    @Override
    public void onStart() {
        super.onStart();
        //adapter.startListening();

        categorieListener = cCategorie.orderBy("intitule", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                String text = "";
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    Categorie categorie = documentSnapshot.toObject(Categorie.class);
                    categorie.setId(documentSnapshot.getId());

                    text += "ID : " + categorie.getId() + "\n"
                            + categorie.getIntitule() + "\n\n";
                }
                tCategorie.setText(text);
            }
        });


        moyenDePaiementListener = cMoyenDePaiement.orderBy("intitule", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                String text = "";
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    MoyenDePaiement moyenDePaiement = documentSnapshot.toObject(MoyenDePaiement.class);
                    moyenDePaiement.setId(documentSnapshot.getId());

                    text += "ID : " + moyenDePaiement.getId() + "\n"
                            + moyenDePaiement.getIntitule() + "\n\n";
                }
                tMoyenDePaiement.setText(text);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        categorieListener.remove();
        moyenDePaiementListener.remove();
    }

    public void confBtnCategorie(){
        bCategorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String intitule = eCategorie.getText().toString();
                if(intitule.equals("")){
                    Toast.makeText(getContext(), "Champ vide", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Appel addCategorie avec le bool qui convient
                inCategorie(intitule);
            }}
            );
    }

    public void inCategorie(String intitule ){
        intitule = intitule.toLowerCase();
        String finalIntitule = intitule;
        Task<QuerySnapshot> querySnapshotTask = cCategorie.whereEqualTo("intitule",intitule).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean rslt=false;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    rslt = true;
                                }
                            }
                            if(rslt){
                                Toast.makeText(getContext(), "Cet intitulé existe déja", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                addCategorie(finalIntitule);
                            }
                        } else{//erreur a la lecture
                            Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void addCategorie(String intitule){
        Categorie categorie = new Categorie(intitule);
        cCategorie.add(categorie)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getContext(),"Catégorie ajouté",Toast.LENGTH_SHORT).show();

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

    public void confBtnMoyenDePaiement(){
        bMoyenDePaiement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String intitule = eMoyenDePaiement.getText().toString();
                if(intitule.equals("")){
                    Toast.makeText(getContext(), "Champ vide", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Appel addCategorie avec le bool qui convient
                inMoyenDePaiement(intitule);
            }}
        );
    }

    public void inMoyenDePaiement(String intitule ){
        intitule = intitule.toLowerCase();
        String finalIntitule = intitule;
        Task<QuerySnapshot> querySnapshotTask = cMoyenDePaiement.whereEqualTo("intitule",intitule).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean rslt=false;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    rslt = true;
                                }
                            }
                            if(rslt){
                                Toast.makeText(getContext(), "Cet intitulé existe déja", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                addMoyenDePaiement(finalIntitule);
                            }
                        } else{//erreur a la lecture
                            Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void addMoyenDePaiement(String intitule){
        MoyenDePaiement moyenDePaiement= new MoyenDePaiement(intitule);
        cMoyenDePaiement.add(moyenDePaiement)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getContext(),"MDP ajouté",Toast.LENGTH_SHORT).show();

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

        //solution sans listener
        public void loadCategorie(View v){
            cCategorie.orderBy("intitule", Query.Direction.ASCENDING).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            //QueryDocumentSnapshot est la super classe de DocumentSnapshot
                            String text = "";
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                Categorie categorie = documentSnapshot.toObject(Categorie.class);

                                categorie.setId(documentSnapshot.getId());//pas du tout important dans la classe

                                text += "ID : " + categorie.getId() + "\n"
                                        + categorie.getIntitule() + "\n\n";
                            }
                            tCategorie.setText(text);
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


    private void setUpRecyclerView() {
        Query query = cCategorie.orderBy("intitule", Query.Direction.ASCENDING);
        //Query query =cCategorie.whereEqualTo("intitule", true);

        //String r = query.toString();
        FirestoreRecyclerOptions<Categorie> options = new FirestoreRecyclerOptions.Builder<Categorie>()
                .setQuery(query, Categorie.class)
                .build();
        //test.setText(options.toString());

        //FirestoreRecyclerAdapter adapter = getFirestoreRecyclerAdapter();
        //test.setText(String.valueOf(options.getSnapshots().size()));


        //adapter = new AdapterCategorie(options);


        recyclerView = (RecyclerView) getView().findViewById(R.id.LCategorie);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        test.setText(String.valueOf(adapter.getItemCount()));
    }
    //apche query parser syntaxe
    //fts3 lucen
    //signIn workflow google android api
    //use case
}

