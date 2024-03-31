package com.kampus.kbazaar.cart;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kampus.kbazaar.security.JwtAuthFilter;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(
        controllers = CartController.class,
        excludeFilters =
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = JwtAuthFilter.class))
class CartControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean CartService cartService;

    @BeforeEach
    public void setup() {
        MockMvcBuilders.standaloneSetup().build();
    }

    @Test
    @DisplayName("should return all cart")
    void getCart_ReturnsAllCart() throws Exception {
        // Set other properties as needed

        when(cartService.getCarts()).thenReturn(new ArrayList<CartResponse>());

        mockMvc.perform(get("/api/v1/carts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(cartService, times(1)).getCarts();
    }

    // TODO:
    //    @Test
    //    public void shouldReturnsOkWhenApplyPromoCode() throws Exception {
    //        mockMvc.perform(
    //                        post("/api/v1/carts/userTest/promotions")
    //                                .contentType(MediaType.APPLICATION_JSON))
    //                .andExpect(status().isOk());
    //    }
}
