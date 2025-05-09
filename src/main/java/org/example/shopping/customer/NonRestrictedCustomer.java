package org.example.shopping.customer;

import org.example.shopping.annotation.ShoppingListValidator;
import org.example.shopping.item.ItemCategory;

import java.util.List;

public class NonRestrictedCustomer implements Customer {

    @ShoppingListValidator
    public boolean unrestrictedAccess(List<ItemCategory> categories, double price) {
        return true;
    }
}