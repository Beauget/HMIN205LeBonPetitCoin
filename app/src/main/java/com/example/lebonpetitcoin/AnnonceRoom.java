package com.example.lebonpetitcoin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
//https://www.simplifiedcoding.net/android-upload-image-to-server-using-php-mysql/
@Entity
public class AnnonceRoom {
    @PrimaryKey
    public int uAnnonce;

    @ColumnInfo(name = "vendeur")
    public String identifiant;

    @ColumnInfo(name = "titre")
    public String titre;

    @ColumnInfo(name = "prix")
    public float prix;

    @ColumnInfo(name = "images")
    public List<Uri> listeImages;

}
