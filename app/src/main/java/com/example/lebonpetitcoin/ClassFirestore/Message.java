package com.example.lebonpetitcoin.ClassFirestore;

import java.util.Calendar;
import java.util.Date;

public class Message {
    String idConversation;
    String auteur;
    String texte;
    String image;
    Date date;
    String idAnnonce;

    public Message(){}

    public Message(String idConversation, String auteur, String texte, String image) {
        this.idConversation = idConversation;
        this.auteur = auteur;
        this.texte = texte;
        this.image = image;
        this.date = Calendar.getInstance().getTime();
    }

    public Message(String idConversation, String auteur, String texte, String image, Date date, String idAnnonce) {
        this.idConversation = idConversation;
        this.auteur = auteur;
        this.texte = texte;
        this.image = image;
        this.date = Calendar.getInstance().getTime();
        this.idAnnonce = idAnnonce;
    }

    public String getIdConversation() {
        return idConversation;
    }

    public void setIdConversation(String idConversation) {
        this.idConversation = idConversation;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
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

    public String getIdAnnonce() {
        return idAnnonce;
    }

    public void setIdAnnonce(String idAnnonce) {
        this.idAnnonce = idAnnonce;
    }
}
