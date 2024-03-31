package com.kampus.kbazaar.cart;

import java.math.BigDecimal;

public record AddProductToCartResponse(String username, CartItem items, BigDecimal subtotal) {}
