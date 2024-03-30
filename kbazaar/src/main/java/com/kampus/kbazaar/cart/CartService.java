package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.product.Product;
import com.kampus.kbazaar.promotion.Promotion;

import java.util.List;

public class CartService {

    public void addProductToCart(request, username) throws Exception{
        try {
            Cart cart = new Cart();
            cart.setUsername(username);
            cartRepository.save(cart);

            CartItem cartItem = new CartItem();
            cartItem.setUsername(username);
            cartItem.setSku(request.productSku);
            cartItem.setQuantity(request.quantity);

            Product product =
        } catch (Exception error) {

        }
    }
}
