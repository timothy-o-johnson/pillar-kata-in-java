package com.company;

public class Special {
    private String type;
    private String name;
    private Double buyQuantity;
    private Double getBuyQuantity;
    private Double getDiscount;
    private Double limit;

    public Special(String type, String name, Double buyQuantity, Double getBuyQuantity, Double getDiscount, Double limit) {
        this.type = type;
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.getBuyQuantity = getBuyQuantity;
        this.getDiscount = getDiscount;
        this.limit = limit;
    }
}
