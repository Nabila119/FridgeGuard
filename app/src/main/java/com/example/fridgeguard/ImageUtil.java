package com.example.fridgeguard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {

    public static String saveImageToInternalStorage(Context context, byte[] imageData, String imageName) {
        File directory = context.getDir("images", Context.MODE_PRIVATE);
        if (!directory.exists()) {
            directory.mkdir();
        }

        File imagePath = new File(directory, imageName + ".jpg");
        try (FileOutputStream fos = new FileOutputStream(imagePath)) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            Log.d("ImageUtil", "Image saved successfully at " + imagePath.getAbsolutePath());
            return imagePath.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ImageUtil", "Failed to save image", e);
            return null;
        }
    }
}
