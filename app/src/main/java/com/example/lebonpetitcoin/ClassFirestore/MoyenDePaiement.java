package com.example.lebonpetitcoin.ClassFirestore;

import com.google.firebase.firestore.Exclude;

public class MoyenDePaiement {
    String id; //pas essentiel mais facilite les recherches (?)
    String intitule;

    public MoyenDePaiement(){
        //public no args contructeur obligatoire sinon firebase crash
    }
    public MoyenDePaiement(String intitule) {
        this.intitule = intitule;
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
