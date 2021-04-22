package com.example.lebonpetitcoin.Fragments;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebonpetitcoin.Adapter.AdapterCategorie;
import com.example.lebonpetitcoin.ClassFirestore.Annonce;
import com.example.lebonpetitcoin.ClassFirestore.Categorie;
import com.example.lebonpetitcoin.ClassFirestore.MoyenDePaiement;
import com.example.lebonpetitcoin.GlideApp;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AnnonceFragment extends Fragment {

    TextView titre;
    TextView description;
    TextView auteur;
    TextView prix;
    TextView moyenDePaiements;
    TextView categories;
    TextView nbVisites;
    TextView date;
    ImageView image;

    private static final String TAG = "AnnonceFragment";

    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cAnnonce = firestoreDB.collection("Annonce");
    private CollectionReference cCategorie = firestoreDB.collection("Categorie");

    //Listener afin que la recherche dans la db se fasse pas quand l'application est en arrière plan
    private ListenerRegistration annonceListener;
    private ListenerRegistration categorieListener;
    private ListenerRegistration moyenDePaiementListener;
    ListView lv;
    private ChipGroup chipGroup;

    public static Fragment newInstance() {
            return (new AnnonceFragment());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_annonce, container, false);

        //Champs d'écritures
        titre= view.findViewById(R.id.titre);
        description = view.findViewById(R.id.all);
        image = view.findViewById(R.id.imageView);
        lv=view.findViewById(R.id.lv);
        chipGroup = (ChipGroup) view.findViewById(R.id.chipGroup);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void onStart() {
        super.onStart();
        //adapter.startListening();
        Bundle bundle = this.getArguments();
        final ArrayList<String>[] idCategorie = new ArrayList[]{new ArrayList<>()};

        String id = null;

        if (bundle != null) {
            id = bundle.getString("idAnnonce");
        }

        if (id != null) {
            String finalId = id;
            Task<DocumentSnapshot> tAnnonce = cAnnonce.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                    Annonce annonce = documentSnapshot.toObject(Annonce.class);

                    assert annonce != null;
                    Integer nb = (annonce.getNbDeVisites() + 1);

                    Map<String, Object> updates = new HashMap<>();
                    updates.put("nbDeVisites", nb);

                    cAnnonce.document(finalId).update(updates);

                    titre.setText(annonce.getTitre());
                    GlideApp.with(getContext())
                            .load(annonce.getFirstImage())
                            .into(image);
                    String text = "Description : " + annonce.getDescription() + "\n" +
                            "auteur : " + annonce.getAuteur() + "\n" +
                            "prix : " + String.valueOf(annonce.getPrix()) + "\n" +
                            "Date : " + dateFormat.format(annonce.getDatePoste()) + "\n" ;


                    text+="Moyen de paiement : ";
                    for(String s : annonce.getPaiement())
                    {
                        text+=s+" ";
                    }
                    text+="\n";

                    text+="Categorie : ";
                    for(String s : annonce.getCategories())
                    {
                        text+=s+" ";
                    }
                    text+="\n";

                    description.setText(text);

                    /*
                    for(String s : annonce.getCategories())
                    {
                        LayoutInflater inflater = LayoutInflater.from(getContext());
                        Chip newChip = new Chip(getContext(), null, R.style.Widget_MaterialComponents_Chip_Action);
                        newChip.setText(s);
                        // Other methods:
                        //
                        // newChip.setCloseIconVisible(true);
                        // newChip.setCloseIconResource(R.drawable.your_icon);
                        // newChip.setChipIconResource(R.drawable.your_icon);
                        // newChip.setChipBackgroundColorResource(R.color.red);
                        // newChip.setTextAppearanceResource(R.style.ChipTextStyle);
                        // newChip.setElevation(15);

                        chipGroup.addView(newChip);
                    }*/
                }
            });
        }

            /*
            ArrayList<String> rslt = new ArrayList<>();

           // Query query = cCategorie.whereIn("id", idCategorie[0]);
            Query query = cCategorie.whereIn("status", Arrays.asList("Sport", "sent");
            cCategorie.where(documentId(), 'in', ["123","456","789"])

            if (idCategorie[0].size()!=0){

                for (String x : idCategorie[0]) {
                    DocumentReference docRef = cCategorie.document(x);
                    docRef.get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            rslt.add(document.getString("intitule"));
                                            description.setText(document.getString("intitule"));
                                        } else {
                                            Log.d("TAG", "No such document");
                                        }
                                    } else {
                                        Log.d("TAG", "get failed with ", task.getException());
                                    }
                                }
                            });
                }
                //text[0]+= rslt.toString();
            }
            //description.setText(rslt.toString());
        }

             */

        /*
        cAnnonce.document(id).get();
        annonceListener = cAnnonce.orderBy("intitule", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                description.setText(text);
            }
        });*/
    }

    public ArrayList<String> getCategorie(ArrayList<String> categories){
        ArrayList<String> rslt = new ArrayList<>();

        if (categories.size()==0)
            return rslt;

        for (String id : categories) {
            DocumentReference docRef = cCategorie.document(id);
            docRef.get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    rslt.add(document.getString("intitule"));
                                    //Log.d("TAG", cityName);
                                } else {
                                    Log.d("TAG", "No such document");
                                }
                            } else {
                                Log.d("TAG", "get failed with ", task.getException());
                            }
                            Toast.makeText(getContext(),rslt.toString(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        return rslt;
    }

    /*

        for (String id : categories){
            rslt.add("avant");
            cCategorie.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {;
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Categorie categorie = documentSnapshot.toObject(Categorie.class);

                if(categorie!=null)
                {
                    rslt.add(categorie.getIntitule());
                    rslt.add("success");
                }
                else {rslt.add("null");}
            }
        });
        }
        return rslt;}*/
}
