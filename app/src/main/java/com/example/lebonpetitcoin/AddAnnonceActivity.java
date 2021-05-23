package com.example.lebonpetitcoin;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebonpetitcoin.Adapter.AdapterCategorie;
import com.example.lebonpetitcoin.Adapter.AdapterMoyenDePaiement;
import com.example.lebonpetitcoin.ClassFirestore.Annonce;
import com.example.lebonpetitcoin.ClassFirestore.Categorie;
import com.example.lebonpetitcoin.ClassFirestore.Compte;
import com.example.lebonpetitcoin.ClassFirestore.Image;
import com.example.lebonpetitcoin.ClassFirestore.MoyenDePaiement;
import com.example.lebonpetitcoin.ClassFirestore.Position;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddAnnonceActivity extends AppCompatActivity implements View.OnClickListener{

    //NOM DE LA CLASSE QUI SERA ENVOYÉ EN CAS D'ECHEC D'ENVOIE
    private static final String TAG = "AddAnnonce";

    private static final int PICK_IMAGE_REQUEST = 1;

    private ProgressBar mProgressBar;
    private Button mButtonAdd;
    private Button mButtondelete;
    private Button mButtonRetour;
    private Intent intent = getIntent();
    private Bundle extras ;
    private String name,telephoneContact, mailContact;
    private  boolean estProfessionnel;
    private int maxImage = 4;

    //GEOLOCALISATION
    private LocationManager locationManager;
    private String fournisseur;
    private LocationListener ecouteurGPS;
    private double Latitude ;
    private double Longitude;



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
    EditText departement;
    CheckBox position;


    private String Document_img1="";
    private Object ConfiURL = "https://www.google.com" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_annonce);
        mProgressBar = findViewById(R.id.progress_bar);
        mStorageRef = FirebaseStorage.getInstance().getReference("Annonces");

        extras = getIntent().getExtras();

        if (extras != null) {
            name = extras.getString("name");
            getCompte(name);

            img1 = (ImageView) findViewById(R.id.img1);
            img2 = (ImageView) findViewById(R.id.img2);
            img3 = (ImageView) findViewById(R.id.img3);
            img4 = (ImageView) findViewById(R.id.img4);
            img5 = (ImageView) findViewById(R.id.img5);
            img6 = (ImageView) findViewById(R.id.img6);

            titre = findViewById(R.id.titre);
            description = findViewById(R.id.description);
            prix = findViewById(R.id.prix);
            departement = findViewById(R.id.departement);
            position = findViewById(R.id.position);
            position.setText(getString(R.string.position_recherche));
            Upload_Btn = (Button) findViewById(R.id.UploadBtn);
            mButtonAdd = (Button) findViewById(R.id.add);
            mButtondelete = (Button) findViewById(R.id.delete);
            mButtonRetour = (Button) findViewById(R.id.retourBtn);


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
            adapterCategorie = new AdapterCategorie(optionsC, this, arrayListCategorie);
            recyclerViewCategorie.setAdapter(adapterCategorie);

            Query queryM = cMoyenDePaiement.orderBy("intitule", Query.Direction.DESCENDING);
            FirestoreRecyclerOptions<MoyenDePaiement> optionsM = new FirestoreRecyclerOptions.Builder<MoyenDePaiement>()
                    .setQuery(queryM, MoyenDePaiement.class)
                    .setLifecycleOwner(this)
                    .build();
            ArrayList<String> arrayListMoyenDePaiement = new ArrayList<>();
            adapterMoyenDePaiement = new AdapterMoyenDePaiement(optionsM, this, arrayListMoyenDePaiement);
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

            mButtonRetour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(AddAnnonceActivity.this, MainActivity.class);
                    AddAnnonceActivity.this.startActivity(myIntent);
                }
            });

            Upload_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mUploadTask != null && mUploadTask.isInProgress()) {
                        Toast.makeText(AddAnnonceActivity.this, getString(R.string.creation_de_l_annonce_en_cours), Toast.LENGTH_SHORT).show();
                    } else {
                        checkValidation(arrayListMoyenDePaiement, arrayListCategorie);
                    }
                }
            });
        }

        else {
            Toast.makeText(AddAnnonceActivity.this, getString(R.string.null_auteur), Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(AddAnnonceActivity.this, MainActivity.class);
            AddAnnonceActivity.this.startActivity(myIntent);

        }

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


    public void addAnnonce(String titre,String description,boolean estProfessionnel,String telephoneContact,String mailContact,float prix, ArrayList<String> mdp,ArrayList<String> cat,ArrayList<String> uri,double latitude, double longitude, String departement){
        Annonce annonce = new Annonce(name,titre,description,estProfessionnel,telephoneContact,mailContact,prix,mdp,cat, uri, latitude,longitude,departement);
        cAnnonce.add(annonce)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),getString(R.string.annonce_ajoute),Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(AddAnnonceActivity.this, MainActivity.class);
                        AddAnnonceActivity.this.startActivity(myIntent);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),getString(R.string.echec),Toast.LENGTH_SHORT).show();
                        Log.d(TAG,e.toString());
                    }
                });
    }

    // Check Validation Method
    private void checkValidation(ArrayList<String> mdp,ArrayList<String> cat) {

        // Get all edittext texts
        float prixBase = 0;
        float getPrix = 0;
        if (prix.getText().toString().length() > 0) {
            prixBase = Float.parseFloat(prix.getText().toString());
            int prixInteger = (int) (prixBase * 100);
            getPrix = (float)prixInteger / 100;
        }

        String getTitre = titre.getText().toString();
        String getDescription = description.getText().toString();
        String getDepartement = departement.getText().toString();

        // Pattern match for email id
        //Pattern p = Pattern.compile(regEx);
        //Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getPrix > 9999 || prixBase > getPrix || getTitre.length() == 0 || getDescription.equals("") || cat.size() == 0 || mdp.size() == 0 || (getDepartement.length() > 0 && position.isChecked())){
            Toast.makeText(getApplicationContext(), R.string.echec, Toast.LENGTH_SHORT).show();
        }


        else {
            if (position.isChecked()) {
                if (Latitude == 0 && Longitude == 0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.badPosition), Toast.LENGTH_SHORT).show();
                }
                else {
                    //Toast.makeText(getApplicationContext(), "Latitude :" + String.valueOf(Latitude) + " Longitude : " + String.valueOf(Longitude), Toast.LENGTH_SHORT).show();

                    if (imageUriList.size() > 0) {
                        uploadImageToFirebaseStorage(getTitre, getDescription, getPrix, mdp, cat, imageUriList.size(), Latitude, Longitude, "");
                    } else {
                        addAnnonce(getTitre, getDescription, estProfessionnel, telephoneContact, mailContact, getPrix, mdp, cat, new ArrayList<String>(), Latitude, Longitude, "");
                    }
                }
            } else if (getDepartement.length() > 0) {
                Position position = new Position();
                if (position.isDepartement(getDepartement)) {
                    Position.Departement departement = position.getDepartement(getDepartement);
                    if (imageUriList.size() > 0) {
                        uploadImageToFirebaseStorage(getTitre, getDescription, getPrix, mdp, cat, imageUriList.size(), departement.getLatitude(), departement.getLongitude(), getDepartement+"000, "+departement.getNom()+", France");
                    } else {
                        addAnnonce(getTitre, getDescription, estProfessionnel, telephoneContact, mailContact, getPrix, mdp, cat, new ArrayList<String>(), departement.getLatitude(), departement.getLongitude(), getDepartement+"000, "+departement.getNom()+", France");
                    }
                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.badDepartement), Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getApplicationContext(), getString(R.string.badTooManyPosition), Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if(imageUriList.size()>=maxImage) {
            Toast.makeText(getApplicationContext(), getString(R.string.maxImage), Toast.LENGTH_SHORT).show();
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
            //Toast.makeText(getApplicationContext(), "image : " + String.valueOf(imageUriList.size() - 1), Toast.LENGTH_SHORT).show();
            imageViewArrayList.get(imageUriList.size() - 1).setVisibility(View.VISIBLE);
            Picasso.get().load(mImageUri).into(imageViewArrayList.get(imageUriList.size() - 1));

        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImageToFirebaseStorage(String titre, String description , float prix, ArrayList<String> mdp,  ArrayList<String> cat, int nbImages,double latitude,double longitude,String departement) {
        //Toast.makeText(getApplicationContext(),"nbImages : "+ String.valueOf(nbImages),Toast.LENGTH_SHORT).show();
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
                        //Toast.makeText(getApplicationContext(),"uploaded",Toast.LENGTH_SHORT).show();

                        if(mUrlList.size()==nbImages) {
                            //Toast.makeText(getApplicationContext(), "ajout annonce", Toast.LENGTH_SHORT).show();
                            addAnnonce(titre, description,estProfessionnel,telephoneContact, mailContact, prix, mdp, cat, mUrlList,latitude,longitude,departement);
                        }
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
        }
    }

    @Override
    public void onClick(View view) {

    }


    private void initialiserLocalisation()
    {
        if (locationManager == null)
        {
            locationManager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

            Criteria criteres = new Criteria();

            // la précision  : (ACCURACY_FINE pour une haute précision ou ACCURACY_COARSE pour une moins bonne précision)
            criteres.setAccuracy(Criteria.ACCURACY_FINE);

            // l'altitude
            criteres.setAltitudeRequired(true);

            // la direction
            criteres.setBearingRequired(true);

            // la vitesse
            criteres.setSpeedRequired(true);

            // la consommation d'énergie demandée
            criteres.setCostAllowed(true);
            //criteres.setPowerRequirement(Criteria.POWER_HIGH);
            criteres.setPowerRequirement(Criteria.POWER_MEDIUM);

            fournisseur = locationManager.getBestProvider(criteres, true);
            Log.d("GPS", "fournisseur : " + fournisseur);
        }

        if (fournisseur != null)
        {
            // dernière position connue
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                Log.d("GPS", "pas de permission !");
                return;
            }

            Location localisation = locationManager.getLastKnownLocation(fournisseur);

            if(localisation != null)
            {
                Latitude = localisation.getLatitude();
                Longitude = localisation.getLongitude();

                if(Latitude== 0 && Longitude == 0)
                    position.setText(getString(R.string.position_recherche));
                // on notifie la localisation
                ecouteurGPS.onLocationChanged(localisation);
            }

            //Toast.makeText(getContext(), ecouteurGPS.toString(), Toast.LENGTH_SHORT).show();
            // on configure la mise à jour automatique : au moins 10 mètres et 15 secondes
            locationManager.requestLocationUpdates(fournisseur, 500, 2, ecouteurGPS);
        }
    }

    private void arreterLocalisation()
    {
        if(locationManager != null)
        {
            locationManager.removeUpdates(ecouteurGPS);
            ecouteurGPS = null;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        adapterCategorie.startListening();
        ecouteurGPS = new LocationListener() {
            @Override
            public void onLocationChanged(Location localisation)
            {
                //Toast.makeText(getContext().getApplicationContext(), fournisseur + " localisation", Toast.LENGTH_SHORT).show();

                Log.d("GPS", "localisation : " + localisation.toString());
                String coordonnees = String.format("Latitude : %f - Longitude : %f\n", localisation.getLatitude(), localisation.getLongitude());
                Log.d("GPS", coordonnees);
                String autres = String.format("Vitesse : %f - Altitude : %f - Cap : %f\n", localisation.getSpeed(), localisation.getAltitude(), localisation.getBearing());
                Log.d("GPS", autres);
                //String timestamp = String.format("Timestamp : %d\n", localisation.getTime());
                //Log.d("GPS", "timestamp : " + timestamp);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date(localisation.getTime());
                Log.d("GPS", sdf.format(date));
                position.setText(getString(R.string.position_ok));

                Latitude = localisation.getLatitude();
                Longitude = localisation.getLongitude();

            }

            @Override
            public void onProviderDisabled(String fournisseur)
            {
                Toast.makeText(getApplicationContext(), fournisseur + " désactivé !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderEnabled(String fournisseur)
            {
                Toast.makeText(getApplicationContext(), fournisseur + " activé !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String fournisseur, int status, Bundle extras)
            {
                switch(status)
                {
                    case LocationProvider.AVAILABLE:
                        Toast.makeText(getApplicationContext(), fournisseur + " état disponible", Toast.LENGTH_SHORT).show();
                        break;
                    case LocationProvider.OUT_OF_SERVICE:
                        Toast.makeText(getApplicationContext(), fournisseur + " état indisponible", Toast.LENGTH_SHORT).show();
                        break;
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        Toast.makeText(getApplicationContext(), fournisseur + " état temporairement indisponible", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), fournisseur + " état : " + status, Toast.LENGTH_SHORT).show();
                }
            }
        };

        initialiserLocalisation();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterCategorie.stopListening();
        arreterLocalisation();
    }
}

/*
File dossierImage = new File(Context.getFilesDir(), "images");
File nouvelleImage = new File(dossierImage, "mon_image.jpg");
Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + "mon.nom.de.package.provider", nouvelleImage);

Intent intent = new Intent(Intent.ACTION_VIEW);
intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
 */