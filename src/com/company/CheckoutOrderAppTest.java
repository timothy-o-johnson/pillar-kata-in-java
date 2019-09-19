package com.company;

import java.util.HashMap;

import static org.junit.Assert.*;

public class CheckoutOrderAppTest {

    CheckoutOrderApp checkout;
    Double totalPrice;

    // scans
    Scan soup = new Scan("soup");
    Scan sardines = new Scan("sardines");
    Scan cards = new Scan("cards");
    Scan groundBeef = new Scan("ground beef", 4.0);
    Scan bananas = new Scan("bananas", 5.0);
    Scan batteries = new Scan("batteries");
    Scan orangeJuice = new Scan("orange juice");
    Scan lightBulbs = new Scan("light bulbs");

    // create items []
    Item soupItem = new Item("soup", 1.89);
    Item sardinesItem = new Item("sardines", 0.89);
    Item cardsItem = new Item("cards", 4.0);
    Item batteriesItem = new Item("batteries", 10.0);
    Item lightbulbsItem = new Item("light bulbs", 2.);
    Item orangeJuiceItem = new Item("orange juice", 5.);
    Item groundBeefItem = new Item("ground beef", 2.5, true);
    Item bananasItem = new Item("bananas", 1.00, true);

    // create markdowns
    Markdown soupMarkdown = new Markdown("soup", 1.5);
    Markdown bananasMarkdown = new Markdown("bananas", 0.1);

    // create specials
    Special sardinesSpecial = new Special("xOff", "sardines", 1.0, 1.0, 1.0, 1.0);
    Special cardsSpecial = new Special("xOff", "cards", 2.0, 1.0, 0.5, 2.0);
    Special lightBulbsSpecial = new Special("xOff", "light bulbs", 2.0, 1.0, 1.0, 6.0);

    Special batteriesSpecial = new Special ("nForX","batteries",  3.0,  5.0, 1000.0 );
    Special orangeJuiceSpecial = new Special ("nForX","orange juice",  4.0,  10.0, 12.0 );

    private Double makeDouble(Double totalPrice){
        
        
        return totalPrice;
    }
    
    @org.junit.BeforeClass
    public static void beforeClass(){
        System.out.println("This loads before any tests are called.");
    }

    @org.junit.Before
    public void setup(){
        checkout = new CheckoutOrderApp();
        totalPrice = 0.0;

        // add items to itemList
        Item[] items = {soupItem, sardinesItem, cardsItem, batteriesItem, lightbulbsItem, orangeJuiceItem, groundBeefItem, bananasItem};
        checkout.addItemsToGlobalItemListObjectAndReturnGlobalItemListObject(items);

        // add markdowns
        Markdown[] markdowns = {soupMarkdown, bananasMarkdown};
        checkout.addMarkdowns(markdowns);

        // add specials
        Special[] specials = {sardinesSpecial, cardsSpecial, batteriesSpecial, orangeJuiceSpecial, lightBulbsSpecial};
        checkout.addSpecials(specials);
    }

    @org.junit.Test
    public void whenItemsHaveBeenEnteredShouldReturnAnObjectOfItems(){
        HashMap<String, Double> itemList = new HashMap();

        itemList.put("soup", 1.89);
        itemList.put("sardines", 0.89);
        itemList.put("cards", 4.0);
        itemList.put("batteries", 10.0);
        itemList.put("light bulbs", 2.0);
        itemList.put("orange juice", 5.0);
        itemList.put("ground beef", 2.5);
        itemList.put("bananas", 1.00);

        assertEquals(itemList,checkout.itemList);
    }

    @org.junit.Test
    public void whenItemsAreScannedShouldReturnAnObjectOfScannedObjects(){

        Scan soup = new Scan("soup");
        Scan sardines = new Scan("sardines");
        Scan cards = new Scan("cards");
        Scan[] scans = {soup, sardines, cards};

        HashMap<String, Double> basket = new HashMap();

        basket.put("soup", 1.0);
        basket.put("sardines", 1.0);
        basket.put("cards", 1.0);

        checkout.scanItemsAddToGlobalBasketAndReturnGlobalTotalPrice(scans);

        assertEquals(basket, checkout.basket);
    }

