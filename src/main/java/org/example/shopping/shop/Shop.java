package org.example.shopping.shop;

import org.example.shopping.Pair;
import org.example.shopping.annotation.ShoppingListValidator;
import org.example.shopping.item.Item;
import org.example.shopping.item.ItemCategory;
import org.example.shopping.util.ReaderUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Shop {

    private Map<Item, Integer> itemPiece = new ConcurrentHashMap<>();
    private Map<Item, Double> itemPrice = new ConcurrentHashMap<>();
    private Map<Item, Double> allItems = new ConcurrentHashMap<>();

    public Shop() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream("warehouse.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Pair<Item,Double> readLine = ReaderUtil.readLine(line);
                Item item = readLine.getKey();
                double price = readLine.getValue();
                itemPiece.put(item, 100);
                itemPrice.put(item, price);
                allItems.put(item, price);
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public Map<Item,Integer> getItemPiece() {
        return itemPiece;
    }

    public void listingItemPiece() {
        for (Map.Entry<Item, Integer> entry : itemPiece.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }

    public void listingItemPrice() {
        for (Map.Entry<Item, Double> entry : itemPrice.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }

    public void listingAllItems() {
        for (Map.Entry<Item, Double> entry : allItems.entrySet()) {
            System.out.println("(" + entry.getKey().getName() + "," + entry.getKey().getCategory() + ") - " + entry.getValue());
        }
    }

    public ConcurrentHashMap<Item, Integer> generateRandomShoppingList() {
        ConcurrentHashMap<Item, Integer> shoppingList = new ConcurrentHashMap<>();

        List<Item> availableItems = new ArrayList<>();
        for (Item item : allItems.keySet()) {
            if (itemPiece.getOrDefault(item, 0) > 0) {
                availableItems.add(item);
            }
        }

        if (availableItems.isEmpty()) {
            return shoppingList;
        }

        int numberOfItems = ThreadLocalRandom.current().nextInt(1, availableItems.size() + 1);
        Collections.shuffle(availableItems);

        for (int i = 0; i < numberOfItems; i++) {
            Item item = availableItems.get(i);
            int quantity = ThreadLocalRandom.current().nextInt(1, 25);
            shoppingList.put(item, quantity);
        }

        return shoppingList;
    }


    @ShoppingListValidator
    public void tryToBuy(Map<Item, Integer> shoppingList, Object obj) {
        double totalPrice = 0.0;
        Set<ItemCategory> categoriesInvolved = new HashSet<>();

        for (Map.Entry<Item, Integer> entry : shoppingList.entrySet()) {
            Item item = entry.getKey();
            int requestedAmount = entry.getValue();
            int quantity = entry.getValue();

            int available = itemPiece.getOrDefault(item, 0);
            if (available < requestedAmount) {
                allItems.remove(item);
                System.out.println("Purchase is not possible");
                return;
            }
            double unitPrice = itemPrice.getOrDefault(item, 0.0);

            totalPrice += unitPrice * quantity;
            categoriesInvolved.add(item.getCategory());
            itemPiece.computeIfPresent(entry.getKey(), (k, v) -> v - entry.getValue());
        }

        Method validatorMethod = null;
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(ShoppingListValidator.class)) {
                validatorMethod = method;
                break;
            }
        }

        boolean approved = true;

        if (validatorMethod != null) {
            try {
                validatorMethod.setAccessible(true);
                approved = (boolean) validatorMethod.invoke(obj, new ArrayList<>(categoriesInvolved), totalPrice);
            } catch (Exception e) {
                System.out.println("Validator invocation failed");
                return;
            }
        }

        if (!approved) {
            System.out.println("Rejected sell");
            return;
        }

        if(!categoriesInvolved.isEmpty()) {
            System.out.println("Sell in " + categoriesInvolved + " for " + totalPrice);
        }
    }
}
