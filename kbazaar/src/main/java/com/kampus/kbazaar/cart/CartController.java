package com.kampus.kbazaar.cart;

import java.util.List;
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
}
