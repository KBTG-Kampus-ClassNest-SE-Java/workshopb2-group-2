package com.kampus.kbazaar.cart;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    // TODO poc interface
    public BigDecimal calculateDiscountPrice(CartItem cartItem) {
        // TODO implement
        return BigDecimal.ZERO;
    }

    public void updateGrandTotalPrice(Long id, BigDecimal discount) {
        // update discount, grand total price to item
        // TODO implement
    }
}
