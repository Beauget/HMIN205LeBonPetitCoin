package com.example.lebonpetitcoin.Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lebonpetitcoin.Adapter.AdapterMesAnnonces;
import com.example.lebonpetitcoin.ClassFirestore.Annonce;
import com.example.lebonpetitcoin.ClassFirestore.Compte;
import com.example.lebonpetitcoin.ClassFirestore.Statistique;
import com.example.lebonpetitcoin.MainActivity;
import com.example.lebonpetitcoin.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.graphics.Typeface;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StatsFragment extends Fragment {

    private static final String TAG = "StatsFragment";
    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cStatistique = firestoreDB.collection("Statistique");
    private CollectionReference cCompte = firestoreDB.collection("Compte");
    private CollectionReference cAnnonces = firestoreDB.collection("Annonce");

    TextView textView;
    int nbVisualisation = 0;
    ArrayList<PieEntry> pieEntries;
    PieChart pieChart;
    ArrayList<BarEntry> barEntries;
    BarChart barChart;

    int MAXJOUR = 30;


    public static StatsFragment newInstance() {
        return (new StatsFragment());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        textView = view.findViewById(R.id.textView);
        pieChart = view.findViewById(R.id.piechart);
        barChart = view.findViewById(R.id.barchart);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCompte(((MainActivity)getActivity()).mAuth.getCurrentUser().getUid());
    }

    public void getStatistiquePie(String pseudo){
        pieEntries = new ArrayList<>();
        Task<QuerySnapshot> query = cAnnonces.whereEqualTo("auteur", pseudo).get();
        query.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Annonce annonce = document.toObject(Annonce.class);
                        pieEntries.add(new PieEntry(annonce.getNbDeVisites(),annonce.getTitre()));
                    }
                    setPieChart(pieEntries);

                }
            }
        });
    }

    public void setPieChart(ArrayList<PieEntry> entries){
        PieDataSet dataSet = new PieDataSet(entries,"");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        //data.setValueTypeface(tfLight);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();
    }

    public void getStatistiqueBar(String annonce){
        Date now = Calendar.getInstance().getTime();
        int [] array =new int[MAXJOUR];
        Task<QuerySnapshot> query = cStatistique.whereEqualTo("idAnnonce", annonce).get();
        query.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Statistique statistique= document.toObject(Statistique.class);
                        Date date =statistique.getDate();

                        int difference = (int) ((now.getTime()-date.getTime())/ (1000 * 3600 * 24));
                        
                        if(difference<MAXJOUR){
                            array[difference]= array[difference] + 1;

                        }
                    }
                    setBar(array);
                }
            }
        });
    }

    public void setBar(int [] array ){

        BarDataSet set;
        int x = MAXJOUR -1;
        barEntries = new ArrayList<>();
        for (int i=0; i<MAXJOUR; i++)
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

    public void getStatistique(String pseudo){
        nbVisualisation = 0;

        Task<QuerySnapshot> query = cStatistique.whereEqualTo("idAuteur", pseudo).get();
        // future.get() blocks on response
        query.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        nbVisualisation++;
                        //Statistique statistique = document.toObject(Statistique.class);
                    }
                    textView.setText("Vos annonces ont étais visualisé : " +Integer.valueOf(nbVisualisation).toString() + " fois!");
                }
            }
        });
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
                        getStatistique(compte.getPseudo());
                        getStatistiquePie(compte.getPseudo());
                        getStatistiqueBar("HdsvtjGULUKrIATwGqk8");
                    }
                }
            }
        });
    }
}
