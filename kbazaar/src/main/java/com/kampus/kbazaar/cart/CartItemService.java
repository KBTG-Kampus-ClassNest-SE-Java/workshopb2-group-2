package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.promotion.PromotionRepository;
import com.kampus.kbazaar.promotion.PromotionResponse;
import com.kampus.kbazaar.promotion.PromotionService;
import java.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    private final PromotionService promotionService;
    private final PromotionRepository promotionRepository;

    public CartItemService(
            PromotionService promotionService, PromotionRepository promotionRepository) {
        this.promotionService = promotionService;
        this.promotionRepository = promotionRepository;
    }

    public BigDecimal calculateDiscountPrice(CartItem cartItem) {
        String[] promotions = cartItem.getPromotionCodes().split(",");
        BigDecimal totalDiscount = BigDecimal.ZERO;

        for (String promotion : promotions) {
            if (StringUtils.isEmpty(promotion)) {
                continue;
            }
            PromotionResponse foundPromotion = this.promotionService.getPromotionByCode(promotion);

            if (foundPromotion.discountType().equals("FIXED_AMOUNT")) {
                totalDiscount = totalDiscount.add(foundPromotion.discountAmount());
            }
        }

        return totalDiscount;
    }
}
