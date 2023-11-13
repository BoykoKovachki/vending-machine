package com.example.vendingmachine.models;

public class ProductModel {

    private String name;

    private double price;

    private int type;

    public ProductModel(String name, double price, int type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public ProductModel() {
        name = "";
        price = 0;
        type = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
