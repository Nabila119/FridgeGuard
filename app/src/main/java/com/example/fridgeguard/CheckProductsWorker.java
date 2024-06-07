package com.example.fridgeguard;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;

public class CheckProductsWorker extends Worker {
    private static final String TAG = "CheckProductsWorker";

    public CheckProductsWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        List<Product> productList = databaseHelper.getAllProducts();

        for (Product product : productList) {
            if (product.getRemainingDays() < 2) {
                NotificationHelper.sendNotification(getApplicationContext(), "Product Expiring Soon",
                        product.getName() + " is expiring in " + product.getRemainingDays() + " days.");
            }
        }
        return Result.success();
    }
}
