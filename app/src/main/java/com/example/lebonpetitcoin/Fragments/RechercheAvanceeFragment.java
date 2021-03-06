package com.example.lebonpetitcoin.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebonpetitcoin.Adapter.AdapterCategorie;
import com.example.lebonpetitcoin.ClassFirestore.Categorie;
import com.example.lebonpetitcoin.ClassFirestore.Position;
import com.example.lebonpetitcoin.CustomToast;
import com.example.lebonpetitcoin.MainActivity;
import com.example.lebonpetitcoin.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static androidx.core.content.ContextCompat.getSystemService;

public class RechercheAvanceeFragment extends Fragment {
    private static final String TAG = "RechercheAvanceeFragment";

    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cCategorie = firestoreDB.collection("Categorie");

    private FirestoreRecyclerAdapter adapterCategorie;
    RecyclerView recyclerViewCategorie;
    Button rechercheButton;
    EditText rechercheText;
    TextView tv;
    EditText km;
    EditText departement;
    CheckBox position;

    //GEOLOCALISATION
    LocationManager locationManager;
    private String fournisseur;
    LocationListener ecouteurGPS;
    double Latitude ;
    double Longitude;
    Position positionP = new Position();
    ArrayList<String> arrayListCategorie ;

    public static Fragment newInstance() {
        return (new RechercheAvanceeFragment());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recherche_avancee, container, false);
        recyclerViewCategorie = view.findViewById(R.id.LCategorie);
        recyclerViewCategorie.setLayoutManager(new LinearLayoutManager(getContext()));
        rechercheButton = view.findViewById(R.id.rechercheButton);
        rechercheText = view.findViewById(R.id.rechercheEditText);
        tv = view.findViewById(R.id.Tv);
        position = view.findViewById(R.id.position);
        km = view.findViewById(R.id.km);
        departement = view.findViewById(R.id.departement);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        arrayListCategorie = new ArrayList<>();

