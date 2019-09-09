package com.company;

public class Special {
    protected String type;
    protected String name;
    protected Double buyQuantity;
    protected Double getBuyQuantity;
    protected Double getDiscount;
    protected Double limit;

    public Special(String type, String name, Double buyQuantity, Double getBuyQuantity, Double getDiscount, Double limit) {
        this.type = type;
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.getBuyQuantity = getBuyQuantity;
        this.getDiscount = getDiscount;
        this.limit = limit;
    }
}
