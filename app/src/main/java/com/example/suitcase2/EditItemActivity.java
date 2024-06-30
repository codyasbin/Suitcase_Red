package com.example.suitcase2;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.drawable.BitmapDrawable;


import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

public class EditItemActivity extends AppCompatActivity {

    private ImageView editItemImage;
    private EditText editItemName;
    private EditText editItemPrice;
    private EditText editItemDescription;
    private Button buttonUpdate;

    private DatabaseHelper myDbHelper;
    private long itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // Initialize DatabaseHelper
        myDbHelper = new DatabaseHelper(this);

        // Initialize views
        editItemImage = findViewById(R.id.edit_item_image);
        editItemName = findViewById(R.id.edit_item_name);
        editItemPrice = findViewById(R.id.edit_item_price);
        editItemDescription = findViewById(R.id.edit_item_description);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        // Get item ID from Intent
        Intent intent = getIntent();
        itemId = intent.getLongExtra("ITEM_ID", -1);

        if (itemId == -1) {
            // Invalid item ID, handle error or finish activity
            Toast.makeText(this, "Invalid item ID", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // Load item data for editing
            loadItemData();
        }

        // Set onClickListener for Update button
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItem();
            }
        });
    }

    // Method to load item data from database
    private void loadItemData() {
        Cursor cursor = myDbHelper.getItemById(itemId);

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            double price = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRICE));
            String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION));
            byte[] image = cursor.getBlob(cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE));

            // Set data to views
            editItemName.setText(name);
            editItemPrice.setText(String.valueOf(price));
            editItemDescription.setText(description);

            // Load image if available
            if (image != null && image.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                editItemImage.setImageBitmap(bitmap);
            } else {
                // Set default image or placeholder
                editItemImage.setImageResource(R.drawable.add);
            }
        }

        cursor.close();
    }

    // Method to update item in database
    private void updateItem() {
        String name = editItemName.getText().toString().trim();
        String priceString = editItemPrice.getText().toString().trim();
        String description = editItemDescription.getText().toString().trim();

        if (name.isEmpty()) {
            editItemName.setError("Name cannot be empty");
            editItemName.requestFocus();
            return;
        }

        if (priceString.isEmpty()) {
            editItemPrice.setError("Price cannot be empty");
            editItemPrice.requestFocus();
            return;
        }

        double price = Double.parseDouble(priceString);

        // Convert image to byte array (if needed)
        byte[] image = convertImageToByteArray(); // Call method to convert image to byte array

        // Update item in database
        boolean updated = myDbHelper.updateItem(itemId, name, price, description, image);

        if (updated) {
            Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close activity after update
        } else {
            Toast.makeText(this, "Failed to update item", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to convert image to byte array
    private byte[] convertImageToByteArray() {
        // Get the bitmap from ImageView
        Bitmap bitmap = ((BitmapDrawable) editItemImage.getDrawable()).getBitmap();

        // Convert bitmap to byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
