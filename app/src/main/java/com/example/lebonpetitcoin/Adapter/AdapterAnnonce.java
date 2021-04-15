package com.example.lebonpetitcoin.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lebonpetitcoin.ClassFirestore.Annonce;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdapterAnnonce extends FirestoreRecyclerAdapter<Annonce, AdapterAnnonce.AnnonceHolder> {

    public AdapterAnnonce(@NonNull FirestoreRecyclerOptions<Annonce> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull AnnonceHolder holder, int position, @NonNull Annonce model) {
        holder.getTextViewTitle().setText(String.valueOf((model.getTitre())));
        //holder.getImageViewAnnonce();

        //Glide.with(this).load(model.getFirstImage()).into(holder.getImageViewAnnonce());

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