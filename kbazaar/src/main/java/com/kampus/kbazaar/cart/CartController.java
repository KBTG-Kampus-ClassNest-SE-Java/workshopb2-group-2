package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.promotion.PromotionRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/carts")
    public List<CartResponse> getCart() {
        return cartService.getCarts();
    }

    @PostMapping("/carts/{username}/items")
    public AddProductToCartResponse addProductToCart(
            @Validated @RequestBody AddProductToCartRequest request,
            @PathVariable String username) {
        return cartService.addProductToCart(request, username);
    }

    @PostMapping("/carts/{username}/promotions")
    public ResponseEntity applyPromotions(
            @Validated @RequestBody PromotionRequest promotion, @PathVariable String username) {
        return new ResponseEntity(cartService.applyPromotion(promotion, username), HttpStatus.OK);
    }
}
