package com.company;

public class Special {
    protected String type;
    protected String name;
    protected Double buyQuantity;
    protected Double getQuantity;
    protected Double getDiscount;
    protected Double limit;

    public Special(String type, String name, Double buyQuantity, Double getQuantity, Double getDiscount, Double limit) {
        this.type = type;
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.getQuantity = getQuantity;
        this.getDiscount = getDiscount;
        this.limit = limit;
    }
    public Special(String type, String name, Double buyQuantity, Double getQuantity) {
        this.type = type;
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.getQuantity = getQuantity;
        this.getDiscount = 0.0;
        this.limit = Double.MAX_VALUE;
    }
}
