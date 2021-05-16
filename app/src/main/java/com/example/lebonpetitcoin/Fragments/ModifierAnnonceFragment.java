package com.example.lebonpetitcoin.Fragments;

import android.os.Bundle;
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
import com.example.lebonpetitcoin.Adapter.AdapterMoyenDePaiement;
import com.example.lebonpetitcoin.Adapter.RecycleViewAnnonce;
import com.example.lebonpetitcoin.ClassFirestore.Annonce;
import com.example.lebonpetitcoin.ClassFirestore.Categorie;
import com.example.lebonpetitcoin.ClassFirestore.MoyenDePaiement;
import com.example.lebonpetitcoin.GlideApp;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModifierAnnonceFragment extends Fragment {
    private static final String TAG = "ModifierAnnonceFragment";

    Annonce annonce;
    String id;
    TextView titreTv;
    EditText titre;
    TextView prixTv;
    EditText prix;
    TextView descriptionTv;
    EditText description;
    TextView categorieTv;
    TextView moyendepaiementTv;
    ArrayList<String> moyendepaiement;
    ArrayList<String> categorie;
    Button valider;

    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cAnnonce = firestoreDB.collection("Annonce");
    private CollectionReference cCategories = firestoreDB.collection("Categorie");
    private CollectionReference cMoyenDePaiement = firestoreDB.collection("MoyenDePaiement");
    private FirestoreRecyclerAdapter adapterCategorie;
    private  RecyclerView recyclerViewCategorie;
    private FirestoreRecyclerAdapter adapterMoyenDePaiement;
    private RecyclerView recyclerViewMoyenDePaiement;
    private ListenerRegistration categorieListener;
    private ListenerRegistration moyenDePaiementListener;


    public static Fragment newInstance() {
        return (new ModifierAnnonceFragment());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modifier_annonce, container, false);

        titreTv = view.findViewById(R.id.titre);
        titre = view.findViewById(R.id.titreEditText);

        prixTv = view.findViewById(R.id.prixTextView);
        prix = view.findViewById(R.id.prixEditText);

        descriptionTv = view.findViewById(R.id.descriptionTextView);
        description = view.findViewById(R.id.descriptionEditText);

        categorie = new ArrayList<>();
        categorieTv = view.findViewById(R.id.CategorieTextView);
        recyclerViewCategorie = view.findViewById(R.id.LCategorie);
        recyclerViewCategorie.setLayoutManager(new LinearLayoutManager(getContext()));

        moyendepaiement = new ArrayList<>();
        moyendepaiementTv = view.findViewById(R.id.moyenDePaiementTextView);
        recyclerViewMoyenDePaiement = view.findViewById(R.id.LMoyenDePaiement);
        recyclerViewMoyenDePaiement.setLayoutManager(new LinearLayoutManager(getContext()));

        valider = view.findViewById(R.id.valider);
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Query queryC = cCategories.orderBy("intitule", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Categorie> optionsC = new FirestoreRecyclerOptions.Builder<Categorie>()
                .setQuery(queryC, Categorie.class)
                .setLifecycleOwner(this)
                .build();
        adapterCategorie = new AdapterCategorie(optionsC,getContext(),categorie);
        recyclerViewCategorie.setAdapter(adapterCategorie);

        Query queryM = cMoyenDePaiement.orderBy("intitule", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<MoyenDePaiement> optionsM = new FirestoreRecyclerOptions.Builder<MoyenDePaiement>()
                .setQuery(queryM, MoyenDePaiement.class)
                .setLifecycleOwner(this)
                .build();
        adapterMoyenDePaiement = new AdapterMoyenDePaiement(optionsM,getContext(), moyendepaiement);
        recyclerViewMoyenDePaiement.setAdapter(adapterMoyenDePaiement);

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miseAJour(annonce);
            }
        });
    }

    public void miseAJour(Annonce annonce){
        assert annonce != null;
        String t = titre.getText().toString();
        String d = description.getText().toString();
        String p = prix.getText().toString();
        Map<String, Object> updates = new HashMap<>();

        if (t.length()>0) {
            updates.put("titre", t);
            description.setText("");
        }

        if (d.length()>0) {
            updates.put("description", d);
            description.setText("");
        }

        if (p.length()>0) {
            float prixBase =Float.parseFloat(prix.getText().toString());
            float getPrix = 0;
            int prixInteger = (int) (prixBase * 100);
            getPrix = prixInteger / 100;

            if (prixBase==getPrix && getPrix<9999){
            updates.put("prix", Float.parseFloat(p));
            }
            else{
                Toast.makeText(getContext(),"Erreur prix",Toast.LENGTH_SHORT).show();
            }
            prix.setText("");
        }

        if(categorie.size()>0){
            updates.put("categories", categorie);

        }

        if(moyendepaiement.size()>0){
            updates.put("paiement", categorie);

        }
        cAnnonce.document(id).update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                miseAjourInterface();
            }});

    }

    public void miseAjourInterface(){
        Task<DocumentSnapshot> tAnnonce = cAnnonce.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                annonce = documentSnapshot.toObject(Annonce.class);
                titreTv.setText(annonce.getTitre());

                descriptionTv.setText(annonce.getDescription());
                prixTv.setText(String.valueOf(annonce.getPrix()));

                if(annonce.getCategories()!=null){
                    categorieTv.setText(annonce.getCategories().toString());
                }
                if (annonce.getPaiement()!=null){
                    moyendepaiementTv.setText(annonce.getPaiement().toString());
                }
            }});

    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            id = bundle.getString("idAnnonce","");
        }

        if (id != null) {
            miseAjourInterface();

        }


    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
