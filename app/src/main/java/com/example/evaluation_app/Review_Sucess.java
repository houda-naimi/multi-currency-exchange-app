package com.example.evaluation_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class Review_Sucess extends AppCompatActivity {
    FloatingActionButton menuBtn, translateBtn;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private SQLiteDatabase database;
    DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review_sucess);

        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getReadableDatabase();
        if (dbHelper == null) {
            Log.e("Review_Sucess", "DatabaseHelper is null");
        }

        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1);
        Log.d("UserIdCheck", "User ID: " + userId);

        float rating = dbHelper.getRatingValue(userId);
        Log.d("UserRating", "User ID: " + userId + " - Rating: " + rating);

        TextView ratingCountTextView = findViewById(R.id.text3);
        ratingCountTextView.setText("Your rate is: " + rating);

        menuBtn = findViewById(R.id.MenuBtn);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(navigationView);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    Intent homeIntent = new Intent(Review_Sucess.this, Home.class);
                    startActivity(homeIntent);
                }
                else if (item.getItemId() == R.id.nav_dashboard) {
                    Intent profileIntent = new Intent(Review_Sucess.this, Dashboard.class);
                    startActivity(profileIntent);
                }
                else if (item.getItemId() == R.id.nav_register) {
                    Intent settingsIntent = new Intent(Review_Sucess.this, Register.class);
                    startActivity(settingsIntent);
                }
                else if (item.getItemId() == R.id.nav_logout) {
                    SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    Intent goToWelcomePage = new Intent(Review_Sucess.this, Welcome.class);
                    startActivity(goToWelcomePage);
                    finish();
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

        translateBtn = findViewById(R.id.TranslateBtn);
        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentLanguage = getResources().getConfiguration().locale.getLanguage();
                if (currentLanguage.equals("en")) {
                    LanguageChanging.changeLanguage(Review_Sucess.this, "fr");
                } else {
                    LanguageChanging.changeLanguage(Review_Sucess.this, "en");
                }
                recreate();
            }
        });

    }
}