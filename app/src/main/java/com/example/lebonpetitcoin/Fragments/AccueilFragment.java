package com.example.lebonpetitcoin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.lebonpetitcoin.Adapter.AdapterCategorie;
import com.example.lebonpetitcoin.ClassFirestore.Annonce;
import com.example.lebonpetitcoin.ClassFirestore.Categorie;
import com.example.lebonpetitcoin.GlideApp;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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
    private static final String TAG = "ResultatFragment";


    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cAnnonces = firestoreDB.collection("Annonce");

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

    public static AccueilFragment newInstance() {
        return (new AccueilFragment());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accueil, container, false);

        img1 = view.findViewById(R.id.img1);
        img2 = view.findViewById(R.id.img2);
        img3 = view.findViewById(R.id.img3);
        img4 = view.findViewById(R.id.img4);

        titre1 = view.findViewById(R.id.titre1);
        titre2 = view.findViewById(R.id.titre2);
        titre3 = view.findViewById(R.id.titre3);
        titre4 = view.findViewById(R.id.titre4);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }


    @Override
    public void onStart() {
        super.onStart();
        //adapter.startListening();


        annonceListener = cAnnonces.orderBy("datePoste", Query.Direction.ASCENDING).limit(4).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }

                int i = 0;
                List<ImageView> listImages = new ArrayList<>();
                listImages.add(img1);
                listImages.add(img2);
                listImages.add(img3);
                listImages.add(img4);

                List<TextView> listTitres = new ArrayList<>();
                listTitres.add(titre1);
                listTitres.add(titre2);
                listTitres.add(titre3);
                listTitres.add(titre4);
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    Annonce annonce = documentSnapshot.toObject(Annonce.class);
                    String s;
                    String id = documentSnapshot.getId();

                    //si pas d'image, une générique
                    if( annonce.getImages().size()==0)
                        s = "https://firebasestorage.googleapis.com/v0/b/lebonpetitcoin-6928c.appspot.com/o/seal2.jpg?alt=media&token=41553fe0-e1b7-4712-8eb1-043f2fd07d16";
                    else
                        s = annonce.getFirstImage();
                    //On ajoute l'image
                    GlideApp.with(getContext())
                            .load(s)
                            .into(listImages.get(i));


                    /*
                    listImages.get(i).setOnClickListener(new View.OnClickListener() {
                        private Fragment fragmentAnnonce;
                        @Override
                        public void onClick(View v) {
                            if (this.fragmentAnnonce == null) this.fragmentAnnonce= AnnonceFragment.newInstance();
                            Bundle arguments = new Bundle();
                            arguments.putString( "idAnnonce", id);
                            fragmentAnnonce.setArguments(arguments);
                            ((AppCompatActivity) getContext()).getSupportFragmentManager()
                                    .beginTransaction().replace(R.id.activity_main_frame_layout, fragmentAnnonce).commit();
                        }
                    });*/

                    //Recupération et ajout du titre
                    if (annonce.getTitre()!=null)
                    {
                        listTitres.get(i).setText(annonce.getTitre());
                    }
                    i++;
                }
            }
        });
    }
}
