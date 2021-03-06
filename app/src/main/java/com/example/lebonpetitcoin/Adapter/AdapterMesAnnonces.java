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
import com.example.lebonpetitcoin.Fragments.ModifierAnnonceFragment;
import com.example.lebonpetitcoin.Fragments.ParametresFragment;
import com.example.lebonpetitcoin.Fragments.StatsAnnonceFragment;
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

public class AdapterMesAnnonces extends RecyclerView.Adapter<AdapterMesAnnonces .ViewHolder>  {
    private static final String TAG = "RecyclerViewAnnonce";
    //DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cFavoris = firestoreDB.collection("Favoris");

    private Context mContext;
    private AdapterView.OnItemClickListener mListener;
    private ArrayList<Annonce> annonces;
    private ArrayList<String> ids;
    private String uid;
    private String click;

    public AdapterMesAnnonces(Context context, ArrayList<Annonce> annonces,ArrayList<String> ids, String uid,String click) {
        this.mContext = context;
        this.annonces = annonces;
        this.ids= ids;
        if(uid!=null)
            this.uid=uid;
        else
            this.uid="";
        this.click= click;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_annonce, parent, false);
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


            holder.getLike().setVisibility(View.GONE);



            if(click.equals("modifierAnonce")) {
                holder.getImageViewAnnonce().setOnClickListener(new View.OnClickListener() {
                    private Fragment fragmentModifierAnnonce;

                    @Override
                    public void onClick(View v) {
                        if (this.fragmentModifierAnnonce == null)
                            this.fragmentModifierAnnonce = ModifierAnnonceFragment.newInstance();
                        Bundle arguments = new Bundle();
                        arguments.putString("idAnnonce", id);
                        fragmentModifierAnnonce.setArguments(arguments);
                        ((AppCompatActivity) mContext)
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .addToBackStack("ModifierAnnoncesFragment")
                                .replace(R.id.activity_main_frame_layout, fragmentModifierAnnonce).commit();
                    }
                });
            }

            if (click.equals("statistique")){
                holder.getImageViewAnnonce().setOnClickListener(new View.OnClickListener() {
                    private Fragment fragmentStatistiqueAnnonce;

                    @Override
                    public void onClick(View v) {
                        if (this.fragmentStatistiqueAnnonce== null)
                            this.fragmentStatistiqueAnnonce = StatsAnnonceFragment.newInstance();
                        Bundle arguments = new Bundle();
                        arguments.putString("idAnnonce", id);
                        fragmentStatistiqueAnnonce.setArguments(arguments);
                        ((AppCompatActivity) mContext)
                                .getSupportFragmentManager()
                                .beginTransaction()
                                .addToBackStack("StatsAnnonceFragment")
                                .replace(R.id.activity_main_frame_layout, fragmentStatistiqueAnnonce).commit();

                        //Toast.makeText(mContext, id, Toast.LENGTH_SHORT).show();
                    }
                });

            }


    }

    @Override
    public int getItemCount() {
        return annonces.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle;
        TextView vues;
        Button like;
        TextView prix;
        ImageView imageViewAnnonce;
        public ViewHolder(View itemView) {
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


}