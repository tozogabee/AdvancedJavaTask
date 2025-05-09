package org.example.shopping.util;

import org.example.shopping.Pair;
import org.example.shopping.TriFunction;
import org.example.shopping.item.Item;
import org.example.shopping.item.ItemCategory;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ReaderUtil {

    public static Double readPrice(String price) {
        return Double.parseDouble(price);
    }

    public static Item readItem(String name, String itemName) {
        return new Item(name, ItemCategory.valueOf(itemName.toUpperCase()));
    }

    public static <A, B, C, D, E> TriFunction<A, B, C, Pair<D, E>> parsePair(
            BiFunction<A, B, D> biFunction,
            Function<C, E> function
    ) {
        return (a, b, c) -> {
            D d = biFunction.apply(a, b);
            E e = function.apply(c);
            return new Pair<>(d, e);
        };
    }

    public static Pair<Item, Double> readLine(String line) {
        String[] split = line.split(",");

        return parsePair(
                ReaderUtil::readItem,
                ReaderUtil::readPrice
        ).apply(split[0], split[1], split[2]);
    }

}
