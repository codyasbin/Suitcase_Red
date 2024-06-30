package com.example.suitcase2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "items.db";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    public static final String TABLE_NAME = "items";

    // Table columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE = "image"; // New column for image
    public static final String COLUMN_PURCHASED = "purchased"; // New column for purchase status
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";


    // Creating table query
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_IMAGE + " BLOB, " +
                    COLUMN_LATITUDE + " REAL, " +
                    COLUMN_LONGITUDE + " REAL, " +
                    COLUMN_PURCHASED + " INTEGER DEFAULT 0);"; // Default 0 means not purchased


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the items table
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing items table and recreate it if the database version changes
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertItem(String name, double price, String description, byte[] image, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_PRICE, price);
        contentValues.put(COLUMN_DESCRIPTION, description);
        contentValues.put(COLUMN_IMAGE, image);
        contentValues.put(COLUMN_LATITUDE, latitude);
        contentValues.put(COLUMN_LONGITUDE, longitude);
        db.insert(TABLE_NAME, null, contentValues);
    }
    public Cursor getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    // Method to mark item as purchased or not purchased
    public void markItemAsPurchased(long id, boolean purchased) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PURCHASED, purchased ? 1 : 0);
        db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Cursor getItemById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
    }

    public boolean updateItem(long id, String name, double price, String description, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_PRICE, price);
        contentValues.put(COLUMN_DESCRIPTION, description);
        contentValues.put(COLUMN_IMAGE, image);

        int affectedRows = db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

        return affectedRows > 0; // Return true if update successful, false otherwise
    }

    // Method to update item location (latitude and longitude)
    public boolean updateItemLocation(long id, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_LATITUDE, latitude);
        contentValues.put(COLUMN_LONGITUDE, longitude);

        int affectedRows = db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

        return affectedRows > 0; // Return true if update successful, false otherwise
    }

    // Method to retrieve item location by ID
    public Cursor getItemLocationById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, new String[]{COLUMN_LATITUDE, COLUMN_LONGITUDE}, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
    }

    public void deleteItem(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
