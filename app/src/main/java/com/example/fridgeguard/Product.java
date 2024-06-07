package com.example.fridgeguard;

public class Product {
    private int id;
    private String name;
    private int remainingDays;
    private byte[] imageData;

    public Product(int id, String name, int remainingDays, byte[] imageData) {
        this.id = id;
        this.name = name;
        this.remainingDays = remainingDays;
        this.imageData = imageData;
    }

    public int getId() {
        return id;
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
