package com.example.lebonpetitcoin.ClassFirestore;

import java.util.Calendar;
import java.util.Date;

public class Conversation {
    String compte1;
    String compte2;
    String message;
    Date date;

    public Conversation(){

    }

    public Conversation(String compte1, String compte2,String message) {
        this.compte1 = compte1;
        this.compte2 = compte2;
        this.date = Calendar.getInstance().getTime();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCompte1() {
        return compte1;
    }

    public void setCompte1(String compte1) {
        this.compte1 = compte1;
    }

    public String getCompte2() {
        return compte2;
    }

    public void setCompte2(String compte2) {
        this.compte2 = compte2;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
