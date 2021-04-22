package com.example.lebonpetitcoin.ClassFirestore;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.storage.StorageReference;
import com.google.type.Date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Annonce {
    String id;
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
    ArrayList<String> categories = new ArrayList<>();
    String statistique;

    float prix;
    //Des MoyenDePaiment, pas String dans le doute ou le nom change
    ArrayList<String>  paiement = new ArrayList<>();
    //Des Image
    List<String> images = new ArrayList<String>();

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
        this.categories.add("aa02T1zrFPf5TYUewzdP");
        this.paiement.add("OODjNO7jziDyQy9pxQDh");

    }

    public Annonce(String titre,String description,float prix,ArrayList<String> mdp,ArrayList<String> cat) {
        this.titre = titre;
        this.auteur = "Jhon";
        this.estProfessionnel = false;
        this.telephoneContact = "9999999";
        this.mailContact = "mailContact@Pouet";
        this.titre = titre;
        this.images.add("https://firebasestorage.googleapis.com/v0/b/lebonpetitcoin-6928c.appspot.com/o/seal.jpg?alt=media&token=a0936c24-9211-4400-9d70-ab6b310390da");
        this.description = description;
        this.datePoste = Calendar.getInstance().getTime();
        this.nbDeVisites = 0;
        this.prix = prix;
        this.categories = cat;
        this.paiement= mdp;

    }

    public Annonce(String auteur, boolean estProfessionnel, String telephoneContact, String mailContact, String titre, String description, java.util.Date datePoste, Integer nbDeVisites, ArrayList<String> categories, String statistique, float prix, ArrayList<String> paiement, List<String> images) {
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

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public String getStatistique() {
        return statistique;
    }

    public void setStatistique(String statistique) {
        this.statistique = statistique;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public ArrayList<String> getPaiement() {
        return paiement;
    }

    public void setPaiement(ArrayList<String> paiement) {
        this.paiement = paiement;
    }

    public boolean isEstProfessionnel() {
        return estProfessionnel;
    }

    public void setEstProfessionnel(boolean estProfessionnel) {
        this.estProfessionnel = estProfessionnel;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Exclude
    public String getFirstImage(){return this.images.get(0);}

    @Exclude
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
