package com.example.fridgeguard;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class Scan extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private ImageView productImageView;
    private TextView productNameTextView;
    private EditText expiryDateEditText;
    private Button submitButton;
    private String scannedBarcode;
    private DatabaseHelper databaseHelper;
    private String productName;
    private byte[] imageData;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    scannedBarcode = null;
                } else {
                    scannedBarcode = result.getContents();
                    getData(scannedBarcode);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        productImageView = findViewById(R.id.imageView1);
        productNameTextView = findViewById(R.id.textViewProductName);
        expiryDateEditText = findViewById(R.id.editTextExpiryDate);
        submitButton = findViewById(R.id.buttonSubmit);
        databaseHelper = new DatabaseHelper(this);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expiryDate = expiryDateEditText.getText().toString();
                saveDataWithImage(productName, expiryDate, imageData);
            }
        });
        expiryDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Scan.this,
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

        scanCode();
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan a barcode");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barcodeLauncher.launch(options);
    }

    private void getData(String barcode) {
        String url = "https://world.openfoodfacts.net/api/v2/product/" + barcode;
        mRequestQueue = Volley.newRequestQueue(this);

        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error: " + error.toString());
                Toast.makeText(getApplicationContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    private void parseResponse(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject product = jsonResponse.getJSONObject("product");

            productName = product.optString("generic_name", "N/A");
            String imageUrl = product.optString("image_front_small_url", null);

            productNameTextView.setText(productName);

            if (imageUrl != null) {
                Glide.with(this)
                        .load(imageUrl)
                        .into(productImageView);
                downloadAndSaveImage(imageUrl);
            } else {
                productImageView.setImageResource(R.drawable.ic_launcher_placeholder_background);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error parsing JSON response", Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadAndSaveImage(String imageUrl) {
        ImageDownloader imageDownloader = new ImageDownloader();
        imageDownloader.downloadImage(imageUrl, new ImageDownloader.ImageDownloadCallback() {
            @Override
            public void onImageDownloaded(byte[] imageData) {
                Scan.this.imageData = imageData;
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getApplicationContext(), "Error downloading image", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error downloading image: " + e.getMessage());
            }
        });
    }

    private void saveDataWithImage(String productName, String expiryDate, byte[] imageData) {
        boolean isInserted = databaseHelper.insertProduct(this, productName, expiryDate, imageData);

        if (isInserted) {
            Toast.makeText(this, "Product saved to database", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save product", Toast.LENGTH_SHORT).show();
        }
    }
}