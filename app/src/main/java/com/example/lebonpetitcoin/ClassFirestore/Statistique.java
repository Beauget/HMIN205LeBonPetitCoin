package com.example.lebonpetitcoin.ClassFirestore;

import com.google.type.Date;

import java.util.List;

public class Statistique {
    List<Date> date;

    public Statistique(){
        //public no args contructeur obligatoire sinon firebase crash
    }

    public Statistique(List<Date> date) {
        this.date = date;
    }

    public List<Date> getDate() {
        return date;
    }

    public void setDate(List<Date> date) {
        this.date = date;
    }
}
