package com.example.lebonpetitcoin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.example.lebonpetitcoin.AddAnnonceActivity;
import com.example.lebonpetitcoin.ClassFirestore.Compte;
import com.example.lebonpetitcoin.CustomToast;
import com.example.lebonpetitcoin.GlideApp;
import com.example.lebonpetitcoin.MainActivity;
import com.example.lebonpetitcoin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AccountFragment extends Fragment {

    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cCompte= firestoreDB.collection("Compte");

    public static AccountFragment newInstance() {
        return (new AccountFragment());
    }
    ImageView imageProfile;
    TextView compteTv;
    Button miseAjourButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        compteTv=view.findViewById(R.id.compteTv);
        imageProfile=view.findViewById(R.id.imageProfile);
        miseAjourButton= view.findViewById(R.id.miseAjourButton);

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void getCompteSelf(String uid){
        Task<QuerySnapshot> query = cCompte.whereEqualTo("uid", uid).get();
        // future.get() blocks on response
        query.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Compte compte = document.toObject(Compte.class);
                        String text = "Pseudo : " +compte.getPseudo()+ "\n"+
                                "tel : " +compte.getTelephoneContact()+ "\n"+
                                "e-mail: " +compte.getMailContact() +"\n"+
                                "Localisation : " +compte.getLocalisation()+ "\n";
                        if (compte.getSiret()!=null)
                                text += "Siret: " +compte.getSiret()+ "\n";
                        if(compte.getContacte()!=null &&compte.getContacte().size()>0 &&compte.getContacte().size()<3){
                            text += "Moyen de contacte à privilégier: " +compte.getContacte()+ "\n";
                        }

                        compteTv.setText(text);

                        if (compte.getImageProfile().length()>0){
                            GlideApp.with(getContext())
                                    .load(compte.getImageProfile())
                                    .into(imageProfile);
                        }
                        else{
                            GlideApp.with(getContext())
                                    .load("https://firebasestorage.googleapis.com/v0/b/lebonpetitcoin-6928c.appspot.com/o/noPP.jpg?alt=media&token=a8e9f70c-85e6-48ad-9f00-433c726f9da2")
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

                if (compte != null) {
                    String text = "Pseudo : " + compte.getPseudo() + "\n" +
                            "tel : " + compte.getTelephoneContact() + "\n" +
                            "e-mail: " + compte.getMailContact() + "\n" +
                            "Localisation : " + compte.getLocalisation() + "\n";
                    if (compte.getSiret() != null)
                        text += "Siret: " + compte.getSiret() + "\n";

                    compteTv.setText(text);

                    if (compte.getImageProfile().length() > 0) {
                        GlideApp.with(getContext())
                                .load(compte.getImageProfile())
                                .into(imageProfile);
                    }
                    else{
                        GlideApp.with(getContext())
                                .load("https://firebasestorage.googleapis.com/v0/b/lebonpetitcoin-6928c.appspot.com/o/noPP.jpg?alt=media&token=a8e9f70c-85e6-48ad-9f00-433c726f9da2")
                                .into(imageProfile);
                    }

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
            //Toast.makeText(getContext(),pseudo,Toast.LENGTH_SHORT).show();
        }
        else{
            getCompteSelf(((MainActivity)getActivity()).mAuth.getCurrentUser().getUid());
            //Toast.makeText(getContext(),((MainActivity)getActivity()).mAuth.getCurrentUser().getUid().toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
