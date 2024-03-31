package com.kampus.kbazaar.cart;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AddProductToCartRequest(
        @NotNull
        String productSku,
        @NotNull
        Integer quantity,
        String promotionCodes
) {
}
