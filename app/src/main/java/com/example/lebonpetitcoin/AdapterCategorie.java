package com.example.lebonpetitcoin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebonpetitcoin.ClassFirestore.Categorie;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdapterCategorie extends FirestoreRecyclerAdapter<Categorie, AdapterCategorie.CategorieHolder> {

    public AdapterCategorie(@NonNull FirestoreRecyclerOptions<Categorie> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull CategorieHolder holder, int position, @NonNull Categorie model) {
        holder.getTextViewTitle().setText(String.valueOf((model.getId())));
        holder.getTextViewDescription().setText(String.valueOf(model.getIntitule()));

    }

    @NonNull
    @Override
    public CategorieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_intitule,
                parent, false);
        return new CategorieHolder(v);
    }

    class CategorieHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
        public CategorieHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
        }

        public TextView getTextViewTitle(){
            return textViewTitle;
        }

        public TextView getTextViewDescription() {
            return textViewDescription;
        }
    }
}