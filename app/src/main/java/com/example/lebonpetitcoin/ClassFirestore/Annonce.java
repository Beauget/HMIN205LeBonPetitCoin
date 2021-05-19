package com.example.lebonpetitcoin.ClassFirestore;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;
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
    ArrayList<String> images = new ArrayList<String>();

    GeoPoint position ;
    String departement;

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

    public Annonce(String auteur,String titre,String description,boolean estProfessionnel,String telephoneContact,String mailContact,float prix,ArrayList<String> mdp,ArrayList<String> cat, String uri) {
        this.titre = titre;
        this.auteur = auteur;
        this.estProfessionnel = estProfessionnel;
        this.telephoneContact = telephoneContact;
        this.mailContact = mailContact;
        this.titre = titre;
        this.images.add(uri);
        this.description = description;
        this.datePoste = Calendar.getInstance().getTime();
        this.nbDeVisites = 0;
        this.prix = prix;
        this.categories = cat;
        this.paiement= mdp;
    }
    public Annonce(String auteur,String titre,String description,boolean estProfessionnel,String telephoneContact,String mailContact,float prix,ArrayList<String> mdp,ArrayList<String> cat, ArrayList<String> uri) {
        this.titre = titre;
        this.auteur = auteur;
        this.estProfessionnel = estProfessionnel;
        this.telephoneContact = telephoneContact;
        this.mailContact = mailContact;
        this.titre = titre;
        this.images= uri;
        this.description = description;
        this.datePoste = Calendar.getInstance().getTime();
        this.nbDeVisites = 0;
        this.prix = prix;
        this.categories = cat;
        this.paiement= mdp;
    }

    public Annonce(String auteur,String titre,String description,boolean estProfessionnel,String telephoneContact,String mailContact,float prix,ArrayList<String> mdp,ArrayList<String> cat, ArrayList<String> uri,double lat,double lng,String departement) {
        this.titre = titre;
        this.auteur = auteur;
        this.estProfessionnel = estProfessionnel;
        this.telephoneContact = telephoneContact;
        this.mailContact = mailContact;
        this.titre = titre;
        this.images= uri;
        this.description = description;
        this.datePoste = Calendar.getInstance().getTime();
        this.nbDeVisites = 0;
        this.prix = prix;
        this.categories = cat;
        this.paiement= mdp;
        this.position = new GeoPoint(lat,lng);
        this.departement = departement;
    }

    public Annonce(String auteur, boolean estProfessionnel, String telephoneContact, String mailContact, String titre, String description, java.util.Date datePoste, Integer nbDeVisites, ArrayList<String> categories, String statistique, float prix, ArrayList<String> paiement, ArrayList<String> images) {
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

    public Annonce(String uid, String imageProfile, boolean estProfessionnel, String telephoneContact, String mailContact, String siret, String localisation) {

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

    public boolean getEstProfessionnel() {
        return estProfessionnel;
    }

    public void setEstProfessionnel(boolean estProfessionnel) {
        this.estProfessionnel = estProfessionnel;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    @Exclude
    public String getFirstImage(){
        if (images.size()>0)
            return this.images.get(0);
        else
            return "https://firebasestorage.googleapis.com/v0/b/lebonpetitcoin-6928c.appspot.com/o/no_image.png?alt=media&token=e4e42748-45d3-4c07-8028-d767efda4846";
    }

    @Exclude
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }


    public GeoPoint getPosition() {
        return position;
    }

    public void setPosition(GeoPoint position) {
        this.position = position;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }
}
