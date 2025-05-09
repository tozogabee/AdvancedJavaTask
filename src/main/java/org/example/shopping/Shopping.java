package org.example.shopping;

import org.example.shopping.customer.Customer;
import org.example.shopping.customer.HumanCustomer;
import org.example.shopping.customer.NonRestrictedCustomer;
import org.example.shopping.customer.RetailCustomer;
import org.example.shopping.item.Item;
import org.example.shopping.item.ItemCategory;
import org.example.shopping.shop.Shop;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Shopping {

    public static void main(String[] args) {
        Shop shop = new Shop();
        try (ExecutorService executor = Executors.newFixedThreadPool(10)) {

            for (int i = 0; i < 50; i++) {
                executor.execute(() -> {
                    Map<Item, Integer> shoppingList = shop.generateRandomShoppingList();
                    Object customer = generateRandomCustomer();

                    shop.tryToBuy(shoppingList, customer);
                });
            }

            executor.shutdown();
        } // 10 szál egyszerre
    }

    private static Customer generateRandomCustomer() {
        Random random = new Random();
        int customerType = random.nextInt(3);

        switch (customerType) {
            case 0:
                return new HumanCustomer();
            case 1:
                return new RetailCustomer();
            case 2:
                return new NonRestrictedCustomer();
            default:
                throw new IllegalStateException("Unexpected customer type");
        }
    }
}