package com.example.lebonpetitcoin.ClassFirestore;

import java.util.ArrayList;

public class Position {
    private ArrayList<departement> liste;

    public Position(){
        liste.add(new departement("01","Ain",5.2056,46.0558));
        liste.add(new departement("02","Aisne",3.3330,49.3334));
        liste.add(new departement("03","Allier",3.1118,46.2337));
        liste.add(new departement("04","Alpes-de-Haute-Provence",6.1438,44.0622));
        liste.add(new departement("05","Hautes-Alpes",6.1547,44.3949));
        liste.add(new departement("06","Alpes-Maritimes",7.0659,43.5615));
        liste.add(new departement("07","Ardèche",4.2529,44.4506));
        liste.add(new departement("08","Ardennes",4.3827,49.3656));
        liste.add(new departement("09","Ariège",1.3014,42.5515));
        liste.add(new departement("10","Aube",4.0942,48.1816));
        liste.add(new departement("11","Aude",2.2451,43.0612));
        liste.add(new departement("12","Aveyron",2.4047,44.1649));
        liste.add(new departement("13","Bouches-du-Rhône",5.0511,43.3236));
        liste.add(new departement("14","Calvados",0.2149,49.0559));
        liste.add(new departement("15","Cantal",2.4007,45.0304));
        liste.add(new departement("16","Charente",0.1206,45.4305));
        liste.add(new departement("17","Charente-Maritime",0.4028,45.4651));
        liste.add(new departement("18","Cher",2.2928,47.0353));
        liste.add(new departement("19","Corrèze",1.5237,45.2125));
        liste.add(new departement("2A","Corse-du-Sud",8.5917,41.5149));
        liste.add(new departement("2B","Haute-Corse",9.1223,42.2339));
        liste.add(new departement("21","Côte-d'Or",4.4620,47.2529));
        liste.add(new departement("22","",9.9999,99.9999));
        liste.add(new departement("23","",9.9999,99.9999));
        liste.add(new departement("24","",9.9999,99.9999));
        liste.add(new departement("25","",9.9999,99.9999));
        liste.add(new departement("26","",9.9999,99.9999));
        liste.add(new departement("27","",9.9999,99.9999));
        liste.add(new departement("28","",9.9999,99.9999));
        liste.add(new departement("29","",9.9999,99.9999));
        liste.add(new departement("30","",9.9999,99.9999));
        liste.add(new departement("31","",9.9999,99.9999));
        liste.add(new departement("32","",9.9999,99.9999));
        liste.add(new departement("33","",9.9999,99.9999));
        liste.add(new departement("34","",9.9999,99.9999));
        liste.add(new departement("35","",9.9999,99.9999));
        liste.add(new departement("36","",9.9999,99.9999));





    }


    public  class departement{
        private String chiffre;
        private String nom;
        private double longitude;
        private double latitude;

        public departement(String chiffre, String nom, double longitude, double latitude) {
            this.chiffre = chiffre;
            this.nom = nom;
            this.longitude = longitude;
            this.latitude = latitude;
        }
    }
}
