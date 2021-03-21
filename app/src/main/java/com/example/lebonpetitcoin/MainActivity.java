package com.example.lebonpetitcoin;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.lebonpetitcoin.Fragments.AccountFragment;
import com.example.lebonpetitcoin.Fragments.AccueilFragment;
import com.example.lebonpetitcoin.Fragments.FavFragment;
import com.example.lebonpetitcoin.Fragments.MessageFragment;
import com.example.lebonpetitcoin.Fragments.StatsFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    MaterialToolbar topAppBar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    FloatingActionButton add;
    ActionMenuItemView home;

    //FRAGMENTS/activité selon ce qui est cliqué
    private Fragment fragmentAccueil;
    private Fragment fragmentAccount;
    private Fragment fragmentMessage;
    private Fragment fragmentFav;
    private Fragment fragmentStats;

    //VALEUR RETOURNE SELON LE CLIQUE
    private static final int FRAGMENT_ACCOUNT = 0;
    private static final int FRAGMENT_MESSAGE = 1;
    private static final int FRAGMENT_FAV = 2;
    private static final int FRAGMENT_STATS = 3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.configureTopAppBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureBottomAppBar();

        this.showAccueilFragment();
    }


    private void configureTopAppBar(){
        this.topAppBar =  findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
    }

    private void configureDrawerLayout(){
        this.drawerLayout = findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, topAppBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }
    private void configureNavigationView(){
        this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void configureBottomAppBar(){

        this.add= (FloatingActionButton) findViewById(R.id.activity_main_button_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, AddAnnonceActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        this.home = findViewById(R.id.bottomAppBar_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.showAccueilFragment();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        // Show fragment after user clicked on a menu item
        switch (id){
            case R.id.activity_main_drawer_account :
                this.showFragment(FRAGMENT_ACCOUNT);
                break;
            case R.id.activity_main_drawer_message:
                this.showFragment(FRAGMENT_MESSAGE);
                break;
            case R.id.activity_main_drawer_fav:
                this.showFragment(FRAGMENT_FAV);
                break;
            case R.id.activity_main_drawer_stats:
                this.showFragment(FRAGMENT_STATS);
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void showFragment(int fragmentIdentifier){
        switch (fragmentIdentifier){
            case FRAGMENT_ACCOUNT:
                this.showAccountFragment();
                break;
            case FRAGMENT_MESSAGE:
                this.showMessageFragment();
                break;
            case FRAGMENT_FAV:
                this.showFavFragment();
                break;
            case FRAGMENT_STATS:
                this.showStatsFragment();
                break;
            default:
                break;
        }
    }

    // Création de chaque fragment
    private void showAccueilFragment(){
        if (this.fragmentAccount == null) this.fragmentAccueil = AccueilFragment.newInstance();
        this.startTransactionFragment(this.fragmentAccueil);
    }

    private void showAccountFragment(){
        if (this.fragmentAccount == null) this.fragmentAccount = AccountFragment.newInstance();
        this.startTransactionFragment(this.fragmentAccount);
    }

    private void showMessageFragment(){
        if (this.fragmentMessage == null) this.fragmentMessage = MessageFragment.newInstance();
        this.startTransactionFragment(this.fragmentMessage);
    }

    private void showFavFragment(){
        if (this.fragmentFav == null) this.fragmentFav = FavFragment.newInstance();
        this.startTransactionFragment(this.fragmentFav);
    }

    private void showStatsFragment(){
        if (this.fragmentStats == null) this.fragmentStats = StatsFragment.newInstance();
        this.startTransactionFragment(this.fragmentStats);
    }

    // Generic method that will replace and show a fragment inside the MainActivity Frame Layout
    private void startTransactionFragment(Fragment fragment){
        if (!fragment.isVisible()){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_frame_layout, fragment).commit();
        }
    }


}