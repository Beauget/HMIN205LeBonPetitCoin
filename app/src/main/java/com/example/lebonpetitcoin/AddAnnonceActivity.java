package com.example.lebonpetitcoin;
//http://www.codeplayon.com/2018/11/android-image-upload-to-server-from-camera-and-gallery/

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lebonpetitcoin.Adapter.AdapterCategorie;
import com.example.lebonpetitcoin.Adapter.AdapterMoyenDePaiement;
import com.example.lebonpetitcoin.ClassFirestore.Annonce;
import com.example.lebonpetitcoin.ClassFirestore.Categorie;
import com.example.lebonpetitcoin.ClassFirestore.Compte;
import com.example.lebonpetitcoin.ClassFirestore.Image;
import com.example.lebonpetitcoin.ClassFirestore.MoyenDePaiement;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.Collator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddAnnonceActivity extends AppCompatActivity implements View.OnClickListener{

    //NOM DE LA CLASSE QUI SERA ENVOYÉ EN CAS D'ECHEC D'ENVOIE
    private static final String TAG = "AddAnnonce";

    private static final int PICK_IMAGE_REQUEST = 1;

    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFileName;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Button mButtonAdd;
    private Button mButtondelete;
    private Intent intent = getIntent();
    private Bundle extras ;
    private String name,telephoneContact, mailContact;
    private  boolean estProfessionnel;
    private int maxImage = 4;


    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cAnnonce = firestoreDB.collection("Annonce");
    private CollectionReference cCategorie = firestoreDB.collection("Categorie");
    private CollectionReference cMoyenDePaiement = firestoreDB.collection("MoyenDePaiement");
    private CollectionReference cCompte= firestoreDB.collection("Compte");

    //Listener afin que la recherce dans la db se fasse pas quand l'application est en arrière plan
    private ListenerRegistration categorieListener;
    private ListenerRegistration moyenDePaiementListener;
    private FirestoreRecyclerAdapter adapterCategorie;
    RecyclerView recyclerViewCategorie;
    private FirestoreRecyclerAdapter adapterMoyenDePaiement;
    RecyclerView recyclerViewMoyenDePaiement;


    public static final String KEY_User_Document1 = "doc1";
    ArrayList<ImageView> imageViewArrayList = new ArrayList<>();
    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    ImageView img5;
    ImageView img6;

    ArrayList<Uri> imageUriList = new ArrayList<>();
    private Uri mImageUri;
    String mUrl;
    ArrayList<String> mUrlList = new ArrayList<>();

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;

    Button Upload_Btn;
    EditText titre;
    EditText description;
    EditText prix;


    private String Document_img1="";
    private Object ConfiURL = "https://www.google.com" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_annonce);

        extras = getIntent().getExtras();

        if (extras != null) {
            name = extras.getString("name");
            getCompte(name);
        }
        else{
            Toast.makeText(AddAnnonceActivity.this, "null", Toast.LENGTH_SHORT).show();
        }

        img1=(ImageView)findViewById(R.id.img1);
        img2=(ImageView)findViewById(R.id.img2);
        img3=(ImageView)findViewById(R.id.img3);
        img4=(ImageView)findViewById(R.id.img4);
        img5=(ImageView)findViewById(R.id.img5);
        img6=(ImageView)findViewById(R.id.img6);

        titre=findViewById(R.id.titre);
        description=findViewById(R.id.description);
        prix=findViewById(R.id.prix);
        Upload_Btn=(Button)findViewById(R.id.UploadBtn);
        mButtonAdd=(Button)findViewById(R.id.add);
        mButtondelete=(Button)findViewById(R.id.delete);




        recyclerViewCategorie = findViewById(R.id.LCategorie);
        recyclerViewCategorie.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMoyenDePaiement = findViewById(R.id.LMoyenDePaiement);
        recyclerViewMoyenDePaiement.setLayoutManager(new LinearLayoutManager(this));

        //imageUriList.add(mImageUri);
        imageViewArrayList.add(img1);
        imageViewArrayList.add(img2);
        imageViewArrayList.add(img3);
        imageViewArrayList.add(img4);
        imageViewArrayList.add(img5);
        imageViewArrayList.add(img6);

        Query queryC = cCategorie.orderBy("intitule", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Categorie> optionsC = new FirestoreRecyclerOptions.Builder<Categorie>()
                .setQuery(queryC, Categorie.class)
                .setLifecycleOwner(this)
                .build();
        ArrayList<String> arrayListCategorie = new ArrayList<>();
        adapterCategorie = new AdapterCategorie(optionsC,this,arrayListCategorie);
        recyclerViewCategorie.setAdapter(adapterCategorie);

        Query queryM = cMoyenDePaiement.orderBy("intitule", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<MoyenDePaiement> optionsM = new FirestoreRecyclerOptions.Builder<MoyenDePaiement>()
                .setQuery(queryM, MoyenDePaiement.class)
                .setLifecycleOwner(this)
                .build();
        ArrayList<String> arrayListMoyenDePaiement = new ArrayList<>();
        adapterMoyenDePaiement = new AdapterMoyenDePaiement(optionsM,this, arrayListMoyenDePaiement);
        recyclerViewMoyenDePaiement.setAdapter(adapterMoyenDePaiement);
        //recyclerViewMoyenDePaiement.getChild


       /* mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();

            }
        });*/

        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mButtondelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage();

            }
        });

        Upload_Btn.setOnClickListener(new View.OnClickListener()
        {   @Override
        public void onClick(View view) {
            //Toast.makeText(view.getContext(), "MDP : \n"+arrayListMoyenDePaiement.toString(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(view.getContext(), "Categorie : \n"+arrayListCategorie.toString(), Toast.LENGTH_SHORT).show();
            if (mUploadTask != null && mUploadTask.isInProgress()) {
                Toast.makeText(AddAnnonceActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                checkValidation(arrayListMoyenDePaiement,arrayListCategorie);
            }

        }});

        
        mProgressBar = findViewById(R.id.progress_bar);
        mStorageRef = FirebaseStorage.getInstance().getReference("Annonces");





    }

    private void deleteImage(){
        if (imageUriList.size()>0) {
            imageUriList.remove(imageUriList.size() - 1);
            imageViewArrayList.get(imageUriList.size()).setVisibility(View.GONE);
        }

    }

    public void getCompte(String uid){
        Task<QuerySnapshot> query = cCompte.whereEqualTo("uid", uid).get();
        // future.get() blocks on response
        query.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Compte compte = document.toObject(Compte.class);
                        name = compte.getPseudo();
                        estProfessionnel = compte.getEstProfessionnel();
                        if (estProfessionnel==true)
                            maxImage = 6;
                        else
                            maxImage = 4;
                        mailContact = compte.getMailContact();
                        telephoneContact=compte.getTelephoneContact();
                    }
                }
            }
        });
    }

    private void selectImage() {
        final CharSequence[] options = { "Prendre une photo", "Choisir depuis la gallerie","Annuler" };
        AlertDialog.Builder builder = new AlertDialog.Builder(AddAnnonceActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Prendre une photo"))
                {/*
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

                    Context context = null;
                    Uri photoURI = MonFileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", f);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(intent, 1);*/
                }
                else if (options[item].equals("Choisir depuis la gallerie"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Toast.makeText(getApplicationContext(),"gallery" , Toast.LENGTH_LONG ).show();
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Annuler")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    public void addAnnonce(String titre,String description,boolean estProfessionnel,String telephoneContact,String mailContact,float prix, ArrayList<String> mdp,ArrayList<String> cat,ArrayList<String> uri){
        Annonce annonce = new Annonce(name,titre,description,estProfessionnel,telephoneContact,mailContact,prix,mdp,cat, uri);
        cAnnonce.add(annonce)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),"Annonce ajouté",Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"erreur",Toast.LENGTH_SHORT).show();
                        Log.d(TAG,e.toString());
                    }
                });
    }

    // Check Validation Method
    private void checkValidation(ArrayList<String> mdp,ArrayList<String> cat) {

        // Get all edittext texts
        float prixBase = 0;
        float getPrix = 0;
        if(prix.getText().toString().length()>0) {
            prixBase = Float.parseFloat(prix.getText().toString());
            int prixInteger = (int) (prixBase * 100);
            getPrix = prixInteger / 100;
        }

        String getTitre = titre.getText().toString();
        String getDescription = description.getText().toString();

        // Pattern match for email id
        //Pattern p = Pattern.compile(regEx);
        //Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getPrix>9999||prixBase>getPrix|| getTitre.length() == 0 || getDescription.equals("")|| cat.size()==0 || mdp.size()==0||imageUriList.size() ==0 )
            Toast.makeText(getApplicationContext(),"echec",Toast.LENGTH_SHORT).show();
        else
            {
                //uploadFile(getTitre,getDescription,Float.valueOf(getPrix),cat,mdp);
                uploadImageToFirebaseStorage(getTitre,getDescription,getPrix,mdp,cat,imageUriList.size());
            }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if(imageUriList.size()>=maxImage) {
            Toast.makeText(getApplicationContext(), "nbMax d'images atteind", Toast.LENGTH_SHORT).show();
        }
        else {
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            imageUriList.add(mImageUri);
            Toast.makeText(getApplicationContext(), "image : " + String.valueOf(imageUriList.size() - 1), Toast.LENGTH_SHORT).show();
            imageViewArrayList.get(imageUriList.size() - 1).setVisibility(View.VISIBLE);
            Picasso.get().load(mImageUri).into(imageViewArrayList.get(imageUriList.size() - 1));

        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImageToFirebaseStorage(String titre, String description , float prix, ArrayList<String> mdp,  ArrayList<String> cat, int nbImages) {
        Toast.makeText(getApplicationContext(),"nbImages : "+ String.valueOf(nbImages),Toast.LENGTH_SHORT).show();
        for (int i=0; i < imageUriList.size() ; i++) {
            Toast.makeText(getApplicationContext(),String.valueOf(i)+ "/" + String.valueOf(imageUriList.size()),Toast.LENGTH_SHORT).show();

            Uri imageUri = imageUriList.get(i);
            StorageReference filereference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));
            //imageUriList.remove(0);

            final UploadTask mUploadTask = filereference.putFile(imageUri);
            Task<Uri> uriTask = mUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>(){
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }

                    String downloadImageUrl = filereference.getDownloadUrl().toString();
                    return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri getUri = task.getResult();
                        mUrl = getUri.toString();
                        mUrlList.add(mUrl);
                        Toast.makeText(getApplicationContext(),"uploaded",Toast.LENGTH_SHORT).show();

                        if(mUrlList.size()==nbImages) {
                            Toast.makeText(getApplicationContext(), "ajout annonce", Toast.LENGTH_SHORT).show();
                            addAnnonce(titre, description,estProfessionnel,telephoneContact, mailContact, prix, mdp, cat, mUrlList);
                        }
                        //updateAnnonce(id,mUrl);
                        //uploadImageToFirebaseStorageRecursive() //Call when completes
                    }
                }
            });
        }
    }

    private void updateAnnonce(String id, String uri){
        Task<DocumentSnapshot> tAnnonce = cAnnonce.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Annonce annonce = documentSnapshot.toObject(Annonce.class);
                assert annonce != null;
                ArrayList <String> images = annonce.getImages();
                images.add(uri);
                Map<String, Object> updates = new HashMap<>();
                updates.put("images", images);
                cAnnonce.document(id).update(updates);
            }
        });

    }


    private void uploadFile(String titre, String description , float prix, ArrayList<String> cat,  ArrayList<String> mdp,ArrayList<String> uri) {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //addAnnonce(titre,description,prix,cat,mdp,uri);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddAnnonceActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this,"pas d'image boloss", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {

    }
}

/*
File dossierImage = new File(Context.getFilesDir(), "images");
File nouvelleImage = new File(dossierImage, "mon_image.jpg");
Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + "mon.nom.de.package.provider", nouvelleImage);

Intent intent = new Intent(Intent.ACTION_VIEW);
intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
 */