package com.example.lebonpetitcoin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.lebonpetitcoin.Adapter.RecycleViewAnnonce;
import com.example.lebonpetitcoin.AddAnnonceActivity;
import com.example.lebonpetitcoin.ClassFirestore.Annonce;
import com.example.lebonpetitcoin.ClassFirestore.Compte;
import com.example.lebonpetitcoin.CustomToast;
import com.example.lebonpetitcoin.GlideApp;
import com.example.lebonpetitcoin.MainActivity;
import com.example.lebonpetitcoin.ProductGridItemDecoration;
import com.example.lebonpetitcoin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AccountFragment extends Fragment {

    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cCompte= firestoreDB.collection("Compte");
    private CollectionReference cAnnonces = firestoreDB.collection("Annonce");


    public static AccountFragment newInstance() {
        return (new AccountFragment());
    }
    View majView;
    ImageView imageProfile;
    TextView emailTV, telTV,pseudoTV, titreTV;
    TextView miseAjourButton;
    Chip chipEmail;
    Chip chipTel;
    Chip chipMsg;
    RelativeLayout moyenDeContacte;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView mRecyclerView ;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        imageProfile=view.findViewById(R.id.imageProfile);
        emailTV = view.findViewById(R.id.email);
        telTV = view.findViewById(R.id.tel);
        pseudoTV = view.findViewById(R.id.pseudo);

        titreTV = view.findViewById(R.id.titre);

        majView=view.findViewById(R.id.MAJView);
        miseAjourButton= view.findViewById(R.id.miseAjourButton);

        moyenDeContacte = view.findViewById(R.id.moyenDeContacte);
        chipEmail= view.findViewById(R.id.chipEmail);
        chipTel = view.findViewById(R.id.chipTelephone);
        chipMsg = view.findViewById(R.id.chipMessagerie);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.LAnnonces);



        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);

        mRecyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));

    }

    public void getCompteSelf(String uid){
        Task<QuerySnapshot> query = cCompte.whereEqualTo("uid", uid).get();
        final String[] txt = new String[1];
        query.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Compte compte = document.toObject(Compte.class);

                        setAnnonce(compte.getPseudo());
                        pseudoTV.setText(compte.getPseudo());
                        telTV.setText(compte.getTelephoneContact());
                        emailTV.setText(compte.getMailContact());
                        majView.setVisibility(View.VISIBLE);

                        if (compte.getSiret()!=null)
                            titreTV.setText(getText(R.string.infos_du_vendeur_pro) +"\n"+ getString(R.string.siret) + " : "+ compte.getSiret());
                        if(compte.getContacte()!=null &&compte.getContacte().size()>0 &&compte.getContacte().size()<3){
                            moyenDeContacte.setVisibility(View.VISIBLE);
                            majView.setVisibility(View.VISIBLE);
                            for(String s : compte.getContacte())
                            {
                                if(s.equals("E-mail")){chipEmail.setVisibility(View.VISIBLE);}
                                if(s.equals("Messagerie")){chipMsg.setVisibility(View.VISIBLE);}
                                if(s.equals("Téléphone")){chipTel.setVisibility(View.VISIBLE);}
                            }
                        }

                        if (compte.getImageProfile().length()>0){
                            GlideApp.with(getContext())
                                    .load(compte.getImageProfile())
                                    .centerCrop()
                                    .into(imageProfile);
                        }
                        else{
                            GlideApp.with(getContext())
                                    .load("https://firebasestorage.googleapis.com/v0/b/lebonpetitcoin-6928c.appspot.com/o/noPP.jpg?alt=media&token=a8e9f70c-85e6-48ad-9f00-433c726f9da2")
                                    .centerCrop()
                                    .into(imageProfile);
                        }
                        miseAjourButton.setVisibility(View.VISIBLE);
                        setMiseAjourButton();

                    }
                }
            }
        });
    }

    public void getCompte(String pseudo) {
        Task<DocumentSnapshot> tCompte = cCompte.document(pseudo).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Compte compte = documentSnapshot.toObject(Compte.class);

                pseudoTV.setText(compte.getPseudo());
                telTV.setText(compte.getTelephoneContact());
                emailTV.setText(compte.getMailContact());

                if (compte.getSiret()!=null)
                    titreTV.setText(getText(R.string.infos_du_vendeur_pro) +"\n"+ getString(R.string.siret) + " : "+ compte.getSiret());
                if(compte.getContacte()!=null &&compte.getContacte().size()>0 &&compte.getContacte().size()<3){
                    moyenDeContacte.setVisibility(View.VISIBLE);
                    for(String s : compte.getContacte())
                    {
                        if(s.equals("E-mail")){chipEmail.setVisibility(View.VISIBLE);}
                        if(s.equals("Messagerie")){chipMsg.setVisibility(View.VISIBLE);}
                        if(s.equals("Téléphone")){chipTel.setVisibility(View.VISIBLE);}
                    }
                }

                    if (compte.getImageProfile().length() > 0) {
                        GlideApp.with(getContext())
                                .load(compte.getImageProfile())
                                .centerCrop()
                                .into(imageProfile);
                    }
                    else{
                        GlideApp.with(getContext())
                                .load("https://firebasestorage.googleapis.com/v0/b/lebonpetitcoin-6928c.appspot.com/o/noPP.jpg?alt=media&token=a8e9f70c-85e6-48ad-9f00-433c726f9da2")
                                .centerCrop()
                                .into(imageProfile);
                    }

                }
        });
    }

    public void setMiseAjourButton(){
        miseAjourButton.setOnClickListener(new View.OnClickListener() {
            private Fragment fragmentMAJ;
            @Override
            public void onClick(View v) {
                if (this.fragmentMAJ== null) this.fragmentMAJ=MiseAJourAccountFragment.newInstance();
                ((AppCompatActivity)getContext()).getSupportFragmentManager()
                        .beginTransaction().replace(R.id.activity_main_frame_layout, fragmentMAJ).commit();
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String pseudo = bundle.getString("pseudo","");
            getCompte(pseudo);
            setAnnonce(pseudo);
            //Toast.makeText(getContext(),pseudo,Toast.LENGTH_SHORT).show();
        }
        else{
            if(((MainActivity)getActivity()).mAuth.getCurrentUser()!=null)
                getCompteSelf(((MainActivity)getActivity()).mAuth.getCurrentUser().getUid());
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        chipEmail.setVisibility(View.INVISIBLE);
        chipMsg.setVisibility(View.INVISIBLE);
        chipTel.setVisibility(View.INVISIBLE);
    }

    public void setAnnonce(String pseudo){
        String uid = "";

        if (((MainActivity)getActivity()).mAuth.getCurrentUser() != null)
            uid = ((MainActivity)getActivity()).mAuth.getCurrentUser().getUid();

        ArrayList<Annonce> mAnnonces =new ArrayList<>();
        ArrayList<String> mIds =new ArrayList<>();
        String finalUid = uid;
        cAnnonces.whereEqualTo("auteur",pseudo).orderBy("datePoste", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                mAdapter = new RecycleViewAnnonce(getContext(),mAnnonces,mIds, finalUid);
                mRecyclerView.setAdapter(mAdapter);
                if (mRecyclerView.getAdapter().getItemCount() > 0) {
                    mRecyclerView.smoothScrollToPosition(0);
                }

            }
        });
    }
}
