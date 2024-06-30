package com.example.suitcase2;

public class Item {
    private long id; // Unique ID for the item
    private String name;
    private double price;
    private String description;
    private byte[] image;
    private boolean purchased; // New field for purchase status
    private double latitude;
    private double longitude;


    public Item(long id, String name, double price, String description, byte[] image, boolean purchased) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.purchased = purchased;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public byte[] getImage() {
        return image;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
