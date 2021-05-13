package com.example.lebonpetitcoin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_TIME = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent to_main = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(to_main);
                finish();
            }
        }, SPLASH_TIME);


    }
}