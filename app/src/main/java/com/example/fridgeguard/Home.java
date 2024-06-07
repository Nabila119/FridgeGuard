package com.example.fridgeguard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Home extends AppCompatActivity implements ProductAdapter.OnDeleteClickListener {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();

        productAdapter = new ProductAdapter(productList, this);
        recyclerView.setAdapter(productAdapter);

        databaseHelper = new DatabaseHelper(this);
        loadProducts();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        scheduleProductCheck();

        // Execute the check immediately when the app is launched
        checkProductsImmediately();
    }

    private void loadProducts() {
        productList.clear();
        productList.addAll(databaseHelper.getAllProducts());
        productAdapter.notifyDataSetChanged();
    }

    public void launchAddItem(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void launchScanItem(View v) {
        Intent i = new Intent(this, Scan.class);
        startActivity(i);
    }

    @Override
    public void onDeleteClick(Product product) {
        int productId = product.getId();
        databaseHelper.deleteProduct(productId);
        loadProducts(); // Refresh the product list
    }
    private void scheduleProductCheck() {
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(CheckProductsWorker.class, 1, TimeUnit.MINUTES)
                .build();
        WorkManager.getInstance(this).enqueue(workRequest);
    }

    private void checkProductsImmediately() {
        OneTimeWorkRequest immediateWorkRequest = new OneTimeWorkRequest.Builder(CheckProductsWorker.class)
                .build();
        WorkManager.getInstance(this).enqueue(immediateWorkRequest);
    }
}