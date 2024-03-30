package com.kampus.kbazaar.cart;

import jakarta.persistence.*;
import java.math.BigDecimal;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Description("discount on cart")
    @Column(name = "discount")
    private BigDecimal discount;

    @Description("total discount product in cart")
    @Column(name = "total_discount")
    private BigDecimal totalDiscount;

    @Column(name = "promotion_codes")
    private String promotionCodes;

    @Description("price total product in cart")
    @Column(name = "sub_total")
    private BigDecimal subtotal;

    @Description("price total after discount all")
    @Column(name = "grand_total")
    private BigDecimal grandTotal;
}
