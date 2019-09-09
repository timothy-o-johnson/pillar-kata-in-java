package com.company;

import java.util.HashMap;

import static org.junit.Assert.*;

public class CheckoutOrderAppTest {

    CheckoutOrderApp checkout;

    @org.junit.BeforeClass
    public static void beforeClass(){
        System.out.println("This loads before any tests are called.");
    }

    @org.junit.Before
    public void setup(){
        checkout = new CheckoutOrderApp();

        Item soup = new Item("soup", 1.89);
        Item sardines = new Item("sardines", 0.89);
        Item cards = new Item("cards", 4.0);
        Item batteries = new Item("batteries", 10.0);
        Item lightbulbs = new Item("lightbulbs", 2.);
        Item orangeJuice = new Item("orange juice", 5.);
        Item groundBeef = new Item("ground beef", 2.5, true);
        Item bananas = new Item("bananas", 0.25, true);

        Item[] items = {soup, sardines, cards, batteries, lightbulbs, orangeJuice, groundBeef, bananas};

        checkout.addItemsToGlobalItemListObjectAndReturnGlobalItemListObject(items);
        // add items to itemList
       // checkout.
    }

    @org.junit.Test
    public void shouldReturnAnObjectOfItemsAfterItemsHaveBeenEntered(){

        HashMap<String, Double> itemList = new HashMap();

        itemList.put("soup", 1.89);
        itemList.put("sardines", 0.89);
        itemList.put("cards", 4.0);
        itemList.put("batteries", 10.0);
        itemList.put("lightbulbs", 2.0);
        itemList.put("orange juice", 5.0);
        itemList.put("ground beef", 2.5);
        itemList.put("bananas", 0.25);

        System.out.println(checkout.itemList.toString());
        System.out.println(itemList.toString());

        assertEquals(itemList,checkout.itemList);
    }

    @org.junit.Test
    public void shouldReturnAnObjectOfScannedObjectsWhenItemsAreScanned(){

        Scan soup = new Scan("soup");
        Scan sardines = new Scan("sardines");
        Scan cards = new Scan("cards");
        Scan[] scans = {soup, sardines, cards};

        HashMap<String, Double> basket = new HashMap();

        basket.put("soup", 1.0);
        basket.put("sardines", 1.0);
        basket.put("cards", 1.0);

        checkout.scanItemsAddToGlobalBasketAndReturnGlobalTotalPrice(scans);

        System.out.println(checkout.basket.toString());
        System.out.println(basket.toString());

        assertEquals(basket, checkout.basket);
    }

    @org.junit.Test
    public void shouldReturnAnObjectOfScannedObjectsWhenSeveralOfTheSameItemsAreScanned(){

        Scan soup = new Scan("soup");
        Scan sardines = new Scan("sardines");
        Scan cards = new Scan("cards");
        Scan[] scans = {soup, sardines, soup, cards};

        HashMap<String, Double> basket = new HashMap();

        basket.put("soup", 2.0);
        basket.put("sardines", 1.0);
        basket.put("cards", 1.0);

        checkout.scanItemsAddToGlobalBasketAndReturnGlobalTotalPrice(scans);

        System.out.println(checkout.basket.toString());
        System.out.println(basket.toString());

        assertEquals(basket, checkout.basket);
    }

    @org.junit.Test
    public void shouldReturnAnObjectOfScannedObjectsWhenWeightedItemsAreScanned(){
        Scan soup = new Scan("soup");
        Scan sardines = new Scan("sardines");
        Scan cards = new Scan("cards");
        Scan groundBeef = new Scan("ground beef", 5.0);
        Scan[] scans = {soup, sardines, soup, cards, groundBeef};

        HashMap<String, Double> basket = new HashMap();

        basket.put("soup", 2.0);
        basket.put("sardines", 1.0);
        basket.put("cards", 1.0);
        basket.put("ground beef", 5.0);

        checkout.scanItemsAddToGlobalBasketAndReturnGlobalTotalPrice(scans);

        System.out.println(checkout.basket.toString());
        System.out.println(basket.toString());

        assertEquals(basket, checkout.basket);
    }

    @org.junit.Test
    public void totalShouldReflectAnIncreaseByThePerUnitPriceAfterAScan(){
        Scan soup = new Scan("soup");
        Scan sardines = new Scan("sardines");
        Scan cards = new Scan("cards");
        Scan groundBeef = new Scan("ground beef", 5.0);
        Scan[] scans = {soup, sardines, soup, cards, groundBeef};

        HashMap<String, Double> basket = new HashMap();

        Double totalPrice = checkout.itemList.get("soup") * 2 + checkout.itemList.get("sardines") * 1 + checkout.itemList.get("cards") * 1 + checkout.itemList.get("ground beef") * 5;

        System.out.println(totalPrice);

        checkout.scanItemsAddToGlobalBasketAndReturnGlobalTotalPrice(scans);

        assertEquals(totalPrice, checkout.getTotalPrice());
    }
}