package com.example.lebonpetitcoin.ClassFirestore;

import java.util.Calendar;
import java.util.Date;

public class Favoris {
    String uid;
    String idAnnonce;
    String titreAnnonce;
    String image= "";
    Date date;

    public Favoris(){}

    public Favoris(String uid, String idAnnonce,String titreAnnonce,String image) {
        this.uid = uid;
        this.idAnnonce = idAnnonce;
        this.titreAnnonce = titreAnnonce;
        this.image=image;
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

    public String getTitreAnnonce() {
        return titreAnnonce;
    }

    public void setTitreAnnonce(String titreAnnonce) {
        this.titreAnnonce = titreAnnonce;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
