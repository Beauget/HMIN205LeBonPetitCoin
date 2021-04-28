package com.example.lebonpetitcoin.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lebonpetitcoin.Adapter.AdapterAnnonce;
import com.example.lebonpetitcoin.Adapter.AdapterCategorie;
import com.example.lebonpetitcoin.Adapter.RecycleViewAnnonce;
import com.example.lebonpetitcoin.ClassFirestore.Annonce;
import com.example.lebonpetitcoin.ClassFirestore.Categorie;
import com.example.lebonpetitcoin.ClassFirestore.Compte;
import com.example.lebonpetitcoin.ClassFirestore.MoyenDePaiement;
import com.example.lebonpetitcoin.GlideApp;
import com.example.lebonpetitcoin.MainActivity;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/*
 * Ici sera l'ajout des catégories et des moyen de paiement
 *
 * */
public class ResultatFragment extends Fragment {

    //NOM DE LA CLASSE QUI SERA ENVOYÉ EN CAS D'ECHEC D'ENVOIE
    private static final String TAG = "ResultatFragment";


    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cAnnonces = firestoreDB.collection("Annonce");

    //Listener afin que la recherce dans la db se fasse pas quand l'application est en arrière plan
    private ListenerRegistration annonceListener;
    private FirestoreRecyclerAdapter adapter;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView mRecyclerView ;
    private String uid = "";

    //StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    RecyclerView recyclerView ;
    TextView tv;

    public static ResultatFragment newInstance() {
        return (new ResultatFragment());
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resultat, container, false);
        tv = (TextView) view.findViewById(R.id.textView);

        //addAnnonce("Annonce styley");

        recyclerView = (RecyclerView) view.findViewById(R.id.LResultat);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager
                (new LinearLayoutManager(view.getContext()));

        mRecyclerView = (RecyclerView) view.findViewById(R.id.RvResultat);
        mRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(view.getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (((MainActivity)getActivity()).mAuth.getCurrentUser() != null)
            uid = ((MainActivity)getActivity()).mAuth.getCurrentUser().getUid();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String mRecherche = bundle.getString("recherche","");
            ArrayList<String> mCategorie = bundle.getStringArrayList("categories");
            //defaultValue
            if (mCategorie==null)
            {
                mCategorie = new ArrayList<>();
            }

            String recherche = mRecherche;
            ArrayList<String> categories = mCategorie;
            ArrayList<Annonce> mAnnonces =new ArrayList<>();
            ArrayList<String> mIds =new ArrayList<>();


            cAnnonces.orderBy("datePoste", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        return;
                    }
                    String text = "Recherche : "+ mRecherche + "\n"+
                            "Categories : "+ categories.toString()+ "\n";
                    tv.setText(text);


                    for (QueryDocumentSnapshot documentSnapshot : value) {
                        Annonce annonce = documentSnapshot.toObject(Annonce.class);
                        if(research(annonce,recherche,categories))
                        {
                            mAnnonces.add(annonce);
                            mIds.add(documentSnapshot.getId());

                        }
                    }
                    mAdapter = new RecycleViewAnnonce(getContext(),mAnnonces,mIds,uid);
                    mRecyclerView.setAdapter(mAdapter);
                    if (mRecyclerView.getAdapter().getItemCount() > 0) {
                        mRecyclerView.smoothScrollToPosition(0);
                    }

                }
            });
        }

        else{
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
                        mAnnonces.add(annonce);
                        mIds.add(documentSnapshot.getId());
                    }
                    mAdapter = new RecycleViewAnnonce(getContext(),mAnnonces,mIds,uid);
                    mRecyclerView.setAdapter(mAdapter);
                    if (mRecyclerView.getAdapter().getItemCount() > 0) {
                        mRecyclerView.smoothScrollToPosition(0);
                    }

                }
            });

            /*
            Query query = cAnnonces.orderBy("datePoste", Query.Direction.DESCENDING);

            FirestoreRecyclerOptions<Annonce> options = new FirestoreRecyclerOptions.Builder<Annonce>()
                    .setQuery(query, Annonce.class)
                    .setLifecycleOwner(this)
                    .build();

            adapter = new AdapterAnnonce(options,getContext());

            recyclerView.setAdapter(adapter);

            if (recyclerView.getAdapter().getItemCount() > 0) {
                recyclerView.smoothScrollToPosition(0);
            }*/
        }
    }

    public boolean research(Annonce annonce,String recherche,ArrayList<String> categories){
        boolean vide = recherche.length()==0;
        String[] text = recherche.split(" ");
        String[] annonceT = annonce.getTitre().split(" ");
        ArrayList<String> annonceC =annonce.getCategories();


        for (String sAnnonce : annonceT){
            for (String sText : text )
            {
                if(sAnnonce.toLowerCase().equals(sText.toLowerCase()) || vide )
                {
                    if (categories.size()==0){
                        return true;
                    }
                    else{
                        for (String sCategorie : categories){
                            if(annonceC.contains(sCategorie))
                            {
                                return true;
                            }
                        }

                    }
                }
            }
        }
        return false;
    }


    @Override
    public void onStop() {
        super.onStop();
        //annonceListener.remove();
    }

    public void addAnnonce(String intitule){
        Annonce annonce= new Annonce(intitule);
        cAnnonces.add(annonce)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getContext(),"Annonce ajouté",Toast.LENGTH_SHORT).show();

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
        Query query = cAnnonces.orderBy("intitule", Query.Direction.ASCENDING);
        //Query query =cCategorie.whereEqualTo("intitule", true);

        //String r = query.toString();
        FirestoreRecyclerOptions<Annonce> options = new FirestoreRecyclerOptions.Builder<Annonce>()
                .setQuery(query, Annonce.class)
                .build();

        recyclerView = (RecyclerView) getView().findViewById(R.id.LCategorie);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }
    //apche query parser syntaxe
    //fts3 lucen
    //signIn workflow google android api
    //use case
}

