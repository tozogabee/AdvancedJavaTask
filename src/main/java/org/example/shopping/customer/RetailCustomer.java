package org.example.shopping.customer;

import java.util.List;
import java.util.Random;

import org.example.shopping.annotation.ShoppingListValidator;
import org.example.shopping.item.ItemCategory;

public class RetailCustomer implements Customer {
    private final double budget;

    public RetailCustomer() {
        this.budget = new Random().nextInt(901) + 100; // 100–1000
    }

    @ShoppingListValidator
    public boolean checkBudget(List<ItemCategory> categories, double price) {
        return price <= budget;
    }

    public double getBudget() {
        return budget;
    }
}

