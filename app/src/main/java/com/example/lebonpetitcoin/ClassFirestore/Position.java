package com.example.lebonpetitcoin.ClassFirestore;

import java.util.ArrayList;
//https://www.sigtv.fr/L-IGN-edite-la-nouvelle-carte-de-la-France-administrative-et-calcule-les-centres-geographiques-de-chaque-Departement_a334.html

public class Position {
    private ArrayList<Departement> liste = new ArrayList<>();

    public Position(){
        liste.add(new Departement("01","Ain",5.2056,46.0558));
        liste.add(new Departement("02","Aisne",3.3330,49.3334));
        liste.add(new Departement("03","Allier",3.1118,46.2337));
        liste.add(new Departement("04","Alpes-de-Haute-Provence",6.1438,44.0622));
        liste.add(new Departement("05","Hautes-Alpes",6.1547,44.3949));
        liste.add(new Departement("06","Alpes-Maritimes",7.0659,43.5615));
        liste.add(new Departement("07","Ardèche",4.2529,44.4506));
        liste.add(new Departement("08","Ardennes",4.3827,49.3656));
        liste.add(new Departement("09","Ariège",1.3014,42.5515));
        liste.add(new Departement("10","Aube",4.0942,48.1816));
        liste.add(new Departement("11","Aude",2.2451,43.0612));
        liste.add(new Departement("12","Aveyron",2.4047,44.1649));
        liste.add(new Departement("13","Bouches-du-Rhône",5.0511,43.3236));
        liste.add(new Departement("14","Calvados",0.2149,49.0559));
        liste.add(new Departement("15","Cantal",2.4007,45.0304));
        liste.add(new Departement("16","Charente",0.1206,45.4305));
        liste.add(new Departement("17","Charente-Maritime",0.4028,45.4651));
        liste.add(new Departement("18","Cher",2.2928,47.0353));
        liste.add(new Departement("19","Corrèze",1.5237,45.2125));
        liste.add(new Departement("2A","Corse-du-Sud",8.5917,41.5149));
        liste.add(new Departement("2B","Haute-Corse",9.1223,42.2339));
        liste.add(new Departement("21","Cote-d'Or",4.4620,47.2529));
        liste.add(new Departement("22","Cote-d'Armor",2.5151,48.2628));
        liste.add(new Departement("23","Creuse",2.0108,46.0525));
        liste.add(new Departement("24","Dordogne",0.4429,45.0615));
        liste.add(new Departement("25","Doubs",6.2142,47.0955));
        liste.add(new Departement("26","Drome",5.1005,44.4103));
        liste.add(new Departement("27","Eure",0.5946,49.0649));
        liste.add(new Departement("28","Eure-et-Loir",1.2213,48.2315));
        liste.add(new Departement("29","Finistere",4.0332,48.1540));
        liste.add(new Departement("30","Gard",4.1049,43.5936));
        liste.add(new Departement("31","Haute-Garonne",1.1022,43.2131));
        liste.add(new Departement("32","Gers",0.2712,43.4134));
        liste.add(new Departement("33","Gironde",0.3431,44.4931));
        liste.add(new Departement("34","Herault",3.2202,43.3447));
        liste.add(new Departement("35","Ile-et-Vilaine",1.3819,48.0916));
        liste.add(new Departement("36","Indre",1.3433,46.4640));
        liste.add(new Departement("37","Indre-et-Loire",0.4129,47.1529));
        liste.add(new Departement("38","",5.3434,45.1548));
        liste.add(new Departement("39","Jura",5.4152,46.4342));
        liste.add(new Departement("40","Landes",0.4702,43.5756));

        liste.add(new Departement("41","",9.9999,99.9999));
        liste.add(new Departement("42","",9.9999,99.9999));
        liste.add(new Departement("43","",9.9999,99.9999));
        liste.add(new Departement("44","",9.9999,99.9999));
        liste.add(new Departement("45","",9.9999,99.9999));
        liste.add(new Departement("46","",9.9999,99.9999));
        liste.add(new Departement("47","",9.9999,99.9999));
        liste.add(new Departement("48","",9.9999,99.9999));
        liste.add(new Departement("49","",9.9999,99.9999));

        liste.add(new Departement("50","",9.9999,99.9999));
        liste.add(new Departement("51","",9.9999,99.9999));
        liste.add(new Departement("52","",9.9999,99.9999));
        liste.add(new Departement("53","",9.9999,99.9999));
        liste.add(new Departement("54","",9.9999,99.9999));
        liste.add(new Departement("55","",9.9999,99.9999));
        liste.add(new Departement("56","",9.9999,99.9999));
        liste.add(new Departement("57","",9.9999,99.9999));
        liste.add(new Departement("58","",9.9999,99.9999));
        liste.add(new Departement("59","",9.9999,99.9999));

        liste.add(new Departement("60","",9.9999,99.9999));
        liste.add(new Departement("61","",9.9999,99.9999));
        liste.add(new Departement("62","",9.9999,99.9999));
        liste.add(new Departement("63","",9.9999,99.9999));
        liste.add(new Departement("64","",9.9999,99.9999));
        liste.add(new Departement("65","",9.9999,99.9999));
        liste.add(new Departement("66","",9.9999,99.9999));
        liste.add(new Departement("67","",9.9999,99.9999));
        liste.add(new Departement("68","",9.9999,99.9999));
        liste.add(new Departement("69","",9.9999,99.9999));

        liste.add(new Departement("70","",9.9999,99.9999));
        liste.add(new Departement("71","",9.9999,99.9999));
        liste.add(new Departement("72","",9.9999,99.9999));
        liste.add(new Departement("73","",9.9999,99.9999));
        liste.add(new Departement("74","",9.9999,99.9999));
        liste.add(new Departement("75","",9.9999,99.9999));
        liste.add(new Departement("76","",9.9999,99.9999));
        liste.add(new Departement("77","",9.9999,99.9999));
        liste.add(new Departement("78","",9.9999,99.9999));
        liste.add(new Departement("79","",9.9999,99.9999));

        liste.add(new Departement("80","",9.9999,99.9999));
        liste.add(new Departement("81","",9.9999,99.9999));
        liste.add(new Departement("82","",9.9999,99.9999));
        liste.add(new Departement("83","",9.9999,99.9999));
        liste.add(new Departement("84","",9.9999,99.9999));
        liste.add(new Departement("85","",9.9999,99.9999));
        liste.add(new Departement("86","",9.9999,99.9999));
        liste.add(new Departement("87","",9.9999,99.9999));
        liste.add(new Departement("88","",9.9999,99.9999));
        liste.add(new Departement("89","",9.9999,99.9999));

        liste.add(new Departement("90","",9.9999,99.9999));
        liste.add(new Departement("91","",9.9999,99.9999));
        liste.add(new Departement("92","",9.9999,99.9999));
        liste.add(new Departement("93","",9.9999,99.9999));
        liste.add(new Departement("94","",9.9999,99.9999));
        liste.add(new Departement("95","",9.9999,99.9999));
    }

    public ArrayList<Departement> getListe() {
        return liste;
    }

    public void setListe(ArrayList<Departement> liste) {
        this.liste = liste;
    }

    public boolean isDepartement(String d){
        for (Departement departement : this.liste){
            if (departement.getChiffre().equals(d)){
                return  true;
            }
        }
        return false;
    }

    public Departement getDepartement(String d){
        for (Departement departement : this.liste){
            if (departement.getChiffre().equals(d)){
                return departement;
            }
        }
        return null;
    }

    public  class Departement{
        private String chiffre;
        private String nom;
        private double longitude;
        private double latitude;

        public Departement(String chiffre, String nom, double longitude, double latitude) {
            this.chiffre = chiffre;
            this.nom = nom;
            this.longitude = longitude;
            this.latitude = latitude;
        }

        public String getChiffre() {
            return chiffre;
        }

        public void setChiffre(String chiffre) {
            this.chiffre = chiffre;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }
    }


}
