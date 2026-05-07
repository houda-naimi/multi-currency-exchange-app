package com.example.evaluation_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Login extends AppCompatActivity {
    private TextView GoToRegisterPage;
    private ImageView backArr;
    private FloatingActionButton fabLanguage;
    private EditText email, password;
    private Button LoginBtn;
    private FloatingActionButton translateBtn;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        LoginBtn = findViewById(R.id.LoginBtn);
        GoToRegisterPage = findViewById(R.id.RedirectiontoRegister);
        backArr = findViewById(R.id.arrowBack);

        GoToRegisterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
        backArr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Welcome.class);
                startActivity(intent);
            }
        });
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean hasError = false;
                String emailRegex = "^[a-zA-Z0-9._]+@[a-zA-Z]+\\.[a-zA-Z]{2,6}$";

                if( email.getText().toString().isEmpty() && password.getText().toString().isEmpty()) {
                    Toast.makeText(Login.this, "all fields are required!" ,Toast.LENGTH_SHORT).show();
                    hasError = true;
                } else if( email.getText().toString().isEmpty()) {
                    Toast.makeText(Login.this, "Email is required!" ,Toast.LENGTH_SHORT).show();
                    hasError = true;
                } else if (!email.getText().toString().matches(emailRegex)) {
                    Toast.makeText(Login.this, "Email is invalid!" ,Toast.LENGTH_SHORT).show();
                    hasError = true;
                } else if( password.getText().toString().isEmpty()) {
                    Toast.makeText(Login.this, "Password is required!" ,Toast.LENGTH_SHORT).show();
                    hasError = true;
                } else if( password.getText().toString().length() < 6 ) {
                    Toast.makeText(Login.this, "Password too short!" ,Toast.LENGTH_SHORT).show();
                    hasError = true;
                }

                if(!hasError) {
                    if (databaseHelper.checkUser(email.getText().toString(), password.getText().toString())) {
                        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("email", email.getText().toString());
                        editor.apply();

                        Toast.makeText(Login.this, "Login Success", Toast.LENGTH_SHORT).show();

                        Intent SuccessLogin = new Intent(Login.this, Home.class);
                        startActivity(SuccessLogin);
                    } else {
                        Toast.makeText(Login.this, "Invalid informations", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        translateBtn = findViewById(R.id.TranslateBtn);
        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentLanguage = getResources().getConfiguration().locale.getLanguage();
                if (currentLanguage.equals("en")) {
                    LanguageChanging.changeLanguage(Login.this, "fr");
                } else {
                    LanguageChanging.changeLanguage(Login.this, "en");
                }
                recreate();
            }
        });
    }
}