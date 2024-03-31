package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.promotion.PromotionResponse;
import com.kampus.kbazaar.promotion.PromotionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartItemService {

    private final PromotionService promotionService;

    public CartItemService(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

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
}
