package com.example.evaluation_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class Home extends AppCompatActivity {
    private FloatingActionButton menuBtn, translateBtn;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RelativeLayout card1, card2, card3, card4;
    private int userId;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();

        menuBtn = findViewById(R.id.MenuBtn);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);


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
                    Intent homeIntent = new Intent(Home.this, Home.class);
                    startActivity(homeIntent);
                }
                else if (item.getItemId() == R.id.nav_dashboard) {
                    Intent profileIntent = new Intent(Home.this, Dashboard.class);
                    startActivity(profileIntent);
                }
                else if (item.getItemId() == R.id.nav_register) {
                    Intent settingsIntent = new Intent(Home.this, Register.class);
                    startActivity(settingsIntent);
                }
                else if (item.getItemId() == R.id.nav_logout) {
                    SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    Intent goToWelcomePage = new Intent(Home.this, Welcome.class);
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
                    LanguageChanging.changeLanguage(Home.this, "fr");
                } else {
                    LanguageChanging.changeLanguage(Home.this, "en");
                }
                recreate();
            }
        });

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Convertor.class);
                startActivity(intent);
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Dashboard.class);
                startActivity(intent);            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PopupTest", "Card3 clicked");
                showRatingPopup();
            }
        });
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent goToWelcomePage = new Intent(Home.this, Welcome.class);
                startActivity(goToWelcomePage);
                finish();
            }
        });
    }

    private void showRatingPopup() {

        try {
            LayoutInflater inflater = LayoutInflater.from(this);
            View ratingView = inflater.inflate(R.layout.activity_reviews, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(ratingView);
            AlertDialog dialog = builder.create();

            RatingBar ratingBar = ratingView.findViewById(R.id.ratingBar);
            Button rateBtn = ratingView.findViewById(R.id.rateBtn);
            Button ignoreBtn = ratingView.findViewById(R.id.ignoreBtn);

            if (ratingBar == null || rateBtn == null) {
                Toast.makeText(this, "Error loading rating dialog", Toast.LENGTH_SHORT).show();
                return;
            }

            rateBtn.setOnClickListener(v -> {
                float ratingValue = ratingBar.getRating();
                DatabaseHelper dbHelper = new DatabaseHelper(this);
                dbHelper.insertRating(userId, ratingValue);
                Toast.makeText(Home.this, "Thank you for rating us", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });

            ignoreBtn.setOnClickListener(v -> {
                dialog.dismiss();
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error showing popup: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}