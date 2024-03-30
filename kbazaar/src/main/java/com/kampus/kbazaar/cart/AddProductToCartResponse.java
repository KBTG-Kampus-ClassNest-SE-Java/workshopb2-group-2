package com.kampus.kbazaar.cart;

import java.util.List;

public record AddProductToCartResponse(
        String username,
        List<Item> items,
        double discount,
        double totalDiscount,
        double subtotal,
        double grandTotal,
        String promotionCodes
) {}

record Item(
        int id,
        String username,
        String sku,
        String name,
        double price,
        int quantity,
        double discount,
        String promotionCodes
) {}
