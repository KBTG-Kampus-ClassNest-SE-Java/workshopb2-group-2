package com.kampus.kbazaar.cart;

import jakarta.validation.constraints.NotNull;

public record AddProductToCartRequest(@NotNull String productSku, @NotNull Integer quantity) {}
