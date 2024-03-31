package com.kampus.kbazaar.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
<<<<<<< HEAD
=======

import com.kampus.kbazaar.promotion.Promotion;
>>>>>>> bf71f61 (create test to pass of update specific promotion code)
import lombok.val;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartResponse> getCarts() {
        List<Cart> carts = cartRepository.findAllWithItems();
        val cartResponses = new ArrayList<CartResponse>();

        for (Cart cart : carts) {
            val cartResponse = cart.toCartResponse();
            val cartItemResponses = new ArrayList<CartItemResponse>();

            for (CartItem cartItem : cart.getCartItems()) {
                CartItemResponse cartItemResponse = cartItem.toCartItemResponse();
                cartItemResponses.add(cartItemResponse);
            }

            cartResponse.setItems(cartItemResponses);
            cartResponses.add(cartResponse);
        }
        return cartResponses;
    }

    // this method will find cart by username
    public Optional<Cart> getCartByUsername(String username) {
        Optional<Cart> cartUser = cartRepository.findAllWithItemsByUsername(username);
        return cartUser;
    }


    // this method will update cart items of user
    public Cart AppliedSpecificPromotion(Cart cartUser, Promotion promotionRequest){
        String[] productSkuArray = promotionRequest.getProductSkus().split(",");
        for(int i = 0 ; i < cartUser.getCartItems().size(); i++){
            for(String productSku : productSkuArray) {
                if(cartUser.getCartItems().get(i).getSku().contains(productSku)){
                    // set discount to promotion discount
                    cartUser.getCartItems().get(i).setDiscount(promotionRequest.getDiscountAmount());
                    // set promotionCode to cart item
                    cartUser.getCartItems().get(i).setPromotionCodes(promotionRequest.getCode());
                }
            }
        }
        System.out.println(cartUser);
        return  cartUser;

    }


    // TODO poc interface
    public BigDecimal calculateDiscountPrice(Cart cart) {
        // TODO implement
        return BigDecimal.ZERO;
    }

    public void updateGrandTotalPrice(Long id, BigDecimal discount) {
        // find by id
        // for update discount, grand total price to item
        // update discount, grand total price to cart
        // TODO implement
    }

    // example add product to cart
    public void addSkuToCart(String username, String skuCode) {
        // do insert cart item by username
        // val cart = cartRepository.save(entity);
        // val grandTotalPrice = calculateGrandTotalPrice(cart);
        // updateGrandTotalPrice();
    }
}
