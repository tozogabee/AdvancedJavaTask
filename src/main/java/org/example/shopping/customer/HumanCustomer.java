package org.example.shopping.customer;

import java.util.List;
import java.util.Random;

import org.example.shopping.annotation.ShoppingListValidator;
import org.example.shopping.item.ItemCategory;

public class HumanCustomer implements Customer{
    private final int age;

    public HumanCustomer() {
        this.age = new Random().nextInt(56) + 12; // 12–67
    }

    @ShoppingListValidator
    public boolean canIBuy(List<ItemCategory> categories, double price) {
        if (age >= 18) {
            return true;
        }

        // Check for alcohol
        return categories.stream().noneMatch(cat -> cat == ItemCategory.ALCOHOL);
    }

    public int getAge() {
        return age;
    }
}

