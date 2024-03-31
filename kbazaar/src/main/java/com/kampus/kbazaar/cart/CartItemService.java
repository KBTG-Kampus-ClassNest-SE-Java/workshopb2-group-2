package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.promotion.PromotionResponse;
import com.kampus.kbazaar.promotion.PromotionService;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    private final PromotionService promotionService;
    private final CartItemRepository cartItemRepository;

    public CartItemService(
            PromotionService promotionService, CartItemRepository cartItemRepository) {
        this.promotionService = promotionService;
        this.cartItemRepository = cartItemRepository;
    }

    // TODO poc interface
    public BigDecimal calculateDiscountPrice(CartItem cartItem) {
        String[] promotions = cartItem.getPromotionCodes().split(",");
        BigDecimal totalDiscount = BigDecimal.ZERO;

        for (String promotion : promotions) {
            PromotionResponse foundPromotion = this.promotionService.getPromotionByCode(promotion);

            if (foundPromotion.discountType().equals("FIXED_AMOUNT")) {
                totalDiscount = totalDiscount.add(foundPromotion.discountAmount());
            }
        }

        return totalDiscount;
    }

    // public void updateGrandTotalPrice(Long id, BigDecimal discount) {
    //     val cartItemOptional = this.cartItemRepository.findById(id);
    //     if (cartItemOptional.isEmpty()) {
    //         throw new BadRequestException("Cart id not found.");
    //     }
    //
    //     val cartItem = cartItemOptional.get();
    //     val totalDiscount = calculateDiscountPrice(cartItem);
    //
    //
    //     // update discount, grand total price to item
    //     // TODO implement
    // }
}
