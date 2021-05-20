package com.example.lebonpetitcoin.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lebonpetitcoin.ClassFirestore.Annonce;
import com.example.lebonpetitcoin.Fragments.AnnonceFragment;
import com.example.lebonpetitcoin.Fragments.ParametresFragment;
import com.example.lebonpetitcoin.GlideApp;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class AdapterAnnonce extends FirestoreRecyclerAdapter<Annonce, AdapterAnnonce.AnnonceHolder> {
    private Context mContext;
    private AdapterView.OnItemClickListener mListener;

    public AdapterAnnonce(@NonNull FirestoreRecyclerOptions<Annonce> options,Context c) {
        super(options);
        mContext = c;
    }



    @Override
    protected void onBindViewHolder(@NonNull AnnonceHolder holder, int position, @NonNull Annonce model) {

        holder.getTextViewTitle().setText(String.valueOf((model.getTitre())));
        holder.getVues().setText(String.valueOf((model.getNbDeVisites())+" vue(s)"));
        //holder.getImageViewAnnonce();

        String s;

        if( model.getImages().size()==0)
            s = "https://firebasestorage.googleapis.com/v0/b/lebonpetitcoin-6928c.appspot.com/o/no_image.png?alt=media&token=e4e42748-45d3-4c07-8028-d767efda4846";
        else
            s = model.getFirstImage();

        GlideApp.with(mContext)
                .load(s)
                .into(holder.getImageViewAnnonce());

        DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        String id = snapshot.getId();

        holder.getLike().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"Ajouté aux favoris!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.getImageViewAnnonce().setOnClickListener(new View.OnClickListener() {
            private Fragment fragmentAnnonce;
            @Override
            public void onClick(View v) {
                if (this.fragmentAnnonce == null) this.fragmentAnnonce= AnnonceFragment.newInstance();
                Bundle arguments = new Bundle();
                arguments.putString( "idAnnonce", id);
                fragmentAnnonce.setArguments(arguments);
                ((AppCompatActivity) mContext).getSupportFragmentManager()
                        .beginTransaction().replace(R.id.activity_main_frame_layout, fragmentAnnonce).commit();

                //Toast.makeText(mContext,id,Toast.LENGTH_SHORT).show();
            }
        });
    }


    @NonNull
    @Override
    public AnnonceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_annonce,
                parent, false);
        return new AnnonceHolder(v);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mListener = listener;
    }

    class AnnonceHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView vues;
        Button like;
        ImageView imageViewAnnonce;
        public AnnonceHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.titleTextView);
            imageViewAnnonce = itemView.findViewById(R.id.imageView);
            vues = itemView.findViewById(R.id.likeCountTextView);
            like = itemView.findViewById(R.id.favBtn);
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

    }

    public class ViewHolder {
    }
}