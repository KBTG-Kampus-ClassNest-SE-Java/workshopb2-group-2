package com.kampus.kbazaar.cart;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CartController {


    @GetMapping("/carts")
    public ResponseEntity getCart() { // NOSONAR
        return ResponseEntity.ok().build();
    }

    @PostMapping("/carts/{username}/items")
    public AddProductToCartResponse addProductToCart(@Validated  @RequestBody AddProductToCartRequest request, @PathVariable String username) throws Exception {
       return cartService.addProductToCart(request, username);
    }
}
