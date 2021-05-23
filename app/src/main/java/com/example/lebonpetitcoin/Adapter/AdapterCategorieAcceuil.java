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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebonpetitcoin.ClassFirestore.Categorie;
import com.example.lebonpetitcoin.ClassFirestore.Image;
import com.example.lebonpetitcoin.Fragments.ResultatFragment;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class AdapterCategorieAcceuil extends FirestoreRecyclerAdapter<Categorie, AdapterCategorieAcceuil.CategorieHolder> {
    private Context mContext;

    public AdapterCategorieAcceuil(@NonNull FirestoreRecyclerOptions<Categorie> options,Context c) {
        super(options);
        mContext = c;
    }

    @Override
    protected void onBindViewHolder(@NonNull CategorieHolder holder, int position, @NonNull Categorie model) {
        holder.getTextViewTitle().setText(String.valueOf((model.getIntitule())));
        ArrayList<String> array = new ArrayList();
        array.add(String.valueOf((model.getIntitule())));

        holder.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = ResultatFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putString("recherche", "");
                bundle.putStringArrayList("categories", array);
                fragment.setArguments(bundle);
                ((AppCompatActivity) mContext).getSupportFragmentManager()
                        .beginTransaction().replace(R.id.activity_main_frame_layout, fragment).commit();
            }
        });
    }



    @NonNull
    @Override
    public CategorieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categorie,
                parent, false);
        return new CategorieHolder(v);
    }

    class CategorieHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        ImageView  imageView;

        public CategorieHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public TextView getTextViewTitle(){
            return textViewTitle;
        }

        public ImageView getImageView() { return imageView; }
    }
}