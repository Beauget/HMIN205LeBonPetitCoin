package com.example.lebonpetitcoin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {


    private MaterialToolbar topAppBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ajout de l'app bar
        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);


    }


    //Création du menu (mais il est à droite HIHI)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.top_appbar_menu,menu);
        return true;
    }

    //Gestion des cliques sur les items du menu
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.account:
                Intent account = new Intent(MainActivity.this,accountActivity.class);
                startActivity(account);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setSupportActionBar(Toolbar topAppBar) {

    }
}