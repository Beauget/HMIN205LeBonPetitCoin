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
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebonpetitcoin.ClassFirestore.Annonce;
import com.example.lebonpetitcoin.ClassFirestore.Message;
import com.example.lebonpetitcoin.Fragments.AnnonceFragment;
import com.example.lebonpetitcoin.GlideApp;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.installations.Utils;

public class AdapterMessage extends FirestoreRecyclerAdapter<Message, AdapterMessage.MessageHolder> {
    private Context mContext;
    private String lecteur;
    private  String imageProfileExpedieur;
    private AdapterView.OnItemClickListener mListener;
    private static final int LAYOUT_ENVOIE= 1;
    private static final int LAYOUT_RECUS= 0;

    public AdapterMessage(@NonNull FirestoreRecyclerOptions<Message> options, Context c,String lecteur,String imageProfileExpedieur) {
        super(options);
        mContext = c;
        this.lecteur= lecteur;
        this.imageProfileExpedieur = imageProfileExpedieur;
    }


    @Override
    protected void onBindViewHolder(@NonNull AdapterMessage.MessageHolder holder, int position, @NonNull Message model) {

        if (holder.getItemViewType()== LAYOUT_RECUS) {
            ((AdapterMessage.MessageHolderRecus) holder).bind(model);
        } else {
            ((AdapterMessage.MessageHolder) holder).bind(model);
        }
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        Message model = getItem(position);
        if(lecteur.equals(model.getAuteur())==false)
            return LAYOUT_RECUS;
        else
            return LAYOUT_ENVOIE;
    }

    @NonNull
    @Override
    public AdapterMessage.MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =null;
        AdapterMessage.MessageHolder messageHolder = null;

        if(viewType==LAYOUT_ENVOIE)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_envoye,parent, false);
            messageHolder = new MessageHolder(view);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_recus,parent, false);
            messageHolder = new MessageHolderRecus(view);
        }

        return messageHolder;
    }

        public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
            mListener = listener;
        }


    class MessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;
        ImageView image;

        void bind(Message model) {
            messageText.setText(model.getTexte());
            getMessageText().setText(model.getTexte());
            getTimeText().setText(model.getDate().toString());


            if(model.getImage().length()>0){
                getImage().setVisibility(View.VISIBLE);
                GlideApp.with(mContext)
                        .load(model.getImage())
                        .into(getImage());

            }
        }

        public MessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            image = (ImageView)itemView.findViewById(R.id.image_message_body);
        }
        public TextView getMessageText() {
                return messageText;
            }
        public TextView getTimeText() {
                return timeText;
            }
        public  ImageView getImage(){return  image;}


    }

    class MessageHolderRecus extends MessageHolder{
        ImageView profileImage;
        TextView nameText;


        void bind(Message model) {
            getNameText().setText(model.getAuteur());
            getMessageText().setText(model.getTexte());
            getTimeText().setText(model.getDate().toString());
            GlideApp.with(mContext)
                    .load(imageProfileExpedieur)
                    .into(getProfileImage());
            //holder.getImage().
            //holder.getImageProfile().

            if (model.getImage().length() > 0) {
                getImage().setVisibility(View.VISIBLE);
                GlideApp.with(mContext)
                        .load(model.getImage())
                        .into(getImage());
            }
        }

        public MessageHolderRecus(View itemView){
            super(itemView);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }
        public TextView getNameText() {return nameText;}
        public ImageView getProfileImage() {
            return profileImage;
        }
    }
}
