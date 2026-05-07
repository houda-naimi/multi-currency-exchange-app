package com.example.evaluation_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.Map;

public class Convertor extends AppCompatActivity {
    private Map<String, Double> conversionRates;
    private FloatingActionButton menuBtn, translateBtn;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView convertImage;
    private TextView resultTextView, text, text1, text2, text3, text4, text5;
    private LinearLayout corner;
    private Spinner inputCurrencySpinner, outputCurrencySpinner;
    private EditText inputAmount;
    private Button convertButton;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_convertor);

        menuBtn = findViewById(R.id.MenuBtn);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        inputCurrencySpinner = findViewById(R.id.inputCurrencySpinner);
        outputCurrencySpinner = findViewById(R.id.outputCurrencySpinner);
        convertButton = findViewById(R.id.ConvertBtn);
        inputAmount = findViewById(R.id.inputAmount);
        resultTextView = findViewById(R.id.result);
        text = findViewById(R.id.text);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);
        corner = findViewById(R.id.layout);
        convertImage = findViewById(R.id.convertImage);
        progressBar = findViewById(R.id.progressBar);

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
                    Intent homeIntent = new Intent(Convertor.this, Home.class);
                    startActivity(homeIntent);
                }
                else if (item.getItemId() == R.id.nav_dashboard) {
                    Intent profileIntent = new Intent(Convertor.this, Dashboard.class);
                    startActivity(profileIntent);
                }
                else if (item.getItemId() == R.id.nav_register) {
                    Intent settingsIntent = new Intent(Convertor.this, Register.class);
                    startActivity(settingsIntent);
                }
                else if (item.getItemId() == R.id.nav_logout) {
                    SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    Intent goToWelcomePage = new Intent(Convertor.this, Welcome.class);
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
                    LanguageChanging.changeLanguage(Convertor.this, "fr");
                } else {
                    LanguageChanging.changeLanguage(Convertor.this, "en");
                }
                recreate();
            }
        });

        progressBar.setVisibility(View.VISIBLE);
        translateBtn.setVisibility(View.GONE);
        corner.setVisibility(View.GONE);
        convertImage.setVisibility(View.GONE);
        text.setVisibility(View.GONE);
        text1.setVisibility(View.GONE);
        text2.setVisibility(View.GONE);
        text3.setVisibility(View.GONE);
        text4.setVisibility(View.GONE);
        text5.setVisibility(View.GONE);
        inputAmount.setVisibility(View.GONE);
        inputCurrencySpinner.setVisibility(View.GONE);
        outputCurrencySpinner.setVisibility(View.GONE);
        convertButton.setVisibility(View.GONE);
        resultTextView.setVisibility(View.GONE);
        CurrencyService.fetchExchangeRates(this, new CurrencyService.CurrencyServiceCallback() {
            @Override
            public void onSuccess(Map<String, Double> rates) {
                progressBar.setVisibility(View.GONE);
                translateBtn.setVisibility(View.VISIBLE);
                corner.setVisibility(View.VISIBLE);
                convertImage.setVisibility(View.VISIBLE);
                text.setVisibility(View.VISIBLE);
                text1.setVisibility(View.VISIBLE);
                text2.setVisibility(View.VISIBLE);
                text3.setVisibility(View.VISIBLE);
                text4.setVisibility(View.VISIBLE);
                text5.setVisibility(View.VISIBLE);
                inputAmount.setVisibility(View.VISIBLE);
                inputCurrencySpinner.setVisibility(View.VISIBLE);
                outputCurrencySpinner.setVisibility(View.VISIBLE);
                convertButton.setVisibility(View.VISIBLE);
                resultTextView.setVisibility(View.VISIBLE);

                conversionRates = rates;
                ArrayList<String> currencyList = new ArrayList<>(rates.keySet());
                ArrayAdapter<String> currencyAdapter = new ArrayAdapter<>(Convertor.this, android.R.layout.simple_spinner_item, currencyList);
                currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                inputCurrencySpinner.setAdapter(currencyAdapter);
                outputCurrencySpinner.setAdapter(currencyAdapter);
            }
            @Override
            public void onFailure(Exception e) {
                Log.d("CurrencyService", "onFailure called: " + e.getMessage());


                Toast.makeText(Convertor.this, "Erreur de récupération des taux", Toast.LENGTH_SHORT).show();
            }
        });

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amountText = inputAmount.getText().toString();
                if (!amountText.isEmpty() && conversionRates != null) {
                    double amount = Double.parseDouble(amountText);
                    String inputCurrency = inputCurrencySpinner.getSelectedItem().toString();
                    String outputCurrency = outputCurrencySpinner.getSelectedItem().toString();
                    if (conversionRates.containsKey(inputCurrency) && conversionRates.containsKey(outputCurrency)) {
                        double inputRate = conversionRates.get(inputCurrency);
                        double outputRate = conversionRates.get(outputCurrency);
                        double convertedAmount = (amount / inputRate) * outputRate;
                        resultTextView.setText(String.valueOf(convertedAmount));
                    } else {
                        Toast.makeText(Convertor.this, "Conversion rate missing", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Convertor.this, "Please enter a valid amount or check your connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}