package com.example.fridgeguard; // Adjust this to match your package name

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ProductDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Product";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EXPIRY_DATE = "expiryDate";
    private static final String COLUMN_IMAGE_URL = "imageUrl";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EXPIRY_DATE + " TEXT, " +
                COLUMN_IMAGE_URL + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertProduct(String name, String expiryDate, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_EXPIRY_DATE, expiryDate);
        contentValues.put(COLUMN_IMAGE_URL, imageUrl);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1; // Return true if insertion is successful
    }
}
