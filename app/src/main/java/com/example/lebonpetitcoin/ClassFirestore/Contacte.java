package com.example.lebonpetitcoin.ClassFirestore;

import com.google.firebase.firestore.Exclude;

public class Contacte {
    String id;
    String intitule;

    public Contacte(){
        //public no args contructeur obligatoire sinon firebase crash
    }

    public Contacte(String intitule) {
        this.intitule = intitule;
    }
    public Contacte(String intitule,String id) {
        this.intitule = intitule;
        this.id=id;
    }

    //pour pas de redondance
    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }
}