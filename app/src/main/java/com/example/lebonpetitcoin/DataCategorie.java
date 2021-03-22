package com.example.lebonpetitcoin;

public class DataCategorie {
    String idCategorie;
    String intitule;

    // public void setDateCreated(Date dateCreated) { this.dateCreated = dateCreated; } gere les date, bien pour les visites

    public DataCategorie(String id, String s){
        this.idCategorie = id;
        this.intitule = s;
    }

    //Get
    public String getIntitule(){
        return this.intitule;
    }
    public String getIdCategorie() {
        return idCategorie;
    }

    //Set
    public void setIdCategorie(String s){
        this.idCategorie=s;
    }
    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }
}
