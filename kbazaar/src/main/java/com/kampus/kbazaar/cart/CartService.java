package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.exceptions.NotFoundException;
import com.kampus.kbazaar.product.Product;
import com.kampus.kbazaar.promotion.Promotion;

import java.util.List;

public class CartService {

    public void addProductToCart(request, username) throws Exception{
        try {
            if((request.promotionCode).length > 0) {
                Promotion promotion = new Promotion();
                List<Promotion> promotionList = promotionRepository.findByCode(request.promotionCode);
                if (promotionList.isEmpty()) {
                    throw new NotFoundException("promotion code is invalid");
                }
            }

            Cart cart = new Cart();
            cart.setUsername(username);
            cartRepository.save(cart);

            CartItem cartItem = new CartItem();
            cartItem.setUsername(username);
            cartItem.setSku(request.productSku);
            cartItem.setQuantity(request.quantity);
        } catch (Exception error) {

        }
    }
}
