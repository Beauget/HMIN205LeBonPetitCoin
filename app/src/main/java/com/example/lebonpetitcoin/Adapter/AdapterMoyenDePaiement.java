package com.example.lebonpetitcoin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebonpetitcoin.ClassFirestore.MoyenDePaiement;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class AdapterMoyenDePaiement extends FirestoreRecyclerAdapter<MoyenDePaiement, AdapterMoyenDePaiement.MoyenDePaiementHolder> {
    private Context mContext;
    public static ArrayList<String> arrayList = new ArrayList<>();


    public AdapterMoyenDePaiement(@NonNull FirestoreRecyclerOptions<MoyenDePaiement> options, Context c) {
        super(options);
        mContext = c;
    }
    public  ArrayList<String> getArrayList(){
        return arrayList;
    }


    @Override
    protected void onBindViewHolder(@NonNull MoyenDePaiementHolder holder, int position, @NonNull MoyenDePaiement model) {
        holder.getTextViewTitle().setText(String.valueOf((model.getIntitule())));
        //holder.getTextViewDescription().setText(String.valueOf(model.getIntitule()));

        DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        String id = snapshot.getId();

        holder.getCheckBox().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer) holder.checkBox.getTag();

                if (arrayList.contains(id)){
                    arrayList.remove(id);
                }
                else
                    arrayList.add(id);
                Toast.makeText(mContext,
                        arrayList.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public MoyenDePaiementHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_intitule,
                parent, false);
        return new MoyenDePaiementHolder(v);
    }


    class MoyenDePaiementHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
        CheckBox checkBox;
        public MoyenDePaiementHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            checkBox = itemView.findViewById(R.id.chbContent);
            //textViewDescription = itemView.findViewById(R.id.text_view_description);
        }

        public TextView getTextViewTitle(){
            return textViewTitle;
        }
        public CheckBox getCheckBox(){return  checkBox;}
        /*
        public TextView getTextViewDescription() {
            return textViewDescription;
        }
        */

    }
}