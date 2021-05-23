package com.example.lebonpetitcoin.Fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebonpetitcoin.Adapter.AdapterConversation;
import com.example.lebonpetitcoin.Adapter.AdapterMessage;
import com.example.lebonpetitcoin.ClassFirestore.Conversation;
import com.example.lebonpetitcoin.ClassFirestore.Message;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class ConversationFragment extends Fragment {

    private static final String TAG = "ConversationFragment";

    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cMessage = firestoreDB.collection("Message");

    private ListenerRegistration conversationListener;



    private String id;
    private String lecteur;
    private RecyclerView recyclerView;
    private EditText messageAEnvoyer;
    private Button envoyerMessage;
    private Button ajouterImage;
    private Button supprimerImage;
    private ImageView imageView;
    private Uri uri;
    private StorageReference mStorageRef;
    private static final int PICK_IMAGE_REQUEST = 1;
    private AdapterMessage adapter;


    public static ConversationFragment newInstance() {
        return (new ConversationFragment());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);

        recyclerView =  view.findViewById(R.id.reyclerview_message_list);
        messageAEnvoyer =  view.findViewById(R.id.edittext_message);
        envoyerMessage = view.findViewById(R.id.button_message);
        ajouterImage = view.findViewById(R.id.button_image_add);
        supprimerImage = view.findViewById(R.id.button_image_delete);
        imageView = view.findViewById(R.id.imageView_image);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            id = bundle.getString("idConversation","");
            lecteur = bundle.getString("lecteur","");

            mStorageRef = FirebaseStorage.getInstance().getReference("ImageMessagerie");

            Query query = cMessage.orderBy("date", Query.Direction.ASCENDING).whereEqualTo("idConversation",id);
            FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                    .setQuery(query, Message.class)
                    .build();
            adapter = new AdapterMessage(options,getContext(),lecteur,"");
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);

            envoyerMessage.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    uploadFile(id,lecteur);
                }
            });

            ajouterImage.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ajouterImage();
                }
            });

            supprimerImage.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    supprimerImage();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            uri = data.getData();
            imageView.setVisibility(View.VISIBLE);
            Picasso.get().load(uri).into(imageView);
            ajouterImage.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            supprimerImage.setVisibility(View.VISIBLE);
        }
    }

    public void onStart() {
        super.onStart();
        adapter.startListening();
        conversationListener = cMessage.orderBy("date", Query.Direction.ASCENDING).whereEqualTo("idConversation",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "listen:error", e);
                    return;
                }
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        conversationListener.remove();
    }

    void envoyerMessage(String idConversation, String auteur,String image){
        if (messageAEnvoyer.getText().toString().length()>0 || image.length()>0)
        {
            Message message = new Message(idConversation,auteur, messageAEnvoyer.getText().toString(), image);

            cMessage.add(message)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            //Toast.makeText(getContext(),"msg envoyé",Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),getString(R.string.echec),Toast.LENGTH_SHORT).show();
                            Log.d(TAG,e.toString());
                        }
                    });
            adapter.startListening();

        }
        Toast.makeText(getContext(),getString(R.string.badMessage),Toast.LENGTH_SHORT).show();
        messageAEnvoyer.setText("");
        supprimerImage();
    }

    void ajouterImage(){
        openFileChooser();
    }

    void supprimerImage(){
        imageView.setVisibility(View.GONE);
        ajouterImage.setVisibility(View.VISIBLE);
        supprimerImage.setVisibility(View.GONE);
        uri = null;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(String idConversation, String auteur) {
        if (uri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(uri));

            final UploadTask mUploadTask = fileReference.putFile(uri);
            Task<Uri> uriTask = mUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>(){
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }

                    String downloadImageUrl = fileReference.getDownloadUrl().toString();
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri getUri = task.getResult();
                        String mUrl = getUri.toString();
                        envoyerMessage(idConversation, auteur,mUrl);


                    }
                }
            });
        }
        else {
            envoyerMessage(idConversation, auteur,"");
        }
    }
}
