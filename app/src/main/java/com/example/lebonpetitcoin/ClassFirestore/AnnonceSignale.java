package com.example.lebonpetitcoin.ClassFirestore;

public class AnnonceSignale {
    String idAnnonce;
    String raison;
    String message;

    public AnnonceSignale(String idAnnonce, String raison, String message) {
        this.idAnnonce = idAnnonce;
        this.raison = raison;
        this.message = message;
    }

    public String getIdAnnonce() {
        return idAnnonce;
    }

    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
