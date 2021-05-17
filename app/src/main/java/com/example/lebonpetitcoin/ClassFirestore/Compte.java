package com.example.lebonpetitcoin.ClassFirestore;

import java.util.ArrayList;

//A voir
public class Compte {
    String uid; //firebase.auth().currentUser.uid
    String pseudo;
    String imageProfile;
    boolean estProfessionnel;
    String telephoneContact;
    String mailContact;
    String siret;
    String localisation;
    ArrayList<String> contacte = new ArrayList<>();

    public Compte(){
        //public no args contructeur obligatoire sinon firebase crash
    }

    public Compte(String uid,String pseudo, String imageProfile, boolean estProfessionnel, String telephoneContact, String mailContact, String siret, String localisation) {
        this.uid = uid;
        this.pseudo= pseudo;
        this.imageProfile = imageProfile;
        this.estProfessionnel = estProfessionnel;
        this.telephoneContact = telephoneContact;
        this.mailContact = mailContact;
        this.siret = siret;
        this.localisation = localisation;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public boolean getEstProfessionnel() {
        return estProfessionnel;
    }

    public void setEstProfessionnel(boolean estProfessionnel) {
        this.estProfessionnel = estProfessionnel;
    }

    public String getTelephoneContact() {
        return telephoneContact;
    }

    public void setTelephoneContact(String telephoneContact) {
        this.telephoneContact = telephoneContact;
    }

    public String getMailContact() {
        return mailContact;
    }

    public void setMailContact(String mailContact) {
        this.mailContact = mailContact;
    }

    public String getSiret() {
        return siret;
    }

    public void setSiret(String siret) {
        this.siret = siret;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public ArrayList<String> getContacte() {
        return contacte;
    }

    public void setContacte(ArrayList<String> contacte) {
        this.contacte = contacte;
    }
}
