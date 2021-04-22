package com.example.lebonpetitcoin;
//http://www.codeplayon.com/2018/11/android-image-upload-to-server-from-camera-and-gallery/

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.example.lebonpetitcoin.ClassFirestore.MoyenDePaiement;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.Collator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddAnnonceActivity extends AppCompatActivity implements View.OnClickListener{

    //NOM DE LA CLASSE QUI SERA ENVOYÉ EN CAS D'ECHEC D'ENVOIE
    private static final String TAG = "AddAnnonce";


    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cAnnonces = firestoreDB.collection("Annonce");
    private CollectionReference cCategorie = firestoreDB.collection("Categorie");
    private CollectionReference cMoyenDePaiement = firestoreDB.collection("MoyenDePaiement");

    //Listener afin que la recherce dans la db se fasse pas quand l'application est en arrière plan
    private ListenerRegistration categorieListener;
    private ListenerRegistration moyenDePaiementListener;
    private FirestoreRecyclerAdapter adapterCategorie;
    RecyclerView recyclerViewCategorie;
    private FirestoreRecyclerAdapter adapterMoyenDePaiement;
    RecyclerView recyclerViewMoyenDePaiement;


    public static final String KEY_User_Document1 = "doc1";
    ImageView img1;
    Button Upload_Btn;

    private String Document_img1="";
    private Object ConfiURL = "https://www.google.com" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_annonce);

        img1=(ImageView)findViewById(R.id.img1);
        Upload_Btn=(Button)findViewById(R.id.UploadBtn);
        recyclerViewCategorie = findViewById(R.id.LCategorie);
        recyclerViewCategorie.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMoyenDePaiement = findViewById(R.id.LMoyenDePaiement);
        recyclerViewMoyenDePaiement.setLayoutManager(new LinearLayoutManager(this));


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


        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();

            }
        });

        Upload_Btn.setOnClickListener(new View.OnClickListener()
        {   @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "MDP : \n"+arrayListMoyenDePaiement.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(view.getContext(), "Categorie : \n"+arrayListCategorie.toString(), Toast.LENGTH_SHORT).show();

        }});

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Toast.makeText(getApplicationContext(), "RESULT_OK", Toast.LENGTH_LONG).show();
            if (requestCode == 1) {
                Toast.makeText(getApplicationContext(), "REQUEST 1", Toast.LENGTH_LONG).show();
                /*
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    bitmap=getResizedBitmap(bitmap, 400);
                    IDProf.setImageBitmap(bitmap);
                    BitMapToString(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            */
            } else if (requestCode == 2) {
                Toast.makeText(getApplicationContext(), "REQUEST 2", Toast.LENGTH_LONG).show();
                Uri selectedImage = data.getData();
                Toast.makeText(getApplicationContext(), selectedImage.toString() , Toast.LENGTH_LONG).show();

                Bitmap bitmap = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    try {
                        bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getContentResolver(), selectedImage));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                bitmap = getResizedBitmap(bitmap, 400);
                img1.setImageBitmap(bitmap);
            }
        }
    }
    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }



    @Override
    public void onClick(View v) {
        if (Document_img1.equals("") || Document_img1.equals(null)) {
            ContextThemeWrapper ctw = new ContextThemeWrapper();
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
            alertDialogBuilder.setTitle("Pas d'image");
            alertDialogBuilder.setMessage("img1 est vide");
            alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            alertDialogBuilder.show();
            return;
        }
        else{


        }
    }

    public void addAnnonce(String titre,float prix, ArrayList<DocumentReference> categories, ArrayList<DocumentReference> moyenDP){
        Annonce annonce = new Annonce(titre);
        cMoyenDePaiement.add(annonce)
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
}

/*
File dossierImage = new File(Context.getFilesDir(), "images");
File nouvelleImage = new File(dossierImage, "mon_image.jpg");
Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + "mon.nom.de.package.provider", nouvelleImage);

Intent intent = new Intent(Intent.ACTION_VIEW);
intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
 */