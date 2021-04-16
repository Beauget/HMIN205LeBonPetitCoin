package com.example.lebonpetitcoin.ClassFirestore;



import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.storage.StorageReference;
import com.google.type.Date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Annonce {
    //pas besoins de mettre le compte en lui meme, si on suppose qu'on ne peut pas changer de pseudo/il est unique
    String auteur;
    boolean estProfessionnel;

    //On permet ainsi au vendeur de donner un numéro différent suivant l'annonce
    String telephoneContact;
    String mailContact;

    String titre;
    String description;
    java.util.Date datePoste;
    Integer nbDeVisites;
    //Catgorie, pas String dans le doute ou le nom change
    List<DocumentReference> categories;
    DocumentReference statistique;

    float prix;
    //Des MoyenDePaiment, pas String dans le doute ou le nom change
    List<DocumentReference>  paiement;
    //Des Image
    ArrayList<String> images = new ArrayList<>();

    public Annonce(){
        //public no args contructeur obligatoire sinon firebase crash
    }

    public Annonce(String titre) {
        this.titre = titre;
        this.auteur = "Jhon";
        this.estProfessionnel = false;
        this.telephoneContact = "9999999";
        this.mailContact = "mailContact@Pouet";
        this.titre = titre;
        this.images.add("https://firebasestorage.googleapis.com/v0/b/lebonpetitcoin-6928c.appspot.com/o/seal.jpg?alt=media&token=a0936c24-9211-4400-9d70-ab6b310390da");
        this.description = "description trop cool";
        this.datePoste = Calendar.getInstance().getTime();
        this.nbDeVisites = 0;
        this.prix = (float) 9.99;

    }

    public Annonce(String auteur, boolean estProfessionnel, String telephoneContact, String mailContact, String titre, String description, java.util.Date datePoste, Integer nbDeVisites, List<DocumentReference> categories, DocumentReference statistique, float prix, List<DocumentReference> paiement, ArrayList<String> images) {
        this.auteur = auteur;
        this.estProfessionnel = estProfessionnel;
        this.telephoneContact = telephoneContact;
        this.mailContact = mailContact;
        this.titre = titre;
        this.description = description;
        this.datePoste = datePoste;
        this.nbDeVisites = nbDeVisites;
        this.categories = categories;
        this.statistique = statistique;
        this.prix = prix;
        this.paiement = paiement;
        this.images = images;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
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

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.util.Date getDatePoste() { return datePoste; }

    public void setDatePoste(java.util.Date datePoste) {
        this.datePoste = datePoste;
    }

    public Integer getNbDeVisites() {
        return nbDeVisites;
    }

    public void setNbDeVisites(Integer nbDeVisites) {
        this.nbDeVisites = nbDeVisites;
    }

    public List<DocumentReference> getCategories() {
        return categories;
    }

    public void setCategories(List<DocumentReference> categories) {
        this.categories = categories;
    }

    public DocumentReference getStatistique() {
        return statistique;
    }

    public void setStatistique(DocumentReference statistique) {
        this.statistique = statistique;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public List<DocumentReference> getPaiement() {
        return paiement;
    }

    public void setPaiement(List<DocumentReference> paiement) {
        this.paiement = paiement;
    }

    public boolean isEstProfessionnel() {
        return estProfessionnel;
    }

    public void setEstProfessionnel(boolean estProfessionnel) {
        this.estProfessionnel = estProfessionnel;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        images = images;
    }

    @Exclude
    public String firstImage(){return this.images.get(0);}
}
