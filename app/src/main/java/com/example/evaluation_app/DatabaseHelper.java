package com.example.evaluation_app;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "users";
    public static final String _ID = "_id";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String GENRE = "genre";
    public static final String AGE = "age";
    public static final String PHONE = "phone";
    static final String DB_NAME = "USERS_AUTH.DB";
    static final int DB_VERSION = 2;
    public static final String RATINGS_TABLE_NAME = "ratings";
    public static final String RATINGS_ID = "id";
    public static final String RATINGS_USER_ID = "user_id";
    public static final String RATINGS_VALUE = "rating";
    public static final String RATINGS_TIMESTAMP = "timestamp";

    String createTable = "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            USERNAME + " TEXT, " + EMAIL + " TEXT, " + PASSWORD + " TEXT, " +
            GENRE + " TEXT, " + AGE + " INTEGER, " + PHONE + " TEXT)";

    String createRatingsTable = "CREATE TABLE " + RATINGS_TABLE_NAME + " (" +
            RATINGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RATINGS_USER_ID + " INTEGER, " +
            RATINGS_VALUE + " REAL, " +
            RATINGS_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            "FOREIGN KEY(" + RATINGS_USER_ID + ") REFERENCES " + TABLE_NAME + "(" + _ID + "))";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createTable);
        sqLiteDatabase.execSQL(createRatingsTable);
        Log.d("DatabaseHelper", "Tables created: " + createTable + ", " + createRatingsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d("DatabaseHelper", "Upgrading database from version " + oldVersion + " to " + newVersion);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RATINGS_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean addUser(String username, String email, String password, String genre, Integer age, String phone) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        String hashedPassword = hashPassword(password);

        values.put(USERNAME, username);
        values.put(EMAIL, email);
        values.put(PASSWORD, hashedPassword);
        values.put(GENRE, genre);
        values.put(AGE, age);
        values.put(PHONE, phone);
        try {
            long result = db.insert(TABLE_NAME, null, values);
            db.close();
            return result != -1;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "L'erreur est: " + e.getMessage());
            return false;
        }
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String hashedPassword = hashPassword(password);

        String query = "SELECT " + _ID + " FROM " + TABLE_NAME +
                " WHERE " + EMAIL + " = ? AND " + PASSWORD + " = ?";

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{email, hashedPassword});

            if (cursor != null && cursor.moveToFirst()) {
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
                Log.d("UserLogin", "User ID retrieved: " + userId);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertRating(int userId, float ratingValue) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Préparer les valeurs à insérer
        ContentValues contentValues = new ContentValues();
        contentValues.put(RATINGS_USER_ID, userId); // ID de l'utilisateur connecté
        contentValues.put(RATINGS_VALUE, ratingValue); // Valeur de l'évaluation

        // Insérer dans la table Ratings
        long result = db.insert(RATINGS_TABLE_NAME, null, contentValues);

        // Vérifier si l'insertion a réussi
        if (result == -1) {
            Log.d("RatingInsert", "Failed to insert rating");
        } else {
            Log.d("RatingInsert", "Rating inserted successfully");
        }

        db.close();
    }

    public boolean hasUserRated(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ratings WHERE user_id = ?", new String[]{String.valueOf(userId)});

        boolean hasRated = cursor.getCount() > 0;
        cursor.close();
        return hasRated;
    }

    public float getRatingValue(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT rating FROM ratings WHERE user_id = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            int ratingColumnIndex = cursor.getColumnIndex("rating");
            if (ratingColumnIndex >= 0) {
                float rating = cursor.getFloat(ratingColumnIndex);
                cursor.close();
                return rating;
            } else {
                cursor.close();
                return -1;
            }
        }
        cursor.close();
        return -1;
    }


}
