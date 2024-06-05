package com.example.fridgeguard;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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


public class MainActivity extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private ImageView productImageView;
    //private String url = "https://run.mocky.io/v3/85cf9aaf-aa4f-41bf-b10c-308f032f7ccc";
    private String scannedBarcode;
    Button btn_scan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btn_scan = findViewById(R.id.btn_scan);
        productImageView = findViewById(R.id.productImageView);
        btn_scan.setOnClickListener(v ->
        {
            scanCode();
        });

        //getData();
    }

    private void scanCode()
    {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan a barcode");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(com.example.fridgeguard.CaptureAct.class);
        barcodeLauncher.launch(options);

    }
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    scannedBarcode = null;
                } else {
                    scannedBarcode = result.getContents();
                    getData(scannedBarcode);
                }
            });
    public void onButtonClick(View view) {
        scanCode();
        //barcodeLauncher.launch(new ScanOptions());

    }

    private void getData(String barcode) {
        // RequestQueue initialized
        String url = "https://world.openfoodfacts.net/api/v2/product/" + barcode;
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
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

            // Extract specific fields from the JSON response
            String productName = product.optString("generic_name", "N/A");
            String expiryDate = product.optString("expiration_date", "N/A");
            String imageUrl = product.optString("image_front_small_url", null);

            // Display the extracted fields
            String displayText = //"Product: " + productName + "\n" +
                    "ProductName: " + productName + "\n" +
                            "ExpiryDate: " + expiryDate + "\n" ;


            Toast.makeText(getApplicationContext(), displayText, Toast.LENGTH_LONG).show();
            if (imageUrl != null) {
                Glide.with(this)
                        .load(imageUrl)
                        .into(productImageView);
            } else {
                productImageView.setImageResource(R.drawable.ic_launcher_placeholder_background); // Placeholder image if URL is not available
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error parsing JSON response", Toast.LENGTH_SHORT).show();
        }
    }
}
/*
    Button btn_scan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btn_scan = findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(v ->
        {
            scanCode();
        });
    }
    private void scanCode()
    {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan a barcode");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(com.example.fridgeguard.CaptureAct.class);
        barcodeLauncher.launch(options);

    }
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                }
            });
    // Launch
    public void onButtonClick(View view) {
        barcodeLauncher.launch(new ScanOptions());

    }
*/

