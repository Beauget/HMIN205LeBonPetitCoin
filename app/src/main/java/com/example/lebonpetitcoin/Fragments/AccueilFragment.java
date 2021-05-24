package com.example.lebonpetitcoin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebonpetitcoin.Adapter.AdapterArticle;
import com.example.lebonpetitcoin.Adapter.AdapterCategorie;
import com.example.lebonpetitcoin.Adapter.AdapterCategorieAcceuil;
import com.example.lebonpetitcoin.Adapter.AdapterFavoris;
import com.example.lebonpetitcoin.ClassFirestore.Annonce;
import com.example.lebonpetitcoin.ClassFirestore.Categorie;
import com.example.lebonpetitcoin.ClassFirestore.Favoris;
import com.example.lebonpetitcoin.GlideApp;
import com.example.lebonpetitcoin.MainActivity;
import com.example.lebonpetitcoin.ProductGridItemDecoration;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
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

public class AccueilFragment extends Fragment {

    //NOM DE LA CLASSE QUI SERA ENVOYÉ EN CAS D'ECHEC D'ENVOIE
    private static final String TAG = "AcceuilFragment";


    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cAnnonces = firestoreDB.collection("Annonce");
    private CollectionReference cCategorie = firestoreDB.collection("Categorie");

    private FirestoreRecyclerAdapter adapterCategorie;
    RecyclerView recyclerViewCategorie;

    //Listener afin que la recherce dans la db se fasse pas quand l'application est en arrière plan
    private ListenerRegistration annonceListener;

    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;

    TextView titre1;
    TextView titre2;
    TextView titre3;
    TextView titre4;

    private RecyclerView recyclerView;
    private AdapterArticle adapter;
    public FirebaseAuth mAuth;
    public String uid = "";


    public static AccueilFragment newInstance() {
        return (new AccueilFragment());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accueil, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerViewCategorie = view.findViewById(R.id.LCategorie);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(((MainActivity)getActivity()).mAuth.getCurrentUser()!=null)
            uid = ((MainActivity)getActivity()).mAuth.getCurrentUser().getUid();

        Query query = cAnnonces.orderBy("datePoste", Query.Direction.DESCENDING).limit(4);
        FirestoreRecyclerOptions<Annonce> options = new FirestoreRecyclerOptions.Builder<Annonce>()
                .setQuery(query, Annonce.class)
                .build();
        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);

        adapter = new AdapterArticle(options,getContext(),uid);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));
        recyclerView.setAdapter(adapter);

        Query queryC = cCategorie.orderBy("intitule", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Categorie> optionsC = new FirestoreRecyclerOptions.Builder<Categorie>()
                .setQuery(queryC, Categorie.class)
                .setLifecycleOwner(this)
                .build();

        adapterCategorie = new AdapterCategorieAcceuil(optionsC,getContext());
        recyclerViewCategorie.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategorie.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));
        recyclerViewCategorie.setAdapter(adapterCategorie);


    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }



    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