        Query queryC = cCategorie.orderBy("intitule", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Categorie> optionsC = new FirestoreRecyclerOptions.Builder<Categorie>()
                .setQuery(queryC, Categorie.class)
                .setLifecycleOwner(this)
                .build();
        adapterCategorie = new AdapterCategorie(optionsC,getContext(),arrayListCategorie);
        recyclerViewCategorie.setAdapter(adapterCategorie);
        rechercheButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkValidation();
            }
        });
        //Toast.makeText(getContext(), ecouteurGPS.toString(), Toast.LENGTH_SHORT).show();
    }

    public void checkValidation(){
        ((MainActivity)getActivity()).hideKeyboard(getActivity());
        if (position.isChecked()&& departement.getText().toString().length()>0) {
            new CustomToast().Show_Toast(getActivity(), getView(), getString(R.string.badTooManyPosition));
        }
        else if ((position.isChecked() || departement.getText().toString().length()>0)&& km.getText().toString().length()==0){
            new CustomToast().Show_Toast(getActivity(), getView(), getString(R.string.badNoKM));
        }
        else if ((position.isChecked() || departement.getText().toString().length()>0)&& (Integer.valueOf(km.getText().toString())==0 ||Integer.valueOf(km.getText().toString())>200)){
            new CustomToast().Show_Toast(getActivity(), getView(), getString(R.string.badKM));
        }

        else{
            int kmInt=0;
            if(km.getText().toString().length()>0)
                kmInt = Integer.valueOf(km.getText().toString());

            Fragment fragment = ResultatFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString("recherche", rechercheText.getText().toString());
            bundle.putStringArrayList("categories", arrayListCategorie);

            if (position.isChecked() && Latitude== 0 && Longitude == 0){
                new CustomToast().Show_Toast(getActivity(), getView(), getString(R.string.badPosition));
            }

            else if (position.isChecked() && (Latitude!= 0 || Longitude != 0)){
                bundle.putInt("km", kmInt);
                bundle.putDouble("latitude", Latitude);
                bundle.putDouble("longitude", Longitude);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_main_frame_layout, fragment).commit();
            }

            else if (departement.getText().toString().length()>0){
                String d = departement.getText().toString();
                if (positionP.isDepartement(d)){
                    double latitude = positionP.getDepartement(d).getLatitude();
                    double longitude = positionP.getDepartement(d).getLongitude();
                    bundle.putInt("km", kmInt);
                    bundle.putDouble("latitude", latitude);
                    bundle.putDouble("longitude", longitude);
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.activity_main_frame_layout, fragment).commit();
                }
                else {
                    new CustomToast().Show_Toast(getActivity(), getView(), getString(R.string.badDepartement));
                }
            }
            else{
            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_frame_layout, fragment).commit();}
        }

    }

    private void initialiserLocalisation()
    {
        if (locationManager == null)
        {
            locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

            Criteria criteres = new Criteria();

            // la pr??cision  : (ACCURACY_FINE pour une haute pr??cision ou ACCURACY_COARSE pour une moins bonne pr??cision)
            criteres.setAccuracy(Criteria.ACCURACY_FINE);

            // l'altitude
            criteres.setAltitudeRequired(true);

            // la direction
            criteres.setBearingRequired(true);

            // la vitesse
            criteres.setSpeedRequired(true);

            // la consommation d'??nergie demand??e
            criteres.setCostAllowed(true);
            //criteres.setPowerRequirement(Criteria.POWER_HIGH);
            criteres.setPowerRequirement(Criteria.POWER_MEDIUM);

            fournisseur = locationManager.getBestProvider(criteres, true);
            Log.d("GPS", "fournisseur : " + fournisseur);
        }

        if (fournisseur != null)
        {
            // derni??re position connue
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                Log.d("GPS", "no permissions !");
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
            // on configure la mise ?? jour automatique : au moins 10 m??tres et 15 secondes
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

    private void setAdresse(Location localisation){
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        List<Address> adresses = null;
        try
        {
            adresses = geocoder.getFromLocation(localisation.getLatitude(), localisation.getLongitude(), 1);
        }
        catch (IOException ioException)
        {
            Log.e("GPS", "erreur", ioException);
        }
        catch (IllegalArgumentException illegalArgumentException)
        {
            Log.e("GPS", "erreur ", illegalArgumentException);
        }

        if (adresses == null || adresses.size()  == 0)
        {
            Log.e("GPS", "erreur aucune adresse !");
        }
        else
        {
            Address adresse = adresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            String strAdresse = adresse.getAddressLine(0) + ", " + adresse.getLocality();
            Log.d("GPS", "adresse : " + strAdresse);

            for(int i = 0; i <= adresse.getMaxAddressLineIndex(); i++)
            {
                addressFragments.add(adresse.getAddressLine(i));
            }
            Log.d("GPS", TextUtils.join(System.getProperty("line.separator"), addressFragments));
            tv.setText(TextUtils.join(System.getProperty("line.separator"), addressFragments));
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
                //tv.setText("Latitude : " + String.valueOf(localisation.getLatitude())+ " Longitude : " + String.valueOf(localisation.getLongitude()));
                setAdresse(localisation);

            }

            @Override
            public void onProviderDisabled(String fournisseur)
            {
                new CustomToast().Show_Toast(getActivity(), getView(),fournisseur +" "+getString(R.string.desactive));
            }

            @Override
            public void onProviderEnabled(String fournisseur)
            {
                new CustomToast().Show_Toast(getActivity(), getView(),fournisseur+" " + getString(R.string.active));
            }

            @Override
            public void onStatusChanged(String fournisseur, int status, Bundle extras)
            {
                switch(status)
                {
                    case LocationProvider.AVAILABLE:
                        new CustomToast().Show_Toast(getActivity(), getView(), fournisseur + " "+ getString(R.string.etat_disponible));
                        break;
                    case LocationProvider.OUT_OF_SERVICE:
                        new CustomToast().Show_Toast(getActivity(), getView(), fournisseur +" "+ getString(R.string.etat_indisponible));
                        break;
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        new CustomToast().Show_Toast(getActivity(), getView(),fournisseur +" "+ getString(R.string.etat_temporairement_indisponible));
                        break;
                    default:
                        new CustomToast().Show_Toast(getActivity(), getView(), fournisseur +" "+ getString(R.string.etat)+" : "+ status);
                }
            }
        };

        initialiserLocalisation();
        //tv.setText("Latitude : " + String.valueOf(Latitude)+ " Longitude : " + String.valueOf(Longitude));

    }

    @Override
    public void onStop() {
        super.onStop();
        adapterCategorie.stopListening();
        arreterLocalisation();
    }
}
