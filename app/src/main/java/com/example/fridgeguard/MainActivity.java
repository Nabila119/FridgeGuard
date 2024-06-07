package com.example.fridgeguard;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private EditText productNameEditText, expiryDateEditText;
    private Button submitButton, button1Week, button2Weeks, button3Weeks;
    private ImageView productImageView;
    private Bitmap selectedImageBitmap;
    private DatabaseHelper databaseHelper;

    private final ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    if (extras != null) {
                        selectedImageBitmap = (Bitmap) extras.get("data");
                        productImageView.setImageBitmap(selectedImageBitmap);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productNameEditText = findViewById(R.id.editTextProductName);
        expiryDateEditText = findViewById(R.id.datePickerExpiryDate);
        submitButton = findViewById(R.id.buttonSubmit);
        productImageView = findViewById(R.id.productImageView);
        button1Week = findViewById(R.id.button1Week);
        button2Weeks = findViewById(R.id.button2Weeks);
        button3Weeks = findViewById(R.id.button3Weeks);

        databaseHelper = new DatabaseHelper(this);

        productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        button1Week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExpiryDate(7);
            }
        });

        button2Weeks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExpiryDate(14);
            }
        });

        button3Weeks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExpiryDate(21);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = productNameEditText.getText().toString();
                String expiryDate = expiryDateEditText.getText().toString();
                saveData(productName, expiryDate);
            }
        });

        expiryDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int monthOfYear, int dayOfMonth) {
                                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + selectedYear;
                                expiryDateEditText.setText(selectedDate);
                            }
                        }, year, month, dayOfMonth);

                datePickerDialog.show();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureLauncher.launch(takePictureIntent);
        }
    }

    private void setExpiryDate(int daysToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, daysToAdd);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Months are 0-based in Calendar
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String selectedDate = dayOfMonth + "/" + month + "/" + year;
        expiryDateEditText.setText(selectedDate);
    }

    private void saveData(String productName, String expiryDate) {
        if (selectedImageBitmap == null) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        selectedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        boolean isInserted = databaseHelper.insertProduct(this, productName, expiryDate, byteArray);

        if (isInserted) {
            Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
            productNameEditText.setText("");
            expiryDateEditText.setText("");
            productImageView.setImageResource(android.R.color.transparent);
        } else {
            Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show();

        }
    }}