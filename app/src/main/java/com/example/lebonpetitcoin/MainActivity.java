package com.example.lebonpetitcoin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.lebonpetitcoin.ClassFirestore.Compte;
import com.example.lebonpetitcoin.ClassFirestore.Message;
import com.example.lebonpetitcoin.Fragments.AccountFragment;
import com.example.lebonpetitcoin.Fragments.AccueilFragment;

import com.example.lebonpetitcoin.Fragments.FavFragment;
import com.example.lebonpetitcoin.Fragments.MessageFragment;
import com.example.lebonpetitcoin.Fragments.ModifierAnnonceFragment;
import com.example.lebonpetitcoin.Fragments.ModifierAnnoncesFragment;
import com.example.lebonpetitcoin.Fragments.ParametresFragment;
import com.example.lebonpetitcoin.Fragments.RechercheAvanceeFragment;
import com.example.lebonpetitcoin.Fragments.ResultatFragment;
import com.example.lebonpetitcoin.Fragments.SignInFragment;
import com.example.lebonpetitcoin.Fragments.SignUpFragment;
import com.example.lebonpetitcoin.Fragments.StatsFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = "Main activity";
    //RECUPERATION DE LA DB
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference cAnnonce = firestoreDB.collection("Annonce");
    private CollectionReference cCategorie = firestoreDB.collection("Categorie");
    private CollectionReference cCompte= firestoreDB.collection("Compte");

    MaterialToolbar topAppBar;
    NavigationView navigationView;
    Menu nav_Menu;
    View headerLayout;
    ImageView header_Menu ;
    DrawerLayout drawerLayout;
    FloatingActionButton add;
    ActionMenuItemView home;
    String imgProfile ;
    public String lecteur;
    boolean estProfessionnel;
    private static final int RC_SIGN_IN = 123;

    //FRAGMENTS/activité selon ce qui est cliqué
    private Fragment fragmentAccueil;
    private Fragment fragmentAccount;
    private Fragment fragmentMessage;
    private Fragment fragmentFav;
    private Fragment fragmentStats;
    private Fragment fragmentSignIn;
    private Fragment fragmentSignUp;
    private Fragment fragmentParametres;
    private Fragment fragmentResultat;
    private Fragment fragmentRechercheAvancee;
    private Fragment fragmentModifierAnnonces;

    //VALEUR RETOURNE SELON LE CLIQUE
    private static final int FRAGMENT_ACCOUNT = 0;
    private static final int FRAGMENT_MESSAGE = 1;
    private static final int FRAGMENT_FAV = 2;
    private static final int FRAGMENT_STATS = 3;
    private static final int FRAGMENT_SIGNIN = 4;
    private static final int FRAGMENT_SIGNUP = 5;
    private static final int FRAGMENT_PARAMETRES = 6;
    private static final int FRAGMENT_RESULTAT = 7;
    private static final int FRAGMENT_RECHERCHE_AVANCE = 8;
    private static final int FRAGMENT_MODIFIER_ANNONCES= 9;

    // [START declare_auth]
    public FirebaseAuth mAuth;
    // [END declare_auth]

    private String mCustomToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        setContentView(R.layout.activity_main);
        navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        nav_Menu = navigationView.getMenu();
        headerLayout = navigationView.getHeaderView(0);
        header_Menu = headerLayout.findViewById(R.id.imageView);

        this.configureTopAppBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureBottomAppBar();

        this.showAccueilFragment();

    }


    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        if (mAuth.getCurrentUser()!=null){
            getCompte(mAuth.getCurrentUser().getUid());
        }
        else {
            lecteur = "";
            Toast.makeText(MainActivity.this, getString(R.string.notConnected), Toast.LENGTH_SHORT).show();
        }
    }
    // [END on_start_check_user]

    private void startSignIn() {
        // [START sign_in_custom]
        mAuth.signInWithCustomToken(mCustomToken)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCustomToken:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

        // [END sign_in_custom]
    }


    public void updateUI(FirebaseUser user) {
        if(user!=null){
            //Toast.makeText(this, "connecté", Toast.LENGTH_SHORT).show();

            hideItemConnected();
        }
        else {
            hideItemDisconnected();
        }
    }

    private void hideItemDisconnected()
    {

        //navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);

        if (header_Menu.getDrawable() != null) {
            header_Menu.setImageDrawable(null);
        }
        nav_Menu.findItem(R.id.activity_main_drawer_account).setVisible(false);
        nav_Menu.findItem(R.id.activity_main_drawer_message).setVisible(false);
        nav_Menu.findItem(R.id.activity_main_drawer_fav).setVisible(false);
        nav_Menu.findItem(R.id.activity_main_drawer_stats).setVisible(false);
        nav_Menu.findItem(R.id.activity_main_drawer_signIn).setVisible(true);
        nav_Menu.findItem(R.id.activity_main_drawer_signUp).setVisible(true);
        nav_Menu.findItem(R.id.activity_main_drawer_deconnect).setVisible(false);
        nav_Menu.findItem(R.id.activity_main_drawer_modifier_annonce).setVisible(false);

    }

    private void hideItemConnected()
    {

        //Uri imgProfile = mAuth.getCurrentUser().getPhotoUrl();
        getCompte(mAuth.getCurrentUser().getUid());

        nav_Menu.findItem(R.id.activity_main_drawer_signIn).setVisible(false);
        nav_Menu.findItem(R.id.activity_main_drawer_signUp).setVisible(false);
        nav_Menu.findItem(R.id.activity_main_drawer_deconnect).setVisible(true);

        nav_Menu.findItem(R.id.activity_main_drawer_account).setVisible(true);
        nav_Menu.findItem(R.id.activity_main_drawer_message).setVisible(true);
        nav_Menu.findItem(R.id.activity_main_drawer_fav).setVisible(true);
        nav_Menu.findItem(R.id.activity_main_drawer_modifier_annonce).setVisible(true);

    }


    public void createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.Theme_LeBonPetitCoin)
                        .setLogo(R.drawable.ic_logo)
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_create_intent]
    }


    private void configureTopAppBar() {
        this.topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
    }

    private void configureDrawerLayout() {
        this.drawerLayout = findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, topAppBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }


    private void configureNavigationView() {
        this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void configureBottomAppBar() {

        this.add = (FloatingActionButton) findViewById(R.id.activity_main_button_add);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser()!=null) {

                    Intent myIntent = new Intent(MainActivity.this, AddAnnonceActivity.class);
                    myIntent.putExtra("name",mAuth.getCurrentUser().getUid());
                    MainActivity.this.startActivity(myIntent);
                }
                else{
                    Toast.makeText(v.getContext(), getString(R.string.notConnected), Toast.LENGTH_SHORT).show();
                }
            }
        });

