package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.exceptions.InternalServerException;
import com.kampus.kbazaar.product.Product;
import com.kampus.kbazaar.product.ProductRepository;
import com.kampus.kbazaar.promotion.PromotionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import lombok.val;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final PromotionRepository promotionRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, PromotionRepository promotionRepository, CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.promotionRepository = promotionRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }


    public List<CartResponse> getCarts() {
        List<Cart> carts = cartRepository.findAllWithItems();
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
                    cartItem.setName(product.getName());
                    price = product.getPrice();
                    cartItem.setPrice(price);
                } else {
                    throw new Exception("price not found");
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
                    cartItem.setName(product.getName());
                    price = product.getPrice();
                    cartItem.setPrice(price);
                    cart.setSubtotal(price);
                } else {
                    throw new Exception("price not found");
                }



                cartItem.setQuantity(request.quantity());
                cartRepository.save(cart);

                AddProductToCartResponse addProductToCartResponse = new AddProductToCartResponse(username, cartItem, price);
                return addProductToCartResponse;
            }
        } catch (Exception error) {
            throw new InternalServerException("internal server error");
        }
    }

    // TODO poc interface
    public BigDecimal calculateDiscountPrice(Cart cart) {
        // TODO implement
        return BigDecimal.ZERO;
    }

    public void updateGrandTotalPrice(Long id, BigDecimal discount) {
        // find by id
        // for update discount, grand total price to item
        // update discount, grand total price to cart
        // TODO implement
    }

    // example add product to cart
    public void addSkuToCart(String username, String skuCode) {
        // do insert cart item by username
        // val cart = cartRepository.save(entity);
        // val grandTotalPrice = calculateGrandTotalPrice(cart);
        // updateGrandTotalPrice();
    }
}





