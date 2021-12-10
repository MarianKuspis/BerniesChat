package com.utm.utmshop;

public class Basket {
    String name;
    String price;
    String image;
    String id;

    Basket() {}

    public Basket(String name, String price, String image, String id) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.id = id;
    }

    public Basket(String name, String price, String image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public Basket(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPrice() { return price; }

    public void setPrice(String price) { this.price = price; }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }
}
