package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.exceptions.BadRequestException;
import com.kampus.kbazaar.promotion.PromotionResponse;
import com.kampus.kbazaar.promotion.PromotionService;
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

    private final CartItemService cartItemService;

    private final PromotionService promotionService;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            CartItemService cartItemService,
            PromotionService promotionService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartItemService = cartItemService;
        this.promotionService = promotionService;
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
        String[] promotions = cart.getPromotionCodes().split(",");
        BigDecimal totalDiscount = BigDecimal.ZERO;

        for (String promotion : promotions) {
            PromotionResponse foundPromotion = this.promotionService.getPromotionByCode(promotion);

            if (foundPromotion.discountType().equals("FIXED_AMOUNT")
                    && foundPromotion.applicableTo().equals("ENTIRE_CART")) {
                totalDiscount = totalDiscount.add(foundPromotion.discountAmount());
            }
        }

        return totalDiscount;
    }

    public Cart updateSummaryPrice(Long id) {
        val cartOptional = this.cartRepository.findById(id);

        if (cartOptional.isEmpty()) {
            throw new BadRequestException("Cart id not found.");
        }

        val cart = cartOptional.get();
        val discount = calculateDiscountPrice(cart);
        BigDecimal totalDiscount = BigDecimal.ZERO;
        BigDecimal subTotal = BigDecimal.ZERO;

        for (val cartItem : cart.getCartItems()) {
            val totalCartItemDiscount = cartItemService.calculateDiscountPrice(cartItem);

            cartItem.setDiscount(totalCartItemDiscount);
            cartItem.setTotal(cartItem.getPrice().subtract(totalCartItemDiscount));

            totalDiscount = totalDiscount.add(cartItem.getDiscount());
            subTotal = subTotal.add(cartItem.getPrice());
        }

        cart.setDiscount(discount);
        cart.setTotalDiscount(totalDiscount);

        val summaryDiscount = discount.add(totalDiscount);

        cart.setSubtotal(subTotal);
        cart.setGrandTotal(subTotal.subtract(summaryDiscount));

        return cartRepository.save(cart);
    }

    // example add product to cart
    public void addSkuToCart(String username, String skuCode) {
        // do insert cart item by username
        // val cart = cartRepository.save(entity);
        // val grandTotalPrice = calculateGrandTotalPrice(cart);
        // updateGrandTotalPrice();
    }
}