    @org.junit.Test
    public void whenSeveralOfTheSameItemsAreScannedShouldReturnAnObjectOfScannedObjects(){

        Scan[] scans = {soup, sardines, soup, cards};

        HashMap<String, Double> basket = new HashMap();
        basket.put("soup", 2.0);
        basket.put("sardines", 1.0);
        basket.put("cards", 1.0);

        checkout.scanItemsAddToGlobalBasketAndReturnGlobalTotalPrice(scans);

        assertEquals(basket, checkout.basket);
    }

    @org.junit.Test
    public void whenWeightedItemsAreScannedShouldReturnAnObjectOfScannedObjects(){

        Scan[] scans = {soup, sardines, soup, cards, groundBeef};

        HashMap<String, Double> basket = new HashMap();
        basket.put("soup", 2.0);
        basket.put("sardines", 1.0);
        basket.put("cards", 1.0);
        basket.put("ground beef", 4.0);

        checkout.scanItemsAddToGlobalBasketAndReturnGlobalTotalPrice(scans);

        assertEquals(basket, checkout.basket);
    }

    @org.junit.Test
    public void whenScannedTotalShouldReflectAnIncreaseByThePerUnitPrice(){
        Scan[] scans = {soup, sardines, soup, cards, groundBeef};

        totalPrice += (checkout.itemList.get("soup") - checkout.markdowns.get("soup"))* 2;
        totalPrice += checkout.itemList.get("sardines") * 1;
        totalPrice += checkout.itemList.get("cards") * 1;
        totalPrice += checkout.itemList.get("ground beef") * 4;

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
        Scan[] scans = {soup, soup, bananas};
        checkout.scanItemsAddToGlobalBasketAndReturnGlobalTotalPrice(scans);

        totalPrice += (checkout.itemList.get("soup") - 1.5) * 2;
        totalPrice += (checkout.itemList.get("bananas") - 0.1) * 5;

        assertEquals(totalPrice, checkout.getTotalPrice());
    }

    @org.junit.Test
    public void whenABuyNItemsGetMItemsAtXPercentOffSpecialIsCreatedShouldSaveToSpecialsObj(){
        HashMap<String, Special> specialsObj = new HashMap();
        specialsObj.put("sardines", sardinesSpecial);
        specialsObj.put("cards", cardsSpecial);
        specialsObj.put("batteries", batteriesSpecial);
        specialsObj.put("orange juice", orangeJuiceSpecial);
        specialsObj.put("light bulbs", lightBulbsSpecial);

        assertEquals(specialsObj.toString(), checkout.specials.toString());
    }

    @org.junit.Test
    public void whenABuyNItemsGetMItemsAtXPercentOffSpecialIsCreatedShouldApplySpecialToTotalPrice(){
       Scan[] scans = {sardines, sardines, sardines};

       checkout.scanItemsAddToGlobalBasketAndReturnGlobalTotalPrice(scans);
       totalPrice = (checkout.itemList.get("sardines") * 3 - checkout.itemList.get("sardines")) * 100/ 100;
       totalPrice = Math.round (totalPrice * 100.0 ) / 100.0;

       assertEquals(totalPrice, checkout.getTotalPrice());
    }

    @org.junit.Test
    public void whenABuy2ItemsGet1ItemAt50PercentOffSpecialIsCreatedShouldCorrectlyUpdateTotalPrice(){  
        Double regularCardPrice = checkout.itemList.get("cards");
        Double discount = 0.5;

        Scan[] scans = {cards, cards, cards, cards};

        checkout.scanItemsAddToGlobalBasketAndReturnGlobalTotalPrice(scans);
        totalPrice = regularCardPrice * 3 + (1 * discount * regularCardPrice);

        assertEquals(totalPrice, checkout.getTotalPrice());
    }

    @org.junit.Test
    public void whenNForXSpecialIsCreatedShouldSaveToSpecialsObj(){
        HashMap<String, Special> specialsObj = new HashMap();
        specialsObj.put("sardines", sardinesSpecial);
        specialsObj.put("cards", cardsSpecial);
        specialsObj.put("batteries", batteriesSpecial);
        specialsObj.put("orange juice", orangeJuiceSpecial);
        specialsObj.put("light bulbs", lightBulbsSpecial);

        assertEquals(specialsObj.toString(), checkout.specials.toString());
    }

