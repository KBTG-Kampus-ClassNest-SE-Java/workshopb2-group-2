package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.exceptions.InternalServerException;
import com.kampus.kbazaar.exceptions.NotFoundException;
import com.kampus.kbazaar.product.Product;
import com.kampus.kbazaar.product.ProductRepository;
import com.kampus.kbazaar.promotion.Promotion;
import com.kampus.kbazaar.promotion.PromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final PromotionRepository promotionRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, PromotionRepository promotionRepository, CardItemRepository cardItemRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.promotionRepository = promotionRepository;
        this.cartItemRepository = cardItemRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public AddProductToCartResponse addProductToCart(AddProductToCartRequest request, String username) throws Exception{
        try {
            // check user cart is exist
            Optional<Cart> cartOptional = cartRepository.findByUsername(username);

            // if user have cart
            if(cartOptional.isPresent()) {
                Cart cart = cartOptional.get();

                // insert item to user cart
                CartItem cartItem = new CartItem();
                cartItem.setUsername(username);
                cartItem.setSku(request.productSku());

                // find item price from product
                BigDecimal price = BigDecimal.ZERO;
                Optional<Product> productOptional = productRepository.findOneBySku(request.productSku());
                if(productOptional.isPresent()) {
                    Product product = productOptional.get();
                    price = product.getPrice();
                    cartItem.setPrice(price);
                } else {
                    throw new Exception("price not found");
                }

                cartItem.setQuantity(request.quantity());
                cartItemRepository.save(cartItem);

                // summarize sub-total and grand-total for user cart
                Optional<List<CartItem>> cartItemOptional = cartItemRepository.findByUsername(username);
                BigDecimal subTotal = BigDecimal.ZERO;
                if (cartItemOptional.isPresent()) {
                    for (CartItem item : cartItemOptional.get()) {
                        subTotal = subTotal.add(item.getPrice());
                    }
                    subTotal = subTotal + price;
                    cart.setSubtotal(subTotal);
                    cartRepository.save(cart);
                } else {
                    subTotal = price;
                    cart.setSubtotal(price);
                    cartRepository.save(cart);
                }

                AddProductToCartResponse addProductToCartResponse = new AddProductToCartResponse(username, cartItem, subTotal);
                return addProductToCartResponse;
            } else {
                Cart cart = new Cart();
                cart.setUsername(username);

                // insert item to user cart
                CartItem cartItem = new CartItem();
                cartItem.setUsername(username);
                cartItem.setSku(request.productSku());

                // find item price from product
                BigDecimal price = BigDecimal.ZERO;
                Optional<Product> productOptional = productRepository.findOneBySku(request.productSku());
                if(productOptional.isPresent()) {
                    Product product = productOptional.get();
                    price = product.getPrice();
                    cartItem.setPrice(price);
                    cart.setSubtotal(price);
                } else {
                    throw new Exception("price not found");
                }

                cartRepository.save(cart);

                cartItem.setQuantity(request.quantity());
                cartItemRepository.save(cartItem);

                AddProductToCartResponse addProductToCartResponse = new AddProductToCartResponse(username, cartItem, price);
                return addProductToCartResponse;
            }
        } catch (Exception error) {
            throw new InternalServerException("internal server error");
        }
    }
}

/*
            if(request.promotionCodes() != null && !request.promotionCodes().isEmpty()) {
                cart.setPromotionCodes(request.promotionCodes());
                cartItem.setPromotionCodes(request.promotionCodes());
                Optional<Promotion> promotionOptional = promotionRepository.findByCode(request.promotionCodes());
                if (promotionOptional.isPresent()) {
                   Promotion promotion = promotionOptional.get();
                   cart.setDiscount(promotion.getDiscountAmount());
                   cartItem.setDisCount(promotion.getDiscountAmount());
                } else {
                    throw new NotFoundException("promotion code is invalid");
                }
 */