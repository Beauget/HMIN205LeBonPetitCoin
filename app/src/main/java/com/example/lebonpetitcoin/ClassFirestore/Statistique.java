package com.example.lebonpetitcoin.ClassFirestore;

import com.google.firebase.firestore.Exclude;

import java.util.Calendar;

import java.util.Date;
import java.util.List;
import java.util.Calendar;

public class Statistique {
    String idAuteur;
    String idAnnonce;
    Date date;
    boolean estMembre;

    public Statistique(String idAuteur, String idAnnonce,boolean estMembre) {
        this.idAuteur = idAuteur;
        this.idAnnonce = idAnnonce;
        this.date  = Calendar.getInstance().getTime();
        this.estMembre = estMembre;
    }

    public Statistique(){
        //public no args contructeur obligatoire sinon firebase crash
    }

    public String getIdAuteur() {
        return idAuteur;
    }

    public void setIdAuteur(String idAuteur) {
        this.idAuteur = idAuteur;
    }

    public String getIdAnnonce() {
        return idAnnonce;
    }

    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isEstMembre() {
        return estMembre;
    }

    public void setEstMembre(boolean estMembre) {
        this.estMembre = estMembre;
    }
}
