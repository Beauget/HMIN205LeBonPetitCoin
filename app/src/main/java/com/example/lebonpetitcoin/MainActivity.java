package com.example.lebonpetitcoin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private MaterialToolbar topAppBar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ajout de l'app bar
        topAppBar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawerLayout);
        //setNavigationViewListener();
        drawerLayout.openDrawer(GravityCompat.START);

    }


    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.activity_main_drawer_news: {
                //do somthing
                break;
            }
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setNavigationViewListener() {
        NavigationView navigationView =findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
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