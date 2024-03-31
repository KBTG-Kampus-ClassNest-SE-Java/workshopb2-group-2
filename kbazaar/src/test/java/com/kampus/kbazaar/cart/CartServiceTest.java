package com.kampus.kbazaar.cart;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kampus.kbazaar.promotion.Promotion;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class CartServiceTest {

    @InjectMocks private CartService cartService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return cart that already updated!")
    void shouldBeAbleToGetUpdateProductItems() {
        Promotion promotion = new Promotion();
        promotion.setCode("FIXEDAMOUNT2");
        promotion.setProductSkus("STATIONERY-STAPLER-SWINGLINE,STATIONERY-PENCIL-FABER-CASTELL");
        promotion.setDiscountAmount(new BigDecimal(2));

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setSku("STATIONERY-STAPLER-SWINGLINE");
        cartItem.setDiscount(new BigDecimal(0));
        cartItem.setPromotionCodes("");
        List<CartItem> cartItemsList = new ArrayList<>();
        cartItemsList.add(cartItem);

        Cart cart = new Cart();
        cart.setCartItems(cartItemsList);

        Cart cartResult = cartService.AppliedSpecificPromotion(cart, promotion);

        assertEquals(new BigDecimal(2), cartResult.getCartItems().get(0).getDiscount());
        assertEquals("FIXEDAMOUNT2", cartResult.getCartItems().get(0).getPromotionCodes());
    }
}
