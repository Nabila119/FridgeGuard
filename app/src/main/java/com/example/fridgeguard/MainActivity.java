package com.example.fridgeguard;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;


public class MainActivity extends AppCompatActivity {

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
}