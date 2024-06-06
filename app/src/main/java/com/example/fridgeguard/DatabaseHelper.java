package com.example.fridgeguard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ProductDB";
    private static final int DATABASE_VERSION = 2; // Update the version when you change the schema
    private static final String TABLE_NAME = "Product";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EXPIRY_DATE = "expiryDate";
    private static final String COLUMN_IMAGE_DATA = "imageUrl";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_REMAINING_DAYS = "remainingDays"; // New column name

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EXPIRY_DATE + " TEXT, " +
                COLUMN_IMAGE_DATA + " TEXT, " +
                COLUMN_QUANTITY + " INTEGER DEFAULT 0, " +
                COLUMN_REMAINING_DAYS + " INTEGER)"; // Add the new column for remaining days
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // If upgrading from version 1 to version 2, add the new columns
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_QUANTITY + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_REMAINING_DAYS + " INTEGER");
        }
    }

    public boolean insertProduct(String name, String expiryDate, byte[] imageData, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_EXPIRY_DATE, expiryDate);
        contentValues.put(COLUMN_IMAGE_DATA, imageData); // Adjusted to accept byte array
        contentValues.put(COLUMN_QUANTITY, quantity);

        // Calculate remaining days and put it in contentValues
        int remainingDays = calculateRemainingDays(expiryDate);
        contentValues.put(COLUMN_REMAINING_DAYS, remainingDays);

        Log.d("DatabaseHelper", "Inserting Product: " +
                "Name: " + name +
                ", ExpiryDate: " + expiryDate +
                ", Quantity: " + quantity +
                ", RemainingDays: " + remainingDays);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            Log.e("DatabaseHelper", "Failed to insert data into database");
        } else {
            Log.d("DatabaseHelper", "Data inserted successfully with ID: " + result);
        }
        return result != -1; // Return true if insertion is successful
    }

    // Method to calculate remaining days
    private int calculateRemainingDays(String expiryDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date expirationDate = sdf.parse(expiryDate);
            Date currentDate = new Date();
            long diffInMillies = expirationDate.getTime() - currentDate.getTime();
            Log.d("DatabaseHelper", "Expiry Date: " + expirationDate);
            Log.d("DatabaseHelper", "Current Date: " + currentDate);
            Log.d("DatabaseHelper", "Difference in Milliseconds: " + diffInMillies);
            return (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("DatabaseHelper", "Failed to parse expiry date: " + expiryDate);
            return -1; // Return -1 if parsing fails
        }
    }
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_NAME + ", " + COLUMN_IMAGE_DATA + ", " + COLUMN_REMAINING_DAYS + " FROM " + TABLE_NAME;
        return db.rawQuery(query, null);
    }

}