package com.company;

public class Item {
    protected String name;
    protected Double price;
    protected Boolean byWeight;

    public Item(String name, Double price, Boolean byWeight) {
        this.name = name;
        this.price = price;
        this.byWeight = byWeight;
    }
    public Item(String name, Double price) {
        this.name = name;
        this.price = price;
        this.byWeight = false;
    }
}
