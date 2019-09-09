package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CheckoutOrderApp {
    protected HashMap<String, Double> itemList = new HashMap();
    protected HashMap<String, Double>  basket = new HashMap();
    protected HashMap<String, Double> markdowns = new HashMap();
    protected HashMap<Special, Integer> specials = new HashMap();
    private Double totalPrice = 0.0;

    public HashMap addItemsToGlobalItemListObjectAndReturnGlobalItemListObject(Item[] items){

        for(Item item: items){
            itemList.put(item.name, item.price);
        }

        return itemList;
    }

    public Double scanItemsAddToGlobalBasketAndReturnGlobalTotalPrice (Scan[] scans){
        for(Scan scan: scans){
            if(scan.weight != null ){
                loadItemIntoBasket(scan.name, scan.weight);
            } else {
                loadItemIntoBasket(scan.name);
            }
        }

        return getTotalPrice();
    }

    private void loadItemIntoBasket(String itemName, Double itemWeight) {
        if(basket.containsKey(itemName)){
            // add item weight to pre-existing item weight
            basket.replace(itemName, basket.get(itemName) + itemWeight);
        } else {
            // add to basket
            basket.put(itemName, itemWeight);
        }
    }

    private void loadItemIntoBasket(String itemName) {
        Double itemCount = basket.get(itemName);

        if(basket.containsKey(itemName)){
            this.basket.replace(itemName, itemCount + 1);
        } else {
            this.basket.put(itemName, 1.0);
        }
    }

    public Double getTotalPrice(){
        Double quantity;
        Double price;
        Double markdown = 0.0;
        totalPrice = 0.0;

        // get array of basket keys
        ArrayList<String> basketItems =  new ArrayList<String>(this.basket.keySet());

        // loop through keys
        for(String item : basketItems){
            quantity = basket.get(item);
            price = itemList.get(item);
            markdown = markdowns.containsKey(item) ? markdowns.get(item) : 0.0 ;

            totalPrice += (price - markdown) * quantity;

        }
        // for each key multiply the basket quantity by the item cost
        return totalPrice;
    }

    public HashMap addMarkdowns(Markdown[] markdowns){
        for(Markdown markdown: markdowns){
            this.markdowns.put(markdown.name, markdown.amount);
        }

        return this.markdowns;
    }

}
