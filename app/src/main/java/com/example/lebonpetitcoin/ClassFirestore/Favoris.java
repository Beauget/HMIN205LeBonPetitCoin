package com.example.lebonpetitcoin.ClassFirestore;

import java.util.Calendar;
import java.util.Date;

public class Favoris {
    String uid;
    String idAnnonce;
    Date date;

    public Favoris(){}

    public Favoris(String uid, String idAnnonce) {
        this.uid = uid;
        this.idAnnonce = idAnnonce;
        this.date = Calendar.getInstance().getTime();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
}
