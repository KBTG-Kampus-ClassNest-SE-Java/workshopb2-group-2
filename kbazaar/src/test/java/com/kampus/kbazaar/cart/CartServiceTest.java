package com.kampus.kbazaar.cart;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CartServiceTest {
    @Mock private CartRepository cartRepository;

    @InjectMocks private CartService cartService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    //    @Test
    //    @DisplayName("Should be success to add product")
}
