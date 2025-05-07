package org.example.shopping.item;

import java.util.Arrays;

public class Item {

    private String name;
    private ItemCategory category;

    public Item(String name, ItemCategory category) {
        this.name = name;
        this.category = category;
    }

    public Item() {}

    public String getName() {
        return name;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public boolean isHealthy(){
         Arrays.stream(category.values())
                .map(itemCategory -> {
                    switch (itemCategory){
                        case FOOD:
                            return true;
                        case ALCOHOL:
                            return false;
                        default:
                            throw new IllegalStateException("Not supported question: <"+(name)+">");
                    }
                });
         return false;
    }




}
