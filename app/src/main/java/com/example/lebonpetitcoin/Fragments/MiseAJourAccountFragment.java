package com.example.lebonpetitcoin.Fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebonpetitcoin.Adapter.AdapterCategorie;
import com.example.lebonpetitcoin.ClassFirestore.Categorie;
import com.example.lebonpetitcoin.ClassFirestore.Compte;
import com.example.lebonpetitcoin.GlideApp;
import com.example.lebonpetitcoin.MainActivity;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MiseAJourAccountFragment extends Fragment {
    private static final String TAG = "MiseAJourAccountFragment";
    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cContacte = firestoreDB.collection("Contacte");
    private CollectionReference cCompte= firestoreDB.collection("Compte");

    ImageView imageProfile;
    ArrayList<String> contacte;
    private RecyclerView recyclerViewContacte;
    private FirestoreRecyclerAdapter adapterContacte;
    Button valider;
    String uid = "";
    private Uri mImageUri;
    private EditText tel;
    private StorageReference mStorageRef;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Fragment fragmentAccount;

    public static MiseAJourAccountFragment newInstance() {
        return (new MiseAJourAccountFragment());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maj_account, container, false);
        valider= view.findViewById(R.id.miseAjourButton);
        imageProfile = view.findViewById(R.id.imageProfile);
        tel = view.findViewById(R.id.tel);
        contacte= new ArrayList<>();
        recyclerViewContacte= view.findViewById(R.id.LContacte);
        recyclerViewContacte.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Query query = cContacte.orderBy("intitule", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Categorie> optionsC = new FirestoreRecyclerOptions.Builder<Categorie>()
                .setQuery(query, Categorie.class)
                .setLifecycleOwner(this)
                .build();
        adapterContacte = new AdapterCategorie(optionsC,getContext(),contacte);
        recyclerViewContacte.setAdapter(adapterContacte);

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });
        mStorageRef = FirebaseStorage.getInstance().getReference("ImageProfile");
        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            GlideApp.with(getContext())
                    .load(mImageUri)
                    .centerCrop()
                    .into(imageProfile);
        }
    }

    public  void getCompte(String imageURL, String tel){
        uid = ((MainActivity)getActivity()).mAuth.getCurrentUser().getUid();
        Task<QuerySnapshot> query = cCompte.whereEqualTo("uid", uid).get();
        // future.get() blocks on response
        query.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();
                        Compte compte = document.toObject(Compte.class);
                        Map<String, Object> updates = new HashMap<>();
                        if(contacte.size()>0){
                            updates.put("contacte", contacte);
                        }
                        if(imageURL.length()>0){
                            updates.put("imageProfile", imageURL);
                        }
                        if(tel.length()>0){
                            if (tel.length()==10)
                                updates.put("telephoneContact", tel);
                        }
                        cCompte.document(id).update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                miseAjourInterface();
                            }});

                    }
                }
            }
        });
    }
    public void miseAjourInterface(){
        if (this.fragmentAccount== null) this.fragmentAccount= AccountFragment.newInstance();
        this.getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.activity_main_frame_layout, fragmentAccount).commit();
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

    private void uploadFile() {
        String getTel = tel.getText().toString();
        tel.setText("");
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            final UploadTask mUploadTask = fileReference.putFile(mImageUri);
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
                        getCompte(mUrl,getTel);
                    }
                }
            });
        }
        else {
            getCompte("",getTel);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
