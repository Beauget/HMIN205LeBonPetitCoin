package com.example.lebonpetitcoin.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebonpetitcoin.ClassFirestore.Annonce;
import com.example.lebonpetitcoin.ClassFirestore.Compte;
import com.example.lebonpetitcoin.ClassFirestore.Favoris;
import com.example.lebonpetitcoin.Fragments.AnnonceFragment;
import com.example.lebonpetitcoin.GlideApp;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdapterFavoris extends FirestoreRecyclerAdapter<Favoris, AdapterFavoris.FavorisHolder> {
    private Context mContext;
    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cAnnonce= firestoreDB.collection("Annonce");

    public AdapterFavoris(@NonNull FirestoreRecyclerOptions<Favoris> options,Context c) {
        super(options);
        mContext = c;
    }

    @Override
    protected void onBindViewHolder(@NonNull FavorisHolder holder, int position, @NonNull Favoris model) {
        holder.getTextViewTitle().setText(String.valueOf((model.getTitreAnnonce())));

        String s;
        if( model.getImage().length()==0)
            s = "https://firebasestorage.googleapis.com/v0/b/lebonpetitcoin-6928c.appspot.com/o/no_image.png?alt=media&token=e4e42748-45d3-4c07-8028-d767efda4846";
        else
            s = model.getImage();

        GlideApp.with(mContext)
                .load(s)
                .into(holder.getImageView());

        cAnnonce.document(model.getIdAnnonce()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Annonce annonce = documentSnapshot.toObject(Annonce.class);

                if (annonce != null) {
                    holder.getTextViewTitle().setText(String.valueOf((annonce.getTitre())));
                    GlideApp.with(mContext)
                            .load(annonce.getFirstImage())
                            .into(holder.getImageView());
                }
            }
        });



        String id = model.getIdAnnonce();
        holder.getCardView().setOnClickListener(new View.OnClickListener() {
            private Fragment fragmentAnnonce;
            @Override
            public void onClick(View v) {
                if (this.fragmentAnnonce == null) this.fragmentAnnonce= AnnonceFragment.newInstance();
                Bundle arguments = new Bundle();
                arguments.putString( "idAnnonce", id);
                fragmentAnnonce.setArguments(arguments);
                ((AppCompatActivity) mContext).getSupportFragmentManager()
                        .beginTransaction().replace(R.id.activity_main_frame_layout, fragmentAnnonce).commit();

                Toast.makeText(mContext,id,Toast.LENGTH_SHORT).show();
            }
        });


    }



    @NonNull
    @Override
    public FavorisHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fav,
                parent, false);
        return new FavorisHolder(v);
    }

    class FavorisHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        ImageView imageView;
        CardView cardView;

        public FavorisHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.fav_item_layout);
        }

        public TextView getTextViewTitle(){
            return textViewTitle;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public CardView getCardView() {
            return cardView;
        }
    }
}