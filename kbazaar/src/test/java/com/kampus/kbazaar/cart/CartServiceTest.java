package com.kampus.kbazaar.cart;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.kampus.kbazaar.exceptions.BadRequestException;
import com.kampus.kbazaar.promotion.Promotion;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.kampus.kbazaar.promotion.PromotionResponse;
import com.kampus.kbazaar.promotion.PromotionService;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class CartServiceTest {

    @InjectMocks @Spy private CartService cartService;
    @Mock private CartRepository cartRepository;

    @Mock private CartItemService cartItemService;

    @Mock private PromotionService promotionService;

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

        Cart cartResult = cartService.appliedSpecificPromotion(cart, promotion);

        assertEquals(new BigDecimal(2), cartResult.getCartItems().get(0).getDiscount());
        assertEquals("FIXEDAMOUNT2", cartResult.getCartItems().get(0).getPromotionCodes());
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

        Cart cartResult = cartService.appliedSpecificPromotion(cart, promotion);

        assertEquals(new BigDecimal(0), cartResult.getCartItems().get(0).getDiscount());
        assertEquals("", cartResult.getCartItems().get(0).getPromotionCodes());
    }

    @Test
    void updateSummaryPrice_ShouldThrowBadRequest_WhenCartNotFound() {
        // arrange
        val mockId = 1L;
        when(this.cartRepository.findById(any())).thenReturn(Optional.empty());

        // act
        val thrown = assertThrows(BadRequestException.class, () -> {
            this.cartService.updateSummaryPrice(mockId);
        });

        // assert
        assertEquals("Cart id not found.", thrown.getMessage());
        verify(this.cartRepository.findById(mockId));
    }

    @Test
    void updateSummaryPrice_ShouldSuccess_WhenRequestIsCorrect() {
        // arrange
        val mockId = 1L;
        val mockCart = new Cart();
        mockCart.setPromotionCodes("A,B");

        val mockCartItem1 = new CartItem();
        mockCartItem1.setPrice(new BigDecimal(200));

        val mockCartItem2 = new CartItem();
        mockCartItem2.setPrice(new BigDecimal(100));

        val mockPromotion1 = new PromotionResponse(
                1L,
                "A",
                "AAA",
                "",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "FIXED_AMOUNT",
                new BigDecimal(10),
                BigDecimal.ZERO,
                "ENTIRE_CART",
                List.of(""),
                1,
                2
        );

        val mockPromotion2 = new PromotionResponse(
                2L,
                "B",
                "BBB",
                "",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "FIXED_AMOUNT",
                new BigDecimal(20),
                BigDecimal.ZERO,
                "ENTIRE_CART",
                List.of(""),
                1,
                2
        );

        val mockPromotion3 = new PromotionResponse(
                3L,
                "C",
                "CCC",
                "",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "FIXED_AMOUNT",
                new BigDecimal(30),
                BigDecimal.ZERO,
                "ENTIRE_CART",
                List.of(""),
                1,
                2
        );

        mockCart.setCartItems(List.of(mockCartItem1, mockCartItem2));

        when(this.cartRepository.findById(any())).thenReturn(Optional.of(mockCart));
        when(this.promotionService.getPromotionByCode("A")).thenReturn(mockPromotion1);
        when(this.promotionService.getPromotionByCode("B")).thenReturn(mockPromotion2);
        when(this.promotionService.getPromotionByCode("C")).thenReturn(mockPromotion3);
        when(this.cartItemService.calculateDiscountPrice(mockCartItem1)).thenReturn(new BigDecimal(50));
        when(this.cartItemService.calculateDiscountPrice(mockCartItem2)).thenReturn(new BigDecimal(100));
        when(this.cartRepository.save(any())).thenReturn(mockCart);

        // act
        val res = this.cartService.updateSummaryPrice(mockId);

        // assert
        assertEquals(mockCart, res);
        verify(this.cartService).calculateDiscountPrice(mockCart);
        assertEquals(new BigDecimal(30), mockCart.getDiscount());
        assertEquals(new BigDecimal(150), mockCart.getTotalDiscount());
        assertEquals(new BigDecimal(300), mockCart.getSubtotal());
        assertEquals(new BigDecimal(120), mockCart.getGrandTotal());
    }
}
