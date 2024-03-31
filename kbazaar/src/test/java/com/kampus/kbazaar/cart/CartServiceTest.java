package com.kampus.kbazaar.cart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.kampus.kbazaar.promotion.Promotion;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CartServiceTest {
    @InjectMocks private CartService cartService;
    @Mock private CartRepository cartRepository;

    @BeforeEach
    void setup() {
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

        //        Cart cartResult = cartService.appliedSpecificPromotion(cart, promotion);
        //
        //        assertEquals(new BigDecimal(2), cartResult.getCartItems().get(0).getDiscount());
        //        assertEquals("FIXEDAMOUNT2",
        // cartResult.getCartItems().get(0).getPromotionCodes());

    }

    @Test
    @DisplayName("Should return cart that applay promotion")
    public void applyPromotion() {
        //        cartService.applyPromotion("test-user", "ABC");
    }

    @Test
    @DisplayName("should return cart's item price that not discount")
    void shouldBeNotUpdateCartItemPrice() {
        Promotion promotion = new Promotion();
        promotion.setCode("FIXEDAMOUNT2");
        promotion.setProductSkus("STATIONERY-STAPLER-SWINGLINE,STATIONERY-PENCIL-FABER-CASTELL");
        promotion.setDiscountAmount(new BigDecimal(2));

        // product sku not match to promotion code
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setSku("STATIONERY-NOTEBOOK-MOLESKINE");
        cartItem.setDiscount(new BigDecimal(0));
        cartItem.setPromotionCodes("");
        List<CartItem> cartItemsList = new ArrayList<>();
        cartItemsList.add(cartItem);

        Cart cart = new Cart();
        cart.setCartItems(cartItemsList);

        //        Cart cartResult = cartService.appliedSpecificPromotion(cart, promotion);
        //
        //        assertEquals(new BigDecimal(0), cartResult.getCartItems().get(0).getDiscount());
        //        assertEquals("", cartResult.getCartItems().get(0).getPromotionCodes());
    }
}