    @org.junit.Test
    public void whenABuy3For5DollarSpecialIsCreatedShouldCorrectlyUpdateTotalPrice(){
        Double regularCardPrice = checkout.itemList.get("batteries");
        Double discountPrice = 5.0;

        Scan[] scans = {batteries, batteries, batteries, batteries, batteries};

        checkout.scanItemsAddToGlobalBasketAndReturnGlobalTotalPrice(scans);
        totalPrice = regularCardPrice * 2 + (1 * discountPrice);

        assertEquals(totalPrice, checkout.getTotalPrice());
    }


    @org.junit.Test
    public void whenAddingBuy4Get10Limit12SpecialShouldUpdateTotalPrice(){   
        Double regularOrangeJuicePrice = checkout.itemList.get("orange juice");

        Scan[] scans = new Scan[20];

        for(int i = 0; i < 20; i++ ){
            scans[i] = orangeJuice;
        }

        checkout.scanItemsAddToGlobalBasketAndReturnGlobalTotalPrice(scans);
        totalPrice = 30 + regularOrangeJuicePrice * 8;

        assertEquals(totalPrice, checkout.getTotalPrice());
    }

    @org.junit.Test
    public void whenAddingBuy2Get1Limit6SpecialShouldUpdateTotalPrice(){
        Double regularLightBulbsPrice = checkout.itemList.get("light bulbs");

        Scan[] scans = new Scan[20];

        for(int i = 0; i < 20; i++ ){
            scans[i] = lightBulbs;
        }


        checkout.scanItemsAddToGlobalBasketAndReturnGlobalTotalPrice(scans);
        totalPrice = regularLightBulbsPrice * (12 + 2);
        

        assertEquals(totalPrice, checkout.getTotalPrice());
    }

    @org.junit.Test
    public void whenRemovingAScannedItemAndInvalidatingAnXForNSpecialShouldUpdateTotalPrice(){
        Double regularOrangeJuicePrice = checkout.itemList.get("orange juice");

        Scan[] scans = new Scan[20];
        Scan[] scansForRemoval = new Scan[10];

        for(int i = 0; i < 20; i++ ){
            scans[i] = orangeJuice;
        }

        checkout.scanItemsAddToGlobalBasketAndReturnGlobalTotalPrice(scans);

        for(int i = 0; i < 10; i++ ){
            scansForRemoval[i] = orangeJuice;
        }

        totalPrice = regularOrangeJuicePrice * 2 + 20;

        assertEquals(totalPrice, checkout.removeScannedItemsReturnTotalPrice(scansForRemoval));
    }

    @org.junit.Test
    public void whenRemovingAScannedItemAndInvalidatingAnXOffSpecialShouldUpdateTotalPrice(){
        Double totalPriceWithOutSpecial = 0.0;
        Double regularLightBulbsPrice = checkout.itemList.get("light bulbs");

        Scan[] scans = new Scan[20];
        Scan[] scansForRemoval = new Scan[13];

        for(int i = 0; i < 20; i++ ){
            scans[i] = lightBulbs;
        }

        checkout.scanItemsAddToGlobalBasketAndReturnGlobalTotalPrice(scans);

        for(int i = 0; i < 13; i++ ){
            scansForRemoval[i] = lightBulbs;
        }

        totalPriceWithOutSpecial = 4 * regularLightBulbsPrice + 2 * 0 + 1 * regularLightBulbsPrice;
        totalPriceWithOutSpecial = Math.round (totalPriceWithOutSpecial * 100.0 ) / 100.0;

        assertEquals(totalPriceWithOutSpecial, checkout.removeScannedItemsReturnTotalPrice(scansForRemoval));
    }

    @org.junit.Test
    public void whenAddingABuyNGetMOfEqualOrLesserValueForXOffShouldAddToSpecialsObject(){
        Special groundBeefSpecial = new Special ("equalOrLesser","ground beef",  2.0, 1.0, 0.5, null );
        Special[] specials = {groundBeefSpecial};
        Scan[] scans = {groundBeef};

        checkout.addSpecials(specials);

        // special price = 2 @ reg price + 1 @ 1/2 price + 1 @ reg price
        totalPrice = 2 * groundBeefItem.price + 1 * groundBeefItem.price/2 + 1 * groundBeefItem.price;

        assertEquals(totalPrice, checkout.scanItemsAddToGlobalBasketAndReturnGlobalTotalPrice(scans));
    }
}