package com.example.android.popularmoviesstageone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start MainActivity
        startActivity(new Intent(SplashActivity.this, MainActivity.class));

        // Close Splash Screen
        finish();
    }
}
