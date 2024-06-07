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

 import java.util.ArrayList;
 import java.util.List;


 public class Home extends AppCompatActivity {
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

        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);

        databaseHelper = new DatabaseHelper(this);
        loadProducts();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
     private void loadProducts() {
         // Fetch products from the database
         productList.clear(); // Clear existing data
         productList.addAll(databaseHelper.getAllProducts());
         productAdapter.notifyDataSetChanged(); // Notify the adapter of data change
     }
    public void launchAddItem(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    public void launchScanItem(View v){
         Intent i = new Intent(this, Scan.class);
         startActivity(i);
     }
}