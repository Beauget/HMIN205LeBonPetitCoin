package com.example.lebonpetitcoin.ClassFirestore;

import com.google.firebase.firestore.Exclude;

public class Categorie {
    String id; //pas essentiel mais facilite les recherches (?)
    String intitule;

    public Categorie(){
        //public no args contructeur obligatoire sinon firebase crash
    }

    public Categorie(String intitule) {
        this.intitule = intitule;
    }
    public Categorie(String intitule,String id) {
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