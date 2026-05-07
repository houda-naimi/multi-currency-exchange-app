package com.example.evaluation_app;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Register extends AppCompatActivity {
    private TextView GoToLoginPage;
    private ImageView backArr;
    private EditText username, email, password, age, phone;
    private RadioGroup genre;
    private CheckBox conditions;
    private Button registerBtn;
    private FloatingActionButton translateBtn;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();

        GoToLoginPage = findViewById(R.id.RedirectiontoLogin);
        backArr = findViewById(R.id.arrowBack);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        age = findViewById(R.id.age);
        phone = findViewById(R.id.phone);
        genre = findViewById(R.id.genre);
        conditions = findViewById(R.id.conditions);
        registerBtn = findViewById(R.id.RegisterBtn);

        GoToLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
        backArr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Welcome.class);
                startActivity(intent);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameStr = username.getText().toString();
                String emailStr = email.getText().toString();
                String passwordStr = password.getText().toString();
                String genreStr = genre.toString();
                String ageStr = age.getText().toString();
                String phoneStr = phone.getText().toString();
                String emailRegex = "^[a-zA-Z0-9._]+@[a-zA-Z]+\\.[a-zA-Z]{2,6}$";
                boolean hasError = false;

                int AgeInt = 0;
                if (!ageStr.isEmpty()) {
                    AgeInt = Integer.parseInt(ageStr);
                }

                if (username.getText().toString().isEmpty() || email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || age.getText().toString().isEmpty() || phone.getText().toString().isEmpty() || genre.getCheckedRadioButtonId() == -1 || !conditions.isChecked()) {
                    Toast.makeText(Register.this, "All fields are requierd!", Toast.LENGTH_SHORT).show();
                    hasError = true;
                } else {
                    if( email.getText().toString().isEmpty()) {
                        Toast.makeText(Register.this, "Email is required!" ,Toast.LENGTH_SHORT).show();
                        hasError = true;
                    } else if (!email.getText().toString().matches(emailRegex)) {
                        Toast.makeText(Register.this, "Email is invalid!" ,Toast.LENGTH_SHORT).show();
                        hasError = true;
                    }

                    if( password.getText().toString().isEmpty()) {
                        Toast.makeText(Register.this, "Password is required!" ,Toast.LENGTH_SHORT).show();
                        hasError = true;
                    } else if( password.getText().toString().length() < 6 ) {
                        Toast.makeText(Register.this, "Password too short!" ,Toast.LENGTH_SHORT).show();
                        hasError = true;
                    }

                    if (AgeInt < 18 || AgeInt > 60) {
                        Toast.makeText(Register.this, "Age is invalid!", Toast.LENGTH_SHORT).show();
                        hasError = true;
                    }else if (!age.getText().toString().matches("\\d+")) {
                        Toast.makeText(Register.this, "Only numbers are accepted!", Toast.LENGTH_SHORT).show();
                        hasError = true;
                    }

                    if (!phone.getText().toString().matches("\\d{8}")) {
                        Toast.makeText(Register.this, "Only 8 digits are needed!", Toast.LENGTH_SHORT).show();
                        hasError = true;
                    }

                    if (genre.getCheckedRadioButtonId() == -1) {
                        Toast.makeText(Register.this, "Select your gender!", Toast.LENGTH_SHORT).show();
                        hasError = true;
                    }

                    if (!conditions.isChecked()) {
                        Toast.makeText(Register.this, "You must accept conditions !", Toast.LENGTH_SHORT).show();
                        hasError = true;
                    }
                }

                if (!hasError) {
                    boolean isAdded = databaseHelper.addUser(usernameStr, emailStr, passwordStr, genreStr, AgeInt, phoneStr);
                    if (isAdded) {
                        Toast.makeText(Register.this, "Singup Success", Toast.LENGTH_SHORT).show();
                        Intent goToSuccessSignUp = new Intent(Register.this, Login.class);
                        startActivity(goToSuccessSignUp);
                    } else {
                        Toast.makeText(Register.this, "Signup failed", Toast.LENGTH_SHORT).show();
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
                    LanguageChanging.changeLanguage(Register.this, "fr");
                } else {
                    LanguageChanging.changeLanguage(Register.this, "en");
                }
                recreate();
            }
        });
    }
}