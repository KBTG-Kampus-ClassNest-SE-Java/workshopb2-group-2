package com.kampus.kbazaar.cart;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CartServiceTest {

    @Mock private CartRepository cartRepository;

    @InjectMocks private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("should be able to get all cart and item")
    void shouldBeAbleToGetCarts() {
        // Mock data
        List<CartItem> cartItemList = new ArrayList<CartItem>();
        CartItem cartItem1 =
                new CartItem(
                        1L,
                        "BOSS",
                        "MOBILE-APPLE-IPHONE-12-PRO",
                        "Apple iPhone 12 Pro",
                        new BigDecimal("20990.25"),
                        1,
                        "",
                        new BigDecimal("0"),
                        new BigDecimal("20990.25"),
                        new Cart());
        CartItem cartItem2 =
                new CartItem(
                        2L,
                        "BOSS",
                        "MOBILE-SAMSUNG-GALAXY-S21-ULTRA",
                        "Samsung Galaxy S21 Ultra",
                        new BigDecimal("18990.00"),
                        1,
                        "",
                        new BigDecimal("0"),
                        new BigDecimal("18990.00"),
                        new Cart());
        cartItemList.add(cartItem1);
        cartItemList.add(cartItem2);
        Cart cart =
                new Cart(
                        1L,
                        "BOSS",
                        new BigDecimal(0),
                        new BigDecimal(0),
                        "",
                        new BigDecimal(39980.25),
                        new BigDecimal(39980.25),
                        cartItemList);
        List<Cart> CartList = new ArrayList<Cart>();
        CartList.add(cart);
        when(cartRepository.findAllWithItems()).thenReturn(CartList);

        List<CartResponse> result = cartService.getCarts();

        assertEquals("BOSS", result.get(0).getUsername());
        assertEquals("MOBILE-APPLE-IPHONE-12-PRO", result.get(0).getItems().get(0).getSku());
        assertEquals("Apple iPhone 12 Pro", result.get(0).getItems().get(0).getName());
        assertEquals("MOBILE-SAMSUNG-GALAXY-S21-ULTRA", result.get(0).getItems().get(1).getSku());
        assertEquals("Samsung Galaxy S21 Ultra", result.get(0).getItems().get(1).getName());
        assertEquals(new BigDecimal(39980.25), result.get(0).getGrandTotal());
    }
}
