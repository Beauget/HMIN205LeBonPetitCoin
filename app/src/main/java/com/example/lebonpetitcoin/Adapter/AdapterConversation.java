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
import com.example.lebonpetitcoin.ClassFirestore.Conversation;
import com.example.lebonpetitcoin.Fragments.AnnonceFragment;
import com.example.lebonpetitcoin.Fragments.ConversationFragment;
import com.example.lebonpetitcoin.Fragments.ParametresFragment;
import com.example.lebonpetitcoin.GlideApp;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdapterConversation extends FirestoreRecyclerAdapter<Conversation, AdapterConversation.ConversationHolder> {
    private Context mContext;
    private String lecteur;
    private AdapterView.OnItemClickListener mListener;
    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cAnnonce= firestoreDB.collection("Annonce");
    public FirebaseAuth mAuth;

    public AdapterConversation(@NonNull FirestoreRecyclerOptions<Conversation> options,Context c,String lecteur) {
        super(options);
        mContext = c;
        this.lecteur = lecteur;
        //Toast.makeText(mContext,"created",Toast.LENGTH_SHORT).show();

    }



    @Override
    protected void onBindViewHolder(@NonNull ConversationHolder holder, int position, @NonNull Conversation model) {

        if(model.getCompte1().equals(lecteur)){
            holder.getAuteur().setText(String.valueOf((model.getCompte2())));
            //Toast.makeText(mContext,model.getCompte1(),Toast.LENGTH_SHORT).show();
        }
        else{
            holder.getAuteur().setText(String.valueOf((model.getCompte1())));
            //Toast.makeText(mContext,model.getCompte2(),Toast.LENGTH_SHORT).show();
        }

        holder.getMessage().setText(String.valueOf((model.getAnnonce())));

        DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        String id = snapshot.getId();

        if (model.getImage()==null) {
            model.setImage("");
        }

        if (model.getIdAnnonce()!=null) {

            cAnnonce.document(model.getIdAnnonce()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Annonce annonce = documentSnapshot.toObject(Annonce.class);

                    if (annonce != null) {
                        GlideApp.with(mContext)
                                .load(annonce.getFirstImage())
                                .into(holder.getImageView());
                    }
                }
            });
        }


       /* if (model.getImage().length()>0){
            GlideApp.with(mContext)
                    .load(model.getImage())
                    .into(holder.getImageView());
        }*/

        holder.getImageView().setOnClickListener(new View.OnClickListener() {
            private Fragment fragmentConversation;
            @Override
            public void onClick(View v) {
                if (this.fragmentConversation == null) this.fragmentConversation= ConversationFragment.newInstance();
                Bundle arguments = new Bundle();
                arguments.putString( "idConversation", id);
                arguments.putString( "lecteur", lecteur);
                //arguments.putString( "idAnnonce", lecteur);
                fragmentConversation.setArguments(arguments);
                ((AppCompatActivity) mContext).getSupportFragmentManager()
                        .beginTransaction().addToBackStack("MessageFragment")
                        .replace(R.id.activity_main_frame_layout, fragmentConversation).commit();
            }
        });
    }


    @NonNull
    @Override
    public ConversationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messages,
                parent, false);
        return new ConversationHolder(v);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mListener = listener;
    }

    class ConversationHolder extends RecyclerView.ViewHolder {
        TextView auteur;
        TextView message;
        ImageView  imageView;
        public ConversationHolder(View itemView) {
            super(itemView);
            auteur= itemView.findViewById(R.id.auteurTextView);
            message = itemView.findViewById(R.id.messageTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public TextView getAuteur() {
            return auteur;
        }

        public TextView getMessage() {
            return message;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }

}