package com.example.lebonpetitcoin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lebonpetitcoin.ClassFirestore.Annonce;
import com.example.lebonpetitcoin.GlideApp;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class AdapterAnnonce extends FirestoreRecyclerAdapter<Annonce, AdapterAnnonce.AnnonceHolder> {
    private Context mContext;

    public AdapterAnnonce(@NonNull FirestoreRecyclerOptions<Annonce> options,Context c) {
        super(options);
        mContext = c;
    }



    @Override
    protected void onBindViewHolder(@NonNull AnnonceHolder holder, int position, @NonNull Annonce model) {
        holder.getTextViewTitle().setText(String.valueOf((model.getTitre())));
        holder.getImageViewAnnonce();

        //ArrayList<String> i = new ArrayList<>();
        //i.add("https://firebasestorage.googleapis.com/v0/b/lebonpetitcoin-6928c.appspot.com/o/seal.jpg?alt=media&token=a0936c24-9211-4400-9d70-ab6b310390da");
        //model.setImages(i);
        String s;

        if( model.getImages().size()==0)
            s = "https://firebasestorage.googleapis.com/v0/b/lebonpetitcoin-6928c.appspot.com/o/seal2.jpg?alt=media&token=41553fe0-e1b7-4712-8eb1-043f2fd07d16";
        else
            s = model.firstImage();


        //if (s==null){ s= "https://firebasestorage.googleapis.com/v0/b/lebonpetitcoin-6928c.appspot.com/o/seal2.jpg?alt=media&token=41553fe0-e1b7-4712-8eb1-043f2fd07d16";}

        //String s= "https://firebasestorage.googleapis.com/v0/b/lebonpetitcoin-6928c.appspot.com/o/seal2.jpg?alt=media&token=41553fe0-e1b7-4712-8eb1-043f2fd07d16";

        //Glide.with(this).load(model.getFirstImage()).into(holder.getImageViewAnnonce());
        GlideApp.with(mContext)
                .load(s)
                .into(holder.getImageViewAnnonce());

    }

    @NonNull
    @Override
    public AnnonceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_annonce,
                parent, false);
        return new AnnonceHolder(v);
    }

    class AnnonceHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        ImageView imageViewAnnonce;
        public AnnonceHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.titleTextView);
            imageViewAnnonce = itemView.findViewById(R.id.imageView);
        }

        public TextView getTextViewTitle(){
            return textViewTitle;
        }

        public ImageView getImageViewAnnonce()
        {
            return imageViewAnnonce;
        }

    }
}