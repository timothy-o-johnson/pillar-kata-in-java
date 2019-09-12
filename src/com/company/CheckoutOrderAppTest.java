package com.company;

import java.util.HashMap;

import static org.junit.Assert.*;

public class CheckoutOrderAppTest {

    CheckoutOrderApp checkout;

    // scans
    Scan soup = new Scan("soup");
    Scan sardines = new Scan("sardines");
    Scan cards = new Scan("cards");
    Scan groundBeef = new Scan("ground beef", 5.0);
    Scan bananas = new Scan("bananas", 5.0);

    // create items []
    Item soupItem = new Item("soup", 1.89);
    Item sardinesItem = new Item("sardines", 0.89);
    Item cardsItem = new Item("cards", 4.0);
    Item batteriesItem = new Item("batteries", 10.0);
    Item lightbulbsItem = new Item("lightbulbs", 2.);
    Item orangeJuiceItem = new Item("orange juice", 5.);
    Item groundBeefItem = new Item("ground beef", 2.5, true);
    Item bananasItem = new Item("bananas", 1.00, true);

    // create markdowns
    Markdown soupMarkdown = new Markdown("soup", 1.5);
    Markdown bananasMarkdown = new Markdown("bananas", 0.1);

    @org.junit.BeforeClass
    public static void beforeClass(){
        System.out.println("This loads before any tests are called.");
    }

    @org.junit.Before
    public void setup(){
        checkout = new CheckoutOrderApp();

        // add items to itemList
        Item[] items = {soupItem, sardinesItem, cardsItem, batteriesItem, lightbulbsItem, orangeJuiceItem, groundBeefItem, bananasItem};
        checkout.addItemsToGlobalItemListObjectAndReturnGlobalItemListObject(items);

        // add markdowns
        Markdown[] markdowns = {soupMarkdown, bananasMarkdown};
        checkout.addMarkdowns(markdowns);
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
        itemList.put("bananas", 1.00);

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
        Double totalPrice = 0.0;

        Scan[] scans = {soup, sardines, soup, cards, groundBeef};

        HashMap<String, Double> basket = new HashMap();

        totalPrice += (checkout.itemList.get("soup") - checkout.markdowns.get("soup"))* 2;
        totalPrice += checkout.itemList.get("sardines") * 1;
        totalPrice += checkout.itemList.get("cards") * 1;
        totalPrice += checkout.itemList.get("ground beef") * 5;

        System.out.println(totalPrice);

        checkout.scanItemsAddToGlobalBasketAndReturnGlobalTotalPrice(scans);

        assertEquals(totalPrice, checkout.getTotalPrice());
    }

    @org.junit.Test
    public void whenAddingMarkDownsShouldSaveMarkDownsToMarkdownObject(){
        HashMap<String, Double> markdownObj = new HashMap();
        markdownObj.put("soup", 1.5);
        markdownObj.put("bananas", 0.1);

        assertEquals(markdownObj.toString(), checkout.markdowns.toString());
    }

    @org.junit.Test
    public void whenScanningAPerUnitItemTotalPriceShouldReflectThePerUnitCostLessTheMarkdown(){
        Double totalPrice = (checkout.itemList.get("soup") - 1.5) * 2;

        Scan[] scans = {soup, soup};
        checkout.scanItemsAddToGlobalBasketAndReturnGlobalTotalPrice(scans);

        assertEquals(totalPrice, checkout.getTotalPrice());
    }

    @org.junit.Test
    public void whenScanningAByWeightItemTotalPriceShouldReflectTheByWeightCostLessTheMarkdown(){
        Double totalPrice = 0.0;

        Scan[] scans = {soup, soup, bananas};
        checkout.scanItemsAddToGlobalBasketAndReturnGlobalTotalPrice(scans);

        totalPrice += (checkout.itemList.get("soup") - 1.5) * 2;
        totalPrice += (checkout.itemList.get("bananas") - 0.1) * 5;

        assertEquals(totalPrice, checkout.getTotalPrice());
    }

    @org.junit.Test
    public void whenABuyNItemsGetMItemsAtXPercentOffSpecialIsCreatedShouldSaveToSpecialsObj(){
       Special sardinesSpecial = new Special("xOff", "sardines", 1.0, 1.0, 1.0, 1.0);
       Special[] specials = {sardinesSpecial};
       checkout.addSpecials(specials);

        HashMap<String, Special> specialsObj = new HashMap();
        specialsObj.put("sardines", sardinesSpecial);

        assertEquals(specialsObj.toString(), checkout.specials.toString());
    }

    @org.junit.Test
    public void whenABuyNItemsGetMItemsAtXPercentOffSpecialIsCreatedShouldApplySpecialToTotalPrice(){
        Double totalPrice = 0.0;

        Special sardinesSpecial = new Special("xOff", "sardines", 1.0, 1.0, 1.0, 1.0);
        Special[] specials = {sardinesSpecial};
        checkout.addSpecials(specials);

       Scan[] scans = {sardines, sardines, sardines};

       checkout.scanItemsAddToGlobalBasketAndReturnGlobalTotalPrice(scans);
       totalPrice = (checkout.itemList.get("sardines") * 3 - checkout.itemList.get("sardines")) * 100/ 100;
       totalPrice = Math.round (totalPrice * 100.0 ) / 100.0;
       assertEquals(totalPrice, checkout.getTotalPrice());
    }
}