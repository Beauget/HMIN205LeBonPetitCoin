package com.example.lebonpetitcoin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lebonpetitcoin.ClassFirestore.Statistique;
import com.example.lebonpetitcoin.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StatsAnnonceFragment extends Fragment {
    private static final String TAG = "StatsAnnonceFragment";
    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cStatistique = firestoreDB.collection("Statistique");
    private CollectionReference cCompte = firestoreDB.collection("Compte");
    private CollectionReference cAnnonces = firestoreDB.collection("Annonce");

    ArrayList<BarEntry> barEntries;
    BarChart barChartMois;
    int MAXJOUR = 30;


    public static Fragment newInstance() {
        return (new StatsAnnonceFragment());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats_annonce, container, false);
        barChartMois = view.findViewById(R.id.barchartMois);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void getStatistiqueBar(String annonce, BarChart barChart, int nbJour){
        Date now = Calendar.getInstance().getTime();
        int [] array =new int[nbJour];
        Task<QuerySnapshot> query = cStatistique.whereEqualTo("idAnnonce", annonce).get();
        query.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Statistique statistique= document.toObject(Statistique.class);
                        Date date =statistique.getDate();

                        int difference = (int) ((now.getTime()-date.getTime())/ (1000 * 3600 * 24));

                        if(difference<nbJour){
                            array[difference]= array[difference] + 1;

                        }
                    }
                    setBar(array,barChart,nbJour);
                }
            }
        });
    }

    public void setBar(int [] array ,BarChart barChart, int nbJour){

        BarDataSet set;
        int x = nbJour -1;
        barEntries = new ArrayList<>();
        for (int i=0; i<nbJour; i++)
        {
            //BarEntryLabels.add("J-"+i)
            barEntries.add(new BarEntry(x,array[i]));
            x--;

        }
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        set = new BarDataSet(barEntries, " ");
        //set.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set.setDrawValues(false);
        dataSets.add(set);
        BarData data = new BarData(dataSets);
        barChart.setData(data);
        barChart.setFitBars(false);
        barChart.invalidate();

    }


    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String idAnnonce = bundle.getString("idAnnonce","");
            getStatistiqueBar(idAnnonce,barChartMois,30);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
