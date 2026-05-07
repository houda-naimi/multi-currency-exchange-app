package com.example.evaluation_app;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class Welcome extends AppCompatActivity {
    private Button loginButton, registerButton;
    private FloatingActionButton translateBtn;
    private TextView welcomeMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        loginButton = findViewById(R.id.GoToLoginPage);
        registerButton = findViewById(R.id.GoToRegisterPage);
        translateBtn = findViewById(R.id.TranslateBtn);
        welcomeMessage = findViewById(R.id.welcomeText);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome.this, Login.class);
                startActivity(intent);            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome.this, Register.class);
                startActivity(intent);
            }
        });

        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentLanguage = getResources().getConfiguration().locale.getLanguage();
                if (currentLanguage.equals("en")) {
                    LanguageChanging.changeLanguage(Welcome.this, "fr");
                } else {
                    LanguageChanging.changeLanguage(Welcome.this, "en");
                }
                recreate();
            }
        });
    }
}