/*        this.home = findViewById(R.id.bottomAppBar_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.showAccueilFragment();
            }
        });*/
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        // Show fragment after user clicked on a menu item
        switch (id) {
            case R.id.activity_main_drawer_account:
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
            case R.id.activity_main_drawer_signIn:{
                this.showFragment(FRAGMENT_SIGNIN);
                break;
            }
            case R.id.activity_main_drawer_signUp:
                this.showFragment(FRAGMENT_SIGNUP);
                break;
            case R.id.activity_main_drawer_parametres:
                this.showFragment(FRAGMENT_PARAMETRES);
                break;
            case R.id.activity_main_drawer_resultat:
                this.showFragment(FRAGMENT_RESULTAT);
                break;
            case R.id.activity_main_drawer_deconnect:
                {
                    FirebaseAuth.getInstance().signOut();
                    this.showAccueilFragment();
                    Toast.makeText(MainActivity.this,getString(R.string.disconnected), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.activity_main_drawer_recherche_avancee:
                this.showFragment(FRAGMENT_RECHERCHE_AVANCE);
                break;
            case R.id.activity_main_drawer_modifier_annonce:
                this.showFragment(FRAGMENT_MODIFIER_ANNONCES);
                break;
            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        return true;
    }


    private void showFragment(int fragmentIdentifier) {
        switch (fragmentIdentifier) {
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
            case FRAGMENT_SIGNIN:
                this.showSignInFragment();
                break;
            case FRAGMENT_SIGNUP:
                this.showSignUpFragment();
                break;
            case FRAGMENT_PARAMETRES:
                this.showParametresFragment();
                break;
            case FRAGMENT_RESULTAT:
                this.showResultatFragment();
                break;
            case FRAGMENT_RECHERCHE_AVANCE:
                this.showRechercheAvanceeFragment();
                break;
            case FRAGMENT_MODIFIER_ANNONCES:
                this.showModifierAnnonce();
                break;
            default:
                break;
        }
    }

    // Création de chaque fragment
    private void showAccueilFragment() {
        if (this.fragmentAccount == null) this.fragmentAccueil = AccueilFragment.newInstance();
        this.startTransactionFragment(this.fragmentAccueil);
    }

    public void showAccountFragment() {
        if (this.fragmentAccount == null) this.fragmentAccount = AccountFragment.newInstance();
        this.startTransactionFragment(this.fragmentAccount);
    }

    private void showMessageFragment() {
        if (this.fragmentMessage== null) this.fragmentMessage= MessageFragment.newInstance();
        Bundle arguments = new Bundle();
        arguments.putString( "lecteur", lecteur);
        fragmentMessage.setArguments(arguments);
        this.getSupportFragmentManager()
                .beginTransaction().replace(R.id.activity_main_frame_layout, fragmentMessage).commit();
    }

    public void showFavFragment() {
        if (this.fragmentFav == null) this.fragmentFav = FavFragment.newInstance();
        this.startTransactionFragment(this.fragmentFav);
    }

    private void showStatsFragment() {
        if (this.fragmentStats == null) this.fragmentStats = StatsFragment.newInstance();
        this.startTransactionFragment(this.fragmentStats);
    }

    public void showSignInFragment() {
        if (this.fragmentSignIn == null) this.fragmentSignIn = SignInFragment.newInstance();
        this.startTransactionFragment(this.fragmentSignIn);
    }

    public void showSignUpFragment() {
        if (this.fragmentSignUp == null) this.fragmentSignUp = SignUpFragment.newInstance();
        this.startTransactionFragment(this.fragmentSignUp);
    }

    public void showParametresFragment() {
        if (this.fragmentParametres == null) this.fragmentParametres= ParametresFragment.newInstance();
        this.startTransactionFragment(this.fragmentParametres);
    }

    public void showResultatFragment() {
        if (this.fragmentResultat== null) this.fragmentResultat= ResultatFragment.newInstance();
        this.startTransactionFragment(this.fragmentResultat);
    }

    public void showRechercheAvanceeFragment() {
        if (this.fragmentRechercheAvancee== null) this.fragmentRechercheAvancee= RechercheAvanceeFragment.newInstance();
        this.startTransactionFragment(this.fragmentRechercheAvancee);
    }

    public void showModifierAnnonce() {
        if (this.fragmentModifierAnnonces== null) this.fragmentModifierAnnonces= ModifierAnnoncesFragment.newInstance();
        this.startTransactionFragment(this.fragmentModifierAnnonces);
    }

    // Generic method that will replace and show a fragment inside the MainActivity Frame Layout
    private void startTransactionFragment(Fragment fragment) {
        if (!fragment.isVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_main_frame_layout, fragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_appbar_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.nav_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Recherche...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                Fragment fragment = ResultatFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putString("recherche", query);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.activity_main_frame_layout, fragment).commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //search(newText);
                return false;
            }
        });
            return true;
        }

    public void getCompte(String uid){
        Task<QuerySnapshot> query = cCompte.whereEqualTo("uid", uid).get();
        // future.get() blocks on response
        query.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Compte compte = document.toObject(Compte.class);
                        imgProfile = compte.getImageProfile();
                        estProfessionnel = compte.getEstProfessionnel();
                        lecteur = compte.getPseudo();

                        if(imgProfile.length()>0){
                            GlideApp.with(MainActivity.this)
                                    .load(imgProfile)
                                    .centerCrop() // this cropping technique scales the image so that it fills the requested bounds and then crops the extra.
                                    .into(header_Menu);
                        }
                        else{
                            GlideApp.with(MainActivity.this)
                                    .load("https://firebasestorage.googleapis.com/v0/b/lebonpetitcoin-6928c.appspot.com/o/noPP.jpg?alt=media&token=a8e9f70c-85e6-48ad-9f00-433c726f9da2")
                                    .centerCrop() // this cropping technique scales the image so that it fills the requested bounds and then crops the extra.
                                    .into(header_Menu);
                        }
                        if(estProfessionnel==true){
                            nav_Menu.findItem(R.id.activity_main_drawer_stats).setVisible(true);
                        }

                    }
                }
            }
        });
    }



}
