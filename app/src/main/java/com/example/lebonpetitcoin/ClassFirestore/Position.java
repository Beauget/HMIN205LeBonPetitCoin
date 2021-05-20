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
        liste.add(new Departement("38","Isere",5.3434,45.1548));
        liste.add(new Departement("39","Jura",5.4152,46.4342));
        liste.add(new Departement("40","Landes",0.4702,43.5756));

        liste.add(new Departement("41","Loir-et-Cher",1.2546,47.3700));
        liste.add(new Departement("42","Loire",4.0957,45.4337));
        liste.add(new Departement("43","Haute-Loire",3.4823,45.0741));
        liste.add(new Departement("44","Loire-Atlantique",1.4056,47.2141));
        liste.add(new Departement("45","Loiret",2.2039,47.5443));
        liste.add(new Departement("46","Lot",1.3617,44.3727));
        liste.add(new Departement("47","Lot-et-Garonne",0.2737,44.2203));
        liste.add(new Departement("48","Lozere",3.3001,44.3102));
        liste.add(new Departement("49","Maine-et-Loire",0.3351,47.2327));

        liste.add(new Departement("50","Manche",1.1939,49.0446));
        liste.add(new Departement("51","Marne",4.1449,48.5657));
        liste.add(new Departement("52","Haute-Marne",5.1935,48.0634));
        liste.add(new Departement("53","Mayenne",0.3929,48.0848));
        liste.add(new Departement("54","Meurthe-et-Moselle",6.0954,48.4713));
        liste.add(new Departement("55","Meuse",5.2254,48.5922));
        liste.add(new Departement("56","Morbihan",2.4836,47.5047));
        liste.add(new Departement("57","Moselle",6.3948,49.0214));
        liste.add(new Departement("58","Nievre",3.3017,47.0655));
        liste.add(new Departement("59","Nord",3.1314,50.2650));

        liste.add(new Departement("60","Oise",2.2531,49.2437));
        liste.add(new Departement("61","Orne",0.0744,48.3725));
        liste.add(new Departement("62","Pas-de-Calais",2.1719,50.2937));
        liste.add(new Departement("63","Puy-de-Dome",3.0827,45.4333));
        liste.add(new Departement("64","Pyrénées-Atlantiques",0.4541,43.1524));
        liste.add(new Departement("65","Hautes-Pyrénées",0.0950,43.0311));
        liste.add(new Departement("66","Pyrénées-Orientales",2.3120,42.3600));
        liste.add(new Departement("67","Bas-Rhin",7.3305,48.4015));
        liste.add(new Departement("68","Haut-Rhin",7.1627,47.5131));
        liste.add(new Departement("69","Rhone",4.4829,45.5213));

        liste.add(new Departement("70","Haute-Saone",6.0510,47.3828));
        liste.add(new Departement("71","Saone-et-Loire",4.3232,46.3841));
        liste.add(new Departement("72","Sarthe",0.1320,47.5940));
        liste.add(new Departement("73","Savoie",6.2637,45.2839));
        liste.add(new Departement("74","Haute-Savoie",6.2541,46.0204));
        liste.add(new Departement("75","Paris",2.2032,48.5124));
        liste.add(new Departement("76","Seine-Maritime",1.0135,49.3918));
        liste.add(new Departement("77","Seine-et-Marne",2.5600,48.3736));
        liste.add(new Departement("78","Yvelines",1.5030,48.4854));
        liste.add(new Departement("79","Deux-Sevres",0.1902,46.3320));

        liste.add(new Departement("80","Somme",2.1640,49.5729));
        liste.add(new Departement("81","Tarn",2.0958,43.4707));
        liste.add(new Departement("82","Tarn-et-Garonne",1.1655,44.0509));
        liste.add(new Departement("83","Var",6.1305,43.2738));
        liste.add(new Departement("84","Vaucluse",5.1110,43.5938));
        liste.add(new Departement("85","Vendee",1.1752,46.4029));
        liste.add(new Departement("86","Vienne",0.2737,46.3350));
        liste.add(new Departement("87","Haute-Vienne",1.1407,45.5330));
        liste.add(new Departement("88","Vosges",6.2250,48.1148));
        liste.add(new Departement("89","Yonne",3.3352,47.5023));

        liste.add(new Departement("90","Territoire de Belfort",6.5543,47.3754));
        liste.add(new Departement("91","Essonne",2.1435,48.3120));
        liste.add(new Departement("92","Haut-de-Seine",2.1445,48.5050));
        liste.add(new Departement("93","Seine-Saint-Denis",2.2841,48.5503));
        liste.add(new Departement("94","Val-de-Marne",2.2808,48.4639));
        liste.add(new Departement("95","Val-d'Oise",2.0752,49.0458));
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
