package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CheckoutOrderApp {
    protected HashMap<String, Double> itemList = new HashMap();
    protected HashMap<String, Double> basket = new HashMap();
    protected HashMap<String, Double> markdowns = new HashMap();
    protected HashMap<String, Special> specials = new HashMap();
    private Double totalPrice = 0.0;

    public HashMap addItemsToGlobalItemListObjectAndReturnGlobalItemListObject(Item[] items) {

        for (Item item : items) {
            itemList.put(item.name, item.price);
        }

        return itemList;
    }

    public Double scanItemsAddToGlobalBasketAndReturnGlobalTotalPrice(Scan[] scans) {
        for (Scan scan : scans) {
            if (scan.weight != null) {
                loadItemIntoBasket(scan.name, scan.weight);
            } else {
                loadItemIntoBasket(scan.name);
            }
        }

        return getTotalPrice();
    }

    private void loadItemIntoBasket(String itemName, Double itemWeight) {
        if (basket.containsKey(itemName)) {
            // add item weight to pre-existing item weight
            basket.replace(itemName, basket.get(itemName) + itemWeight);
        } else {
            // add to basket
            basket.put(itemName, itemWeight);
        }
    }

    private void loadItemIntoBasket(String itemName) {
        Double itemCount = basket.get(itemName);

        if (basket.containsKey(itemName)) {
            this.basket.replace(itemName, itemCount + 1);
        } else {
            this.basket.put(itemName, 1.0);
        }
    }

    public Double getTotalPrice() {
        Double regularPriceQuantity;
        Double regularPrice;
        Double markdown = 0.0;
        Double discountedQuantity = 0.0;
        Double discountedPrice = 0.0;
        totalPrice = 0.0;
        HashMap<String, Double> specialsObj = new HashMap<>();


        // get array of basket keys
        ArrayList<String> basketItems = new ArrayList<String>(this.basket.keySet());

        // loop through keys
        for (String item : basketItems) {
            regularPriceQuantity = basket.get(item);
            regularPrice = itemList.get(item);
            markdown = markdowns.getOrDefault(item, 0.0);
            specialsObj = applySpecials(item, regularPriceQuantity, regularPrice);

            regularPriceQuantity = specialsObj.getOrDefault("regularPriceQuantity",regularPriceQuantity);
            discountedQuantity = specialsObj.getOrDefault("discountedQuantity", 0.0);
            discountedPrice = specialsObj.getOrDefault("discountedPrice",0.0);

            totalPrice += (regularPrice - markdown) * regularPriceQuantity + discountedPrice * discountedQuantity;

        }
        // for each key multiply the basket quantity by the item cost
        return totalPrice;
    }

    public HashMap addMarkdowns(Markdown[] markdowns) {
        for (Markdown markdown : markdowns) {
            this.markdowns.put(markdown.name, markdown.amount);
        }

        return this.markdowns;
    }

    public HashMap addSpecials(Special[] specials) {
        for (Special special : specials) {
            this.specials.put(special.name, special);
        }

        return this.specials;
    }

    public HashMap<String, Double> applySpecials(String item, Double basketQuantity, Double regularPrice) {
        Special special = specials.get(item);
        String specialType = special == null ? " ": special.type;
        Double discountedPrice = 0.0;
        Double discountedQuantity = 0.0;
        Double regularPriceQuantity = basketQuantity;
        HashMap<String, Double> applySpecialsObj = new HashMap<>();

        switch (specialType) {
            case "xOff": //"Buy N items, get M at X% off"
                applySpecialsObj = calculateNForXOffSpecials(basketQuantity, special, regularPrice);
//                discountedPrice = nForXObj.get("discountedPrice");
//                discountedQuantity = nForXObj.get("discountedQuantity");
//                regularPriceQuantity = nForXObj.get("regularPriceQuantity");

                break;
            case "nForX": // "Buy N items for X dollars"
                applySpecialsObj = calculateNForXSpecials(basketQuantity, special);
            default:
                break;
        }

        return applySpecialsObj;
    }

    private HashMap<String, Double> calculateNForXOffSpecials(Double basketQuantity, Special special, Double regularPrice) {
        HashMap<String, Double> xOffSpecialsObj = new HashMap<>();

        Double basketQuantityTemp = basketQuantity;
        Double buyQuantity = special.buyQuantity;
        Double getQuantity = special.getQuantity;
        Double discount = special.getDiscount;
        Double limit = special.limit;
        Double discountedQuantity = 0.0;

        while (basketQuantityTemp > buyQuantity && basketQuantityTemp - buyQuantity >= getQuantity) {
            discountedQuantity += getQuantity;
            basketQuantityTemp -= buyQuantity + getQuantity;

            if (discountedQuantity == limit) break;
        }

        xOffSpecialsObj.put("discountedPrice", (1 - discount) * regularPrice);
        xOffSpecialsObj.put("discountedQuantity", discountedQuantity );
        xOffSpecialsObj.put("regularPriceQuantity", basketQuantity - discountedQuantity);

        return xOffSpecialsObj;
    }

    private HashMap<String, Double> calculateNForXSpecials(Double basketQuantity, Special special) {
        HashMap<String, Double> xOffSpecialsObj = new HashMap<>();

        Double basketQuantityTemp = basketQuantity;
        Double buyQuantity = special.buyQuantity;
        double discount = special.salesPrice;
        double limit = special.limit;
        double discountedQuantity = 0.0;

        while (basketQuantityTemp >= buyQuantity) {
            discountedQuantity += buyQuantity;
            basketQuantityTemp -= buyQuantity;

            if (discountedQuantity == limit) break;
        }

        xOffSpecialsObj.put("discountedPrice", discount);
        xOffSpecialsObj.put("discountedQuantity", Math.floor(discountedQuantity / buyQuantity) );
        xOffSpecialsObj.put("regularPriceQuantity", basketQuantity - discountedQuantity);

        return xOffSpecialsObj;
    }

    public  Double removeScannedItemsReturnTotalPrice(Scan[] scans){
        double newQuantity;

        for(Scan scan: scans){
            if(this.basket.containsKey(scan.name)){
                if(this.basket.get(scan.name) > 0){
                    newQuantity = this.basket.get(scan.name) - 1 ;
                    this.basket.replace(scan.name, newQuantity);
                }
            }

        }
        return getTotalPrice();
    }
}