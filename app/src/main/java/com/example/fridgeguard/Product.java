package com.example.fridgeguard;

public class Product {
    private String name;
    private int remainingDays;
    private byte[] imageData;

    public Product(String name, int remainingDays, byte[] imageData) {
        this.name = name;
        this.remainingDays = remainingDays;
        this.imageData = imageData;
    }

    public String getName() {
        return name;
    }

    public int getRemainingDays() {
        return remainingDays;
    }

    public byte[] getImageData() {
        return imageData;
    }
}
