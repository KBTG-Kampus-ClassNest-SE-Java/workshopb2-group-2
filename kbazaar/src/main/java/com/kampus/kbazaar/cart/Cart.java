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

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "total_discount")
    private BigDecimal totalDiscount;

    @Column(name = "promotion_codes")
    private String promotionCodes;

    @Description("precisely reflect its pre-discount status")
    @Column(name = "subtotal")
    private BigDecimal subtotal;

    @Description("the final, all-inclusive amount to be paid.")
    @Column(name = "grand_total")
    private BigDecimal grandTotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getPromotionCodes() {
        return promotionCodes;
    }

    public void setPromotionCodes(String promotionCodes) {
        this.promotionCodes = promotionCodes;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }
}
