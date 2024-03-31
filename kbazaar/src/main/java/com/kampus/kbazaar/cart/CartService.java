package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.exceptions.BadRequestException;
import com.kampus.kbazaar.exceptions.InternalServerException;
import com.kampus.kbazaar.exceptions.NotFoundException;
import com.kampus.kbazaar.product.Product;
import com.kampus.kbazaar.product.ProductRepository;
import com.kampus.kbazaar.promotion.*;
import com.kampus.kbazaar.promotion.PromotionResponse;
import com.kampus.kbazaar.promotion.PromotionService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartItemService cartItemService;
    private final PromotionService promotionService;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            CartItemService cartItemService,
            PromotionService promotionService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartItemService = cartItemService;
        this.promotionService = promotionService;
        this.productRepository = productRepository;
    }

    public List<CartResponse> getCarts() {
        List<Cart> carts = cartRepository.findAll();
        val cartResponses = new ArrayList<CartResponse>();

        for (Cart cart : carts) {
            val cartResponse = cart.toCartResponse();
            val cartItemResponses = new ArrayList<CartItemResponse>();

            for (CartItem cartItem : cart.getCartItems()) {
                CartItemResponse cartItemResponse = cartItem.toCartItemResponse();
                cartItemResponses.add(cartItemResponse);
            }

            cartResponse.setItems(cartItemResponses);
            cartResponses.add(cartResponse);
        }
        return cartResponses;
    }

    // this method will find cart by username
    public Optional<Cart> getCartByUsername(String username) {
        return cartRepository.findAllWithItemsByUsername(username);
    }

    @Transactional
    public AddProductToCartResponse addProductToCart(
            AddProductToCartRequest request, String username) {
        try {
            // check user cart is exist
            Optional<Cart> cartOptional = cartRepository.findByUsername(username);

            // if user have cart
            if (cartOptional.isPresent()) {
                Cart cart = cartOptional.get();

                // insert item to user cart
                CartItem cartItem = new CartItem();
                cartItem.setUsername(username);
                cartItem.setSku(request.productSku());

                // find item price from product
                BigDecimal price;
                Optional<Product> productOptional =
                        productRepository.findOneBySku(request.productSku());
                if (productOptional.isPresent()) {
                    Product product = productOptional.get();
                    cartItem.setName(product.getName());
                    price = product.getPrice();
                    cartItem.setPrice(price);
                } else {
                    throw new NotFoundException("Product not found");
                }

                cartItem.setQuantity(request.quantity());

                // summarize sub-total and grand-total for user cart
                List<CartItem> cartItems = cartItemRepository.findByUsername(username);
                BigDecimal subTotal = BigDecimal.ZERO;
                if (!cartItems.isEmpty()) {
                    for (CartItem item : cartItems) {
                        subTotal = subTotal.add(item.getPrice());
                    }
                    subTotal = subTotal.add(price);
                    cart.setSubtotal(subTotal);
                    cartRepository.save(cart);
                } else {
                    subTotal = price;
                    cart.setSubtotal(price);
                    cartRepository.save(cart);
                }

                return new AddProductToCartResponse(username, cartItem, subTotal);
            } else {
                Cart cart = new Cart();
                cart.setUsername(username);

                // insert item to user cart
                CartItem cartItem = new CartItem();
                cartItem.setUsername(username);
                cartItem.setSku(request.productSku());

                // find item price from product
                BigDecimal price;
                Optional<Product> productOptional =
                        productRepository.findOneBySku(request.productSku());
                if (productOptional.isPresent()) {
                    Product product = productOptional.get();
                    cartItem.setName(product.getName());
                    price = product.getPrice();
                    cartItem.setPrice(price);
                    cart.setSubtotal(price);
                } else {
                    throw new NotFoundException("Product not found");
                }

                cartItem.setQuantity(request.quantity());
                cartRepository.save(cart);

                return new AddProductToCartResponse(username, cartItem, price);
            }
        } catch (Exception error) {
            throw new InternalServerException("internal server error");
        }
    }

    public CartResponse applyPromotion(PromotionRequest promotionRequest, String username) {
        Optional<Cart> cartOptional = cartRepository.findByUsername(username);
        if (cartOptional.isEmpty()) {
            log.error("cart not found");
            throw new NotFoundException("cart not found");
        }
        Cart cart = cartOptional.get();

        if ("ENTIRE_CART".equals(promotionRequest.getApplicableTo())) {
            return applyCartPromotion(cart, promotionRequest.getCode());
        } else {
            return appliedSpecificPromotion(cart, promotionRequest).toCartResponse();
        }
    }

    public CartResponse applyCartPromotion(Cart cart, String code) {
        List<String> allPromotion = new ArrayList<>(List.of(cart.getPromotionCodes().split(",")));
        allPromotion.add(code);
        String result = allPromotion.stream()
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(","));
        cart.setPromotionCodes(result);
        cartRepository.save(cart);
        Cart updatedCart = updateSummaryPrice(cart.getId());
        return updatedCart.toCartResponse();
    }

    // this method will update cart items of user
    public Cart appliedSpecificPromotion(Cart cartUser, PromotionRequest promotionRequest) {
        String[] productSkuArray = promotionRequest.getProductSkus().split(",");
        for (int i = 0; i < cartUser.getCartItems().size(); i++) {
            for (String productSku : productSkuArray) {
                if (cartUser.getCartItems().get(i).getSku().contains(productSku)) {
                    // set discount to promotion discount
                    cartUser.getCartItems()
                            .get(i)
                            .setDiscount(promotionRequest.getDiscountAmount());
                    // set promotionCode to cart item
                    cartUser.getCartItems().get(i).setPromotionCodes(promotionRequest.getCode());
                }
            }
        }
        return cartUser;
    }

    public BigDecimal calculateDiscountPrice(Cart cart) {
        String[] promotions = cart.getPromotionCodes().split(",");
        BigDecimal totalDiscount = BigDecimal.ZERO;

        for (String promotion : promotions) {
            PromotionResponse foundPromotion = this.promotionService.getPromotionByCode(promotion);

            if (foundPromotion.discountType().equals("FIXED_AMOUNT")
                    && foundPromotion.applicableTo().equals("ENTIRE_CART")) {
                totalDiscount = totalDiscount.add(foundPromotion.discountAmount());
            }
        }

        return totalDiscount;
    }

    public Cart updateSummaryPrice(Long id) {
        val cartOptional = this.cartRepository.findById(id);

        if (cartOptional.isEmpty()) {
            throw new BadRequestException("Cart id not found.");
        }

        val cart = cartOptional.get();
        val discount = calculateDiscountPrice(cart);
        BigDecimal totalDiscount = BigDecimal.ZERO;
        BigDecimal subTotal = BigDecimal.ZERO;

        for (val cartItem : cart.getCartItems()) {
            // cal()
            val totalCartItemDiscount = cartItemService.calculateDiscountPrice(cartItem);

            cartItem.setDiscount(totalCartItemDiscount);
            cartItem.setTotal(cartItem.getPrice().subtract(totalCartItemDiscount));

            totalDiscount = totalDiscount.add(cartItem.getDiscount());
            subTotal = subTotal.add(cartItem.getPrice());
        }

        cart.setDiscount(discount);
        cart.setTotalDiscount(totalDiscount);

        val summaryDiscount = discount.add(totalDiscount);

        cart.setSubtotal(subTotal);
        cart.setGrandTotal(subTotal.subtract(summaryDiscount));

        return cartRepository.save(cart);
    }
}
