package com.example.lebonpetitcoin.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lebonpetitcoin.ClassFirestore.Annonce;
import com.example.lebonpetitcoin.ClassFirestore.Favoris;
import com.example.lebonpetitcoin.Fragments.AnnonceFragment;
import com.example.lebonpetitcoin.Fragments.ParametresFragment;
import com.example.lebonpetitcoin.GlideApp;
import com.example.lebonpetitcoin.MainActivity;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RecycleViewAnnonce extends RecyclerView.Adapter<RecycleViewAnnonce.ViewHolder>  {
    private static final String TAG = "RecyclerViewAnnonce";
    //DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cFavoris = firestoreDB.collection("Favoris");

    private Context mContext;
    private AdapterView.OnItemClickListener mListener;
    private ArrayList<Annonce> annonces;
    private ArrayList<String> ids;
    private String uid;

    public RecycleViewAnnonce(Context context, ArrayList<Annonce> annonces,ArrayList<String> ids, String uid) {
        this.mContext = context;
        this.annonces = annonces;
        this.ids= ids;
        if(uid!=null)
            this.uid=uid;
        else
            this.uid="";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(annonces.get(position));

        Annonce annonce = annonces.get(position);
        String id = ids.get(position);
        holder.getTextViewTitle().setText(annonce.getTitre());
        holder.getVues().setText(String.valueOf((annonce.getNbDeVisites())+" vue(s)"));

        String s;

        if( annonce.getImages().size()==0)
            s = "https://firebasestorage.googleapis.com/v0/b/lebonpetitcoin-6928c.appspot.com/o/no_image.png?alt=media&token=e4e42748-45d3-4c07-8028-d767efda4846";
        else
            s = annonce.getFirstImage();

        GlideApp.with(mContext)
                .load(s)
                .into(holder.getImageViewAnnonce());

        if(uid.length()==0) {
            holder.getLike().setVisibility(View.GONE);
        }

        else{
            isInFavoris(uid,id,annonce.getTitre(),annonce.getFirstImage(),holder.getLike(),holder.getLiked());

            holder.getLike().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (uid.length()>0){
                        inFavoris(uid,id,annonce.getTitre(),annonce.getFirstImage(),holder.getLike(),holder.getLiked());
                    }

                }
            });
        }
        holder.getPrix().setText(String.valueOf(annonce.getPrix())+" €");

        holder.getImageViewAnnonce().setOnClickListener(new View.OnClickListener() {
            private Fragment fragmentAnnonce;
            @Override
            public void onClick(View v) {
                if (this.fragmentAnnonce == null) this.fragmentAnnonce= AnnonceFragment.newInstance();
                Bundle arguments = new Bundle();
                arguments.putString( "idAnnonce", id);
                fragmentAnnonce.setArguments(arguments);
                ((AppCompatActivity) mContext)
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("ResultatFragment")
                        .replace(R.id.activity_main_frame_layout, fragmentAnnonce).commit();

                //Toast.makeText(mContext,id,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return annonces.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle;
        TextView vues;
        Button like;
        Button liked;
        TextView prix;
        ImageView imageViewAnnonce;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.titleTextView);
            imageViewAnnonce = itemView.findViewById(R.id.imageView);
            vues = itemView.findViewById(R.id.likeCountTextView);
            like = itemView.findViewById(R.id.favBtn);
            liked = itemView.findViewById(R.id.favBtnfilled);
            prix = itemView.findViewById(R.id.prix);
        }


        public TextView getTextViewTitle(){
            return textViewTitle;
        }

        public ImageView getImageViewAnnonce()
        {
            return imageViewAnnonce;
        }

        public TextView getVues(){return vues; }

        public Button getLike(){return like;}

        public Button getLiked() { return liked; }

        public TextView getPrix() { return prix; }
    }

    public void addFavoris(String uid,String idAnnonce,String titreAnnonce, String image){
        if (uid.length() > 0) {
        Favoris favoris= new Favoris(uid, idAnnonce,titreAnnonce,image);
        cFavoris.add(favoris)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(mContext,"Ajouté aux favoris !",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext,"erreur",Toast.LENGTH_SHORT).show();
                        Log.d(TAG,e.toString());
                    }
                });

        }
    }

    public void inFavoris(String uid, String idAnnonce, String titreAnnonce,String image,Button like , Button liked){
        final int[] cmpt = {0};
        if(uid.length()>0){
            cFavoris.whereEqualTo("idAnnonce",idAnnonce).whereEqualTo("uid",uid).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        return;
                    }
                    for (QueryDocumentSnapshot documentSnapshot : value) {
                        Annonce annonce = documentSnapshot.toObject(Annonce.class);
                        cmpt[0]++;
                    }
                    if (cmpt[0] == 0) {
                        addFavoris(uid, idAnnonce,titreAnnonce,image,like,liked);
                    }

                }
            });
        }
    }

    public void addFavoris(String uid,String idAnnonce,String titreAnnonce, String image,Button like , Button liked){
        if (uid.length() > 0) {
            Favoris favoris= new Favoris(uid, idAnnonce,titreAnnonce,image);
            cFavoris.add(favoris)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(mContext,"Ajouté aux favoris !",Toast.LENGTH_SHORT).show();
                            like.setVisibility(View.GONE);
                            liked.setVisibility(View.VISIBLE);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(mContext,"erreur",Toast.LENGTH_SHORT).show();
                            Log.d(TAG,e.toString());
                        }
                    });
        }
    }
    public void isInFavoris(String uid, String idAnnonce, String titreAnnonce,String image,Button like , Button liked){
        final int[] cmpt = {0};
        if(uid.length()>0){
            cFavoris.whereEqualTo("idAnnonce",idAnnonce).whereEqualTo("uid",uid).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        return;
                    }
                    for (QueryDocumentSnapshot documentSnapshot : value) {
                        Annonce annonce = documentSnapshot.toObject(Annonce.class);
                        cmpt[0]++;
                    }
                    if (cmpt[0] != 0) {
                        like.setVisibility(View.GONE);
                        liked.setVisibility(View.VISIBLE);
                    }

                }
            });
        }
    }


